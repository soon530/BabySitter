package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class BabysittersParseQueryAdapter extends ParseQueryAdapter<Babysitter> 
	 {
	private int defaultDistance = 2;
	private int count = 2;
	private boolean isColor = true;
	private boolean mIsFirst = true;
	private RatingBar mBabyCount;
	private String mExpandableObjectID = "";
	private CircleImageView mAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private TextView mAge;

    public SitterListClickHandler mSitterListClickHandler;
	private LinearLayout mExpandable;
	private LinearLayout mExpandableToggle;
	private ImageView mArrow;
	private TextView mName;
	private TextView mAddress;
	private TextView mBabycareTime;
	private TextView mBabysitterNumber;
	private TextView mEducation;
	private TextView mCommunityName;
	private TextView mKm;
	private ImageView mKmLine;
	private Button mContact;
	private TextView mDetail;
    public static interface SitterListClickHandler {
    	public void onContactClick(View v, Babysitter babysitter);
    	public void onDetailClick();
    }

	public BabysittersParseQueryAdapter(Context context, SitterListClickHandler sitterListClickHandler) {
		super(context, getQueryFactory(context));
		mSitterListClickHandler = sitterListClickHandler;
	}

	@Override
	public View getItemView(final Babysitter babysitter, View view,
			ViewGroup parent) {
		View rootView;

		if (view == null) {
			LayoutInflater mInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView = mInflater.inflate(R.layout.list_item_sitter, parent,
					false);
		} else {
			rootView = view;
		}
		
		initView(rootView);
		initData(babysitter);
		initListener(babysitter);
		
		return rootView;
	}
	
	private void initView(View rootView) {
		mAvatar = (CircleImageView) rootView.findViewById(R.id.avatar);
		mName = (TextView) rootView.findViewById(R.id.name);
		mBabysitterNumber = (TextView) rootView.findViewById(R.id.babysitterNumber);
		mAge = (TextView) rootView.findViewById(R.id.age);
		mEducation = (TextView) rootView.findViewById(R.id.education);
		mAddress = (TextView) rootView.findViewById(R.id.address);
		mBabycareTime = (TextView) rootView.findViewById(R.id.babycare_time);
		mBabyCount = (RatingBar) rootView.findViewById(R.id.babycareCount);
		mCommunityName = (TextView) rootView.findViewById(R.id.communityName);

		mExpandable = (LinearLayout) rootView.findViewById(R.id.expandable);
		mExpandableToggle = (LinearLayout) rootView.findViewById(R.id.expandable_toggle_button);
		mArrow = (ImageView) rootView.findViewById(R.id.arrow);
		
		mKm = (TextView) rootView.findViewById(R.id.km);
		mKmLine = (ImageView) rootView.findViewById(R.id.km_line);
		
		mContact = (Button) rootView.findViewById(R.id.contact);
		mDetail = (TextView) rootView.findViewById(R.id.detail);

	}

	private void initData(Babysitter babysitter) {
		loadOldAvator(babysitter);
		mName.setText(babysitter.getName());
		mBabysitterNumber.setText("保母證號：" + babysitter.getSkillNumber());
		mAge.setText("("+babysitter.getAge()+")");
		mEducation.setText("教育程度：" + babysitter.getEducation());
		mAddress.setText(babysitter.getAddress());

		String changeText = DisplayUtils.getChangeText(babysitter.getBabycareTime());	
		mBabycareTime.setText(changeText);

		int babyCount = DisplayUtils.getBabyCount(babysitter.getBabycareCount());
		mBabyCount.setRating(babyCount);
		
		SpannableString content = new SpannableString(babysitter.getCommunityName());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		mCommunityName.setText(content);

		initArrowRotation(babysitter.getObjectId());
		initContactStatus(babysitter);
		getKmPosition(babysitter);
		initKmPosition(babysitter);
	}

	private void initArrowRotation(String objectId) {
		if (mExpandableObjectID.equals(objectId)) {
			mArrow.setRotation(180);
		} else {
			mArrow.setRotation(0);
		}
	}
	
	private void initContactStatus(Babysitter babysitter) {
		if (isFavoriteSitter(babysitter)) {
			mContact.setEnabled(false);
			mContact.setText(R.string.contact_sent);
		} else {
			mContact.setEnabled(true);
			mContact.setText(R.string.contact);
		}
		
	}
	
	private void getKmPosition(Babysitter babysitter) {
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
		
	}

	private void initKmPosition(Babysitter babysitter) {
		if (babysitter.mIsShow && Config.keyWord.isEmpty()) {
			// km.setTextColor(android.graphics.Color.RED);
			mKm.setText("  " + babysitter.mGroup + " KM  ");
			mKm.setVisibility(View.VISIBLE);
			mKmLine.setBackgroundResource(R.drawable.line);
		} else {
			mKm.setVisibility(View.INVISIBLE);
			mKmLine.setBackgroundResource(R.drawable.gray_line);
			mKm.setText("  2 KM  ");
			// km.setTextColor(android.graphics.Color.RED);
		}

		LogUtils.LOGD("vic", "babysitter.mGroup:" + babysitter.mGroup
				+ ", babysitter.mIsShwo:" + babysitter.mIsShow);
		
	}

	private void initListener(final Babysitter babysitter) {

		mContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSitterListClickHandler.onContactClick(v, babysitter);
			}
		});
		
		mDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSitterListClickHandler.onDetailClick();
				Config.sitterInfo = babysitter;
			}
		});

//		expandableToggle.setOnTouchListener(new OnTouchListener() {
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//
//			
//			if (clickDuration < MAX_CLICK_DURATION) {
//				if (expandable.getVisibility() == View.VISIBLE) {
//					arrow.animate().rotation(0).start();
//					mIsExpandableSitter = "";
//				} else {
//					arrow.animate().rotation(180).start();
//					mIsExpandableSitter = babysitter.getObjectId();
//				}
//			}
//			return false;
//		}
//	});

	}



	// TODO the system will be crashed sometimes.
	private boolean isFavoriteSitter(Babysitter babysitter) {

		if (AccountChecker.isNull(Config.favorites))
			return false;
			
		for (BabysitterFavorite favorite : Config.favorites) {
			Babysitter favoriteSitter = favorite.getBabysitter();
			
			if (favoriteSitter.getObjectId().equals(babysitter.getObjectId())) {
				return true;
			}
		}
		
		return false;
	}

	private void loadOldAvator(Babysitter sitter) {
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

}
