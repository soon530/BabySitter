package tw.tasker.babysitter.presenter.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.layer.LayerImpl;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.fragment.ListDialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class BabysittersParseQueryAdapter extends ParseQueryAdapter<Babysitter> {
	private int defaultDistance = 2;
	private int count = 2;
	private boolean isColor = true;
	private boolean mIsFirst = true;
	private RatingBar mBabyCount;
	private String mExpandableObjectID = "";
	private CircleImageView mAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private TextView mAge;
	private ProgressDialog mRingProgressDialog;
	private BabysitterFavorite mBabysitterFavorite;

	// message
	private ArrayList<String> mTargetParticipants;
    //The owning conversation
    private Conversation mConversation;


	public BabysittersParseQueryAdapter(Context context) {
		super(context, getQueryFactory(context));
	}

	@Override
	public View getItemView(final Babysitter babysitter, View view,
			ViewGroup parent) {
		// boolean recycle = false;
		View rootView;

		if (view == null) {
			// recycle = false;
			// rootView = View.inflate(getContext(), R.layout.list_item_sitter,
			// null);
			LayoutInflater mInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView = mInflater.inflate(R.layout.list_item_sitter, parent,
					false);
		} else {
			rootView = view;
			// recycle = true;
		}
		
		mAge = (TextView) rootView.findViewById(R.id.age);
		mAge.setText("("+babysitter.getAge()+")");

		mAvatar = (CircleImageView) rootView.findViewById(R.id.avatar);
		getOldAvator(babysitter);
		
		final LinearLayout expandable = (LinearLayout) rootView
				.findViewById(R.id.expandable);
		final LinearLayout expandableToggle = (LinearLayout) rootView
				.findViewById(R.id.expandable_toggle_button);

		final ImageView arrow = (ImageView) rootView.findViewById(R.id.arrow);

//		expandableToggle.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				
//				if (clickDuration < MAX_CLICK_DURATION) {
//					if (expandable.getVisibility() == View.VISIBLE) {
//						arrow.animate().rotation(0).start();
//						mIsExpandableSitter = "";
//					} else {
//						arrow.animate().rotation(180).start();
//						mIsExpandableSitter = babysitter.getObjectId();
//					}
//				}
//				return false;
//			}
//		});

		
		if (mExpandableObjectID.equals(babysitter.getObjectId())) {
			arrow.setRotation(180);
		} else {
			arrow.setRotation(0);
		}

		TextView name = (TextView) rootView.findViewById(R.id.name);
		TextView address = (TextView) rootView.findViewById(R.id.address);
		TextView babycareTime = (TextView) rootView
				.findViewById(R.id.babycare_time);

		TextView babysitterNumber = (TextView) rootView
				.findViewById(R.id.babysitterNumber);
		TextView education = (TextView) rootView.findViewById(R.id.education);
		TextView communityName = (TextView) rootView
				.findViewById(R.id.communityName);

		SpannableString content = new SpannableString(
				babysitter.getCommunityName());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		communityName.setText(content);

		Button contact = (Button) rootView.findViewById(R.id.contact);
		
		if (isFavoriteSitter(babysitter)) {
			contact.setEnabled(false);
			contact.setText("已送請求");
		} else {
			contact.setEnabled(true);
			contact.setText("保母媒合");
		}
		
		contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				String[] phones = babysitter.getTel().replace("(日):", "")
//						.replace("手機: ", "").split(" ");
//				LogUtils.LOGD("vic", "phones" + babysitter.getTel());
//				for (String phone : phones) {
//					LogUtils.LOGD("vic", "phone" + phone);
//				}

//				showBabysitterPhone(phones);
				
				addFavorite(babysitter);
				pushTextToSitter(babysitter);
				newConversationWithSitter(babysitter.getUser().getObjectId());
			}
			
			protected void newConversationWithSitter(String objectId) {
			    
		        if(mTargetParticipants == null)
		            mTargetParticipants = new ArrayList<>();
		            
		        mTargetParticipants.add(objectId);
		        mTargetParticipants.add(ParseUser.getCurrentUser().getObjectId());
		           
	            //First Check to see if we have a valid Conversation object
	            if(mConversation == null){
	                //Make sure there are valid participants. Since the Authenticated user will always be
	                // included in a new Conversation, we check to see if there is more than one target participant
	                if(mTargetParticipants.size() > 1) {

	                    //Create a new conversation, and tie it to the QueryAdapter
	                    mConversation = LayerImpl.getLayerClient().newConversation(mTargetParticipants);
	                    //createMessagesAdapter();

	                    //Once the Conversation object is created, we don't allow changing the Participant List
	                    // Note: this is an implementation choice. It is always possible to add/remove participants
	                    // after a Conversation has been created
	                    //hideAddParticipantsButton();

	                } else {
	                    //showAlert("Send Message Error","You need to specify at least one participant before sending a message.");
	                    return;
	                }
	            }

	            
	            String text = "向您提出托育請求";

	            //If the input is valid, create a new Message and send it to the Conversation
	            if(mConversation != null && text != null && text.length() > 0){

	                MessagePart part = LayerImpl.getLayerClient().newMessagePart(text);
	                Message msg = LayerImpl.getLayerClient().newMessage(part);
	                mConversation.send(msg);


	            } else {
	                //showAlert("Send Message Error","You cannot send an empty message.");
	            }

				
			}

			private void pushTextToSitter(Babysitter babysitter) {
				ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
				LogUtils.LOGD("vic", "push obj:" + babysitter.getUser().getObjectId());
				//ParseObject obj = ParseObject.createWithoutData("user", "KMyQfnc5k3");
				pushQuery.whereEqualTo("user", babysitter.getUser());
				
				// Send push notification to query
				ParsePush push = new ParsePush();
				push.setQuery(pushQuery); // Set our Installation query
				//push.setMessage("有爸媽，想找你帶小孩唷~");
				JSONObject data = getJSONDataMessageForIntent();
				push.setData(data);
				push.sendInBackground(new SendCallback() {
					
					@Override
					public void done(ParseException e) {
						if (e != null)
							LogUtils.LOGD("vic", "erroe" + e.getMessage());
					}
				});
				
			}
			
			private void addFavorite(Babysitter sitter) {
				mRingProgressDialog = ProgressDialog.show(getContext(),
				"請稍等 ...", "加入收藏中...", true);

				Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class, sitter.getObjectId());
				UserInfo userInfo = ParseObject.createWithoutData(UserInfo.class, Config.userInfo.getObjectId());

				BabysitterFavorite babysitterfavorite = new BabysitterFavorite();
				mBabysitterFavorite = babysitterfavorite;
				// favorite.put("baby", mBaby);
				babysitterfavorite.setBabysitter(babysitter);
				babysitterfavorite.setUserInfo(userInfo);
				
				babysitterfavorite.put("user", ParseUser.getCurrentUser());
				babysitterfavorite.setIsParentConfirm(true);
				babysitterfavorite.setIsSitterConfirm(false);
				
				babysitterfavorite.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							// Toast.makeText(getActivity().getApplicationContext(),
							// "saving doen!", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getContext(),
									"Error saving: " + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
						mRingProgressDialog.dismiss();
					}

				});
			}

			private JSONObject getJSONDataMessageForIntent() {
				try
			    {
			        JSONObject data = new JSONObject();
			        data.put("alert", "家長["+ParseUser.getCurrentUser().getUsername()+"]，想找你帶小孩唷~");
			        //instead action is used
			        //data.put("customdata", "custom data value");
			        data.put("user", ParseUser.getCurrentUser().getObjectId());
			        return data;
			    }
			    catch(JSONException x)
			    {
			        throw new RuntimeException("Something wrong with JSON", x);
			    }
			}
		});

		mBabyCount = (RatingBar) rootView.findViewById(R.id.babycareCount);
		int babyCount = DisplayUtils.getBabyCount(babysitter.getBabycareCount());
		mBabyCount.setRating(babyCount);

		name.setText(babysitter.getName());
		address.setText(babysitter.getAddress());
		
		String changeText = DisplayUtils.getChangeText(babysitter.getBabycareTime());
		
		babycareTime.setText(changeText);

		
		babysitterNumber.setText("保母證號：" + babysitter.getSkillNumber());
		education.setText("教育程度：" + babysitter.getEducation());

		TextView km = (TextView) rootView.findViewById(R.id.km);
		ImageView kmLine = (ImageView) rootView.findViewById(R.id.km_line);

		float distance = (float) babysitter.getLocation()
				.distanceInKilometersTo(Config.MY_LOCATION);


		if (babysitter.mGroup > -1) { // 已有距離區間

		} else { // 沒有距離區間
			int d = (int) distance; // 轉為單純數字來比較就好
			LogUtils.LOGD("vic", "[ ] defaultDistance:" + defaultDistance + ", d: " + d + ", diff: " + (d - defaultDistance));

			if (mIsFirst && d >= defaultDistance) {
				defaultDistance = d;
				mIsFirst = false;
			}
			
			babysitter.mGroup = defaultDistance; // 給 距離區間 ex.2
			if (d >= defaultDistance ) { // 如果現在的距離 > 門檻值，做調整

				babysitter.mGroup = defaultDistance; // 更新 距離區間
				int step = 1;
				if (d - defaultDistance > 0 ) {
					step = d - defaultDistance; 
				}
				LogUtils.LOGD("vic", "[*] defaultDistance:" + defaultDistance + ", d: " + d + ", diff: " + (d - defaultDistance));
				defaultDistance = defaultDistance + step; // 門檻值往上調整 ex.4
				babysitter.mIsShow = true; // 可以show出來

			}
		}

		// if (mIsFirst) {
		// kmLine.setVisibility(View.INVISIBLE);
		// mIsFirst = false;
		// } else {
		// kmLine.setVisibility(View.VISIBLE);
		// }

		if (babysitter.mIsShow && Config.keyWord.isEmpty()) {
			// km.setTextColor(android.graphics.Color.RED);
			km.setText("  " + babysitter.mGroup + " KM  ");
			km.setVisibility(View.VISIBLE);
			kmLine.setBackgroundResource(R.drawable.line);
		} else {
			km.setVisibility(View.INVISIBLE);
			kmLine.setBackgroundResource(R.drawable.gray_line);
			km.setText("  2 KM  ");
			// km.setTextColor(android.graphics.Color.RED);
		}

		LogUtils.LOGD("vic", "babysitter.mGroup:" + babysitter.mGroup
				+ ", babysitter.mIsShwo:" + babysitter.mIsShow);

		// if (babysitter.getDistance() > 0.0f && babysitter.mIsShowDistance ) {
		// // 已存
		// km.setTextColor(android.graphics.Color.RED);
		// km.setText(" [" + babysitter.getDistance() + "] ");
		//
		// } else { //未存
		// DecimalFormat decimalFormat = new DecimalFormat("0.#");
		//
		// String show = "";
		// int d = (int) distance;
		// if (d >= defaultDistance ) {
		// babysitter.setDistance(defaultDistance);
		// km.setTextColor(android.graphics.Color.RED);
		// km.setText(" [" + babysitter.getDistance() + "] ");
		// //count++;
		// defaultDistance = defaultDistance + count;
		// } else {
		// //babysitter.setDistance(-2.0f);
		// }
		// }

		// babysitter.setDistance(distance);

		// if ( distance > defaultDistance) {
		// if (distance > 0.0f && distance < 1.0f) {
		// show = " [" + decimalFormat.format(distance * 1000) + "公尺]";
		// } else {
		// show = " [" + decimalFormat.format(distance) + "公里]";
		// }
		// show = " [" + defaultDistance + "km]";
		// km.setText(show);

		// LogUtils.LOGD("vic",
		// "defaultDistance:"+defaultDistance+", count:"+count+", total:" +
		// (defaultDistance ) + ", distance:" +babysitter.getDistance() + "d:" +
		// d);
		// if (d >= defaultDistance ) {

		// show = " [" + defaultDistance + "km]";
		// km.setText(show);

		// km.setTextColor(android.graphics.Color.RED);
		// count++;
		// defaultDistance = defaultDistance + count;
		// isColor = false;
		// } else {
		// km.setText("");
		// }

		// }
		// } else {
		// km.setText("");
		// }

		// babysitter.setDistance(distance);

		// BabysitterGridCard mCard = new BabysitterGridCard(getContext());
		// mCard.setBabysitter(babysitter);
		// mCard.init();

		// CardView mCardView;
		// mCardView = (CardView) view.findViewById(R.id.list_cardId);
		// if (mCardView != null) {
		// It is important to set recycle value for inner layout elements
		// mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(
		// mCardView.getCard(), mCard));

		// It is important to set recycle value for performance issue
		// mCardView.setRecycle(recycle);
		// mCardView.setCard(mCard);
		// }
		return rootView;
	}
	

	private boolean isFavoriteSitter(Babysitter babysitter) {
		
		for (BabysitterFavorite favorite : Config.favorites) {
			Babysitter favoriteSitter = favorite.getBabysitter();
			
			if (favoriteSitter.getObjectId().equals(babysitter.getObjectId())) {
				return true;
			}
		}
		
		return false;
		
	}

	private void getOldAvator(Babysitter sitter) {
		String websiteUrl = "http://cwisweb.sfaa.gov.tw/";
		String parseUrl = sitter.getImageUrl();
		if (parseUrl.equals("../img/photo_mother_no.jpg")) {
			mAvatar.setImageResource(R.drawable.profile);
		} else {
			imageLoader.displayImage(websiteUrl + parseUrl, mAvatar, Config.OPTIONS, null);
		}
	}



	/*
	 * @Override public int getViewTypeCount() { return 2; }
	 */
	private void showBabysitterPhone(final String[] phones) {
		DialogFragment newFragment = new ListDialogFragment(phones,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String phone = phones[which];
						makePhoneCall(phone);

					}
				});

		newFragment.show(
				((FragmentActivity) getContext()).getSupportFragmentManager(),
				"dialog");
	}

	private void makePhoneCall(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		getContext().startActivity(intent);
	}

	
	private static ParseQueryAdapter.QueryFactory<Babysitter> getQueryFactory(
			final Context context) {
		ParseQueryAdapter.QueryFactory<Babysitter> factory = new ParseQueryAdapter.QueryFactory<Babysitter>() {
			public ParseQuery<Babysitter> create() {

				
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(context);

				ParseQuery<Babysitter> query = Babysitter.getQuery();

					query.whereEqualTo("skillNumber", "154-056893");
					
				boolean mDayTime = sharedPreferences.getBoolean("mDayTime", false);
				if (mDayTime) {
					query.whereContains("babycareTime", "白天");
				}

				boolean mNightTime = sharedPreferences.getBoolean("mNightTime", false);
				if (mNightTime) {
					query.whereContains("babycareTime", "夜間");
				}

				boolean mHelfDay = sharedPreferences.getBoolean("mHalfDay", false);
				if (mHelfDay) {
					query.whereContains("babycareTime", "半天");
				}
				
				boolean mFullDay = sharedPreferences.getBoolean("mFullDay", false);
				if (mFullDay) {
					query.whereContains("babycareTime", "全天(24小時)");
				}
				
				boolean mPartTime = sharedPreferences.getBoolean("mPartTime", false);
				if (mPartTime) {
					query.whereContains("babycareTime", "臨時托育");
				}

				boolean mInHouse = sharedPreferences.getBoolean("mInHouse", false);
				if (mInHouse) {
					query.whereContains("babycareTime", "到宅服務");
				}

				// Kids
				boolean mKids0 = sharedPreferences.getBoolean("mKids0", false);
				if (mKids0) {
					query.whereMatches("babycareCount", "^.{0}$");
				}

				boolean mKids1 = sharedPreferences.getBoolean("mKids1", false);
				if (mKids1) {
					query.whereMatches("babycareCount", "^.{7}$");
				}

				boolean mKids2 = sharedPreferences.getBoolean("mKids2", false);
				if (mKids2) {
					query.whereMatches("babycareCount", "^.{15}$");
				}

				boolean mKids3 = sharedPreferences.getBoolean("mKids3", false);
				if (mKids3) {
					query.whereMatches("babycareCount", "^.{23}$");
				}

				boolean mOld40 = sharedPreferences.getBoolean("mOld40", false);
				if (mOld40) {
					query.whereMatches("age", "^[1-3][0-9]");
				}

				boolean mOld40_50 = sharedPreferences.getBoolean("mOld40_50", false);
				if (mOld40_50) {
					query.whereMatches("age", "^[4][0-9]");
				}
				
				boolean mOld50 = sharedPreferences.getBoolean("mOld50", false);
				if (mOld50) {
					query.whereMatches("age", "^[5][0-9]");
				}

				if (Config.keyWord.equals("")) {
					query.whereNear("location", Config.MY_LOCATION);
				} else {
					String keyword = Config.keyWord;
					keyword = keyword.replace("台", "臺");
					query.whereContains("address", keyword);
					query.orderByAscending("address");
				} 

				
//				if (!Config.keyWord.equals("")) {
//					finalQuery.whereNear("location", Config.MY_LOCATION);
//				}

				/*
				 * if (!DisplayUtils.hasNetwork(context)) { LogUtils.LOGD("vic",
				 * "babysitters fromLocalDatastore()");
				 * query.fromLocalDatastore(); }
				 */
				
				
				return query;
			}
		};
		return factory;
	}

	public void setExpandableObjectID(String objectID) {
		mExpandableObjectID = objectID;
	}

	/*
	 * @Override public View getNextPageView(View v, ViewGroup parent) { if (v
	 * == null) { v = View.inflate(getContext(), R.layout.adapter_next_page,
	 * null); }
	 * 
	 * return v; }
	 */
}
