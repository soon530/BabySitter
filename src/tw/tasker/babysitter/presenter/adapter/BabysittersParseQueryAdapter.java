package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.fragment.ListDialogFragment;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysittersParseQueryAdapter extends ParseQueryAdapter<Babysitter> {
	private int defaultDistance = 2;
	private int count = 2;
	private boolean isColor = true;
	private boolean mIsFirst = true;

	public BabysittersParseQueryAdapter(Context context, int position) {
		super(context, getQueryFactory(context, position));
	}

	@Override
	public View getItemView(final Babysitter babysitter, View view, ViewGroup parent) {
		//boolean recycle = false;
		View rootView;

		if (view == null) {
			//recycle = false;
			//rootView = View.inflate(getContext(), R.layout.list_item_sitter, null);
			LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView = mInflater.inflate(R.layout.list_item_sitter, parent, false);
		} else {
				rootView = view;
			//recycle = true;
		}
		
		TextView name = (TextView) rootView.findViewById(R.id.name);
		TextView address = (TextView) rootView.findViewById(R.id.address);
		TextView babycareTime = (TextView) rootView.findViewById(R.id.babycare_time);

		TextView babysitterNumber = (TextView) rootView.findViewById(R.id.babysitterNumber);
		TextView education = (TextView) rootView.findViewById(R.id.education);
		TextView communityName = (TextView) rootView.findViewById(R.id.communityName);

		SpannableString content = new SpannableString(babysitter.getCommunityAddress());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		communityName.setText(content);
		
		Button contact = (Button) rootView.findViewById(R.id.contact);
		contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String[] phones = babysitter.getTel().replace("(日):", "").replace("手機: ", "").split(" ");
				LogUtils.LOGD("vic", "phones" + babysitter.getTel());
				for (String phone : phones) {
					LogUtils.LOGD("vic", "phone" + phone);
				}
				
				//if (phones.length == 1) {
					//mPresenter.makePhoneCall(phones[0]);
				//} else {
					showBabysitterPhone(phones);
				//}
				
			}
		});
		
		name.setText(babysitter.getName());
		address.setText(babysitter.getAddress());
		babycareTime.setText(babysitter.getBabycareTime());
		
		babysitterNumber.setText("保母編號：" + babysitter.getBabysitterNumber());
		education.setText("教育程度：" + babysitter.getEducation());
        
		TextView km = (TextView) rootView.findViewById(R.id.km);
		ImageView kmLine = (ImageView) rootView.findViewById(R.id.km_line);
		
		float distance = (float) babysitter.getLocation().distanceInKilometersTo(Config.MY_LOCATION);
		
		LogUtils.LOGD("vic", "defaultDistance:"+defaultDistance+", distance%2:" + (distance%2) + ", distance/2:"+ (distance/2) + " ["+distance+"]");

		if (babysitter.mGroup > -1) { // 已有距離區間
		
		
		} else { // 沒有距離區間
			babysitter.mGroup = defaultDistance; // 給 距離區間 ex.2
			
			int d = (int) distance; // 轉為單純數字來比較就好
			if (d >= defaultDistance) { // 如果現在的距離 > 門檻值，做調整
				babysitter.mGroup = defaultDistance; // 更新 距離區間
				defaultDistance = defaultDistance + 1; //門檻值往上調整 ex.4
				babysitter.mIsShow = true; // 可以show出來 
			}
		}
		
//		if (mIsFirst) {
//			kmLine.setVisibility(View.INVISIBLE);
//			mIsFirst = false;
//		} else {
//			kmLine.setVisibility(View.VISIBLE);
//		}
		
		if (babysitter.mIsShow) {
			//km.setTextColor(android.graphics.Color.RED);
			km.setText("  " + babysitter.mGroup + " KM  ");
			km.setVisibility(View.VISIBLE);
			kmLine.setBackgroundResource(R.drawable.line);
		} else {
			km.setVisibility(View.INVISIBLE);
			kmLine.setBackgroundResource(R.drawable.gray_line);
			km.setText("  2 KM  ");
			//km.setTextColor(android.graphics.Color.RED);
		}
		
		LogUtils.LOGD("vic", "babysitter.mGroup:" + babysitter.mGroup + ", babysitter.mIsShwo:" + babysitter.mIsShow);
		
//		if (babysitter.getDistance() > 0.0f && babysitter.mIsShowDistance ) { // 已存
//			km.setTextColor(android.graphics.Color.RED);
//			km.setText(" [" + babysitter.getDistance() + "] ");
//
//		} else { //未存
//			DecimalFormat decimalFormat = new DecimalFormat("0.#");
//			
//			String show = "";
//			int d = (int) distance;
//			if (d >= defaultDistance ) {
//				babysitter.setDistance(defaultDistance);
//				km.setTextColor(android.graphics.Color.RED);
//				km.setText(" [" + babysitter.getDistance() + "] ");
//				//count++;
//				defaultDistance = defaultDistance + count;
//			} else {
//				//babysitter.setDistance(-2.0f);
//			}
//		}
		

		//babysitter.setDistance(distance);

		//if ( distance > defaultDistance) {
			//if (distance > 0.0f && distance < 1.0f) {
			//	show = " [" + decimalFormat.format(distance * 1000) + "公尺]";
			//} else {
				//show = " [" + decimalFormat.format(distance) + "公里]";
			//}		
			//show = " [" + defaultDistance + "km]";
			//km.setText(show);
			
			//LogUtils.LOGD("vic", "defaultDistance:"+defaultDistance+", count:"+count+", total:" + (defaultDistance ) + ", distance:" +babysitter.getDistance() + "d:" + d);
			//if (d >= defaultDistance ) {

				//show = " [" + defaultDistance + "km]";
				//km.setText(show);

				//km.setTextColor(android.graphics.Color.RED);
				//count++;
				//defaultDistance = defaultDistance + count;
				//isColor = false;
			//} else {
				//km.setText("");
			//}
			
			
		//}	
		//} else {
			//km.setText("");
		//}
		
		//babysitter.setDistance(distance);
        
		//BabysitterGridCard mCard = new BabysitterGridCard(getContext());
		//mCard.setBabysitter(babysitter);
		//mCard.init();

		//CardView mCardView;
		//mCardView = (CardView) view.findViewById(R.id.list_cardId);
		//if (mCardView != null) {
			// It is important to set recycle value for inner layout elements
			//mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(
					//mCardView.getCard(), mCard));

			// It is important to set recycle value for performance issue
			//mCardView.setRecycle(recycle);
			//mCardView.setCard(mCard);
		//}
		return rootView;
	}
	
/*	@Override
	public int getViewTypeCount() {
		return 2;
	}
*/
	private void showBabysitterPhone(final String[] phones) {
		DialogFragment newFragment = new ListDialogFragment(phones, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String phone = phones[which];
				//mPresenter.makePhoneCall(phone);
				
			}
		});
		
		newFragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "dialog");		
	}

	
	private static ParseQueryAdapter.QueryFactory<Babysitter> getQueryFactory(final Context context, final int position) {
		ParseQueryAdapter.QueryFactory<Babysitter> factory = new ParseQueryAdapter.QueryFactory<Babysitter>() {
			public ParseQuery<Babysitter> create() {

				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

				
				ParseQuery<Babysitter> query = Babysitter.getQuery();
				
				boolean mDay = sharedPreferences.getBoolean("mDay", false);
				if (mDay) {
					query.whereContains("babycareTime", "白天");
				}

				boolean mNight = sharedPreferences.getBoolean("mNight", false);
				if (mNight) {
					query.whereContains("babycareTime", "半天");
				}

				boolean mTemp = sharedPreferences.getBoolean("mTemp", false);
				if (mTemp) {
					query.whereContains("babycareTime", "臨時");
				}

				if (!Config.keyWord.equals("")) {
					String keyword = Config.keyWord;
					keyword = keyword.replace("台", "臺");
					query.whereContains("address", keyword);
				} else {
					query.whereNear("location", Config.MY_LOCATION);
				}
				

//				switch (position) {
//				case 0:
//					query.whereNear("location", Config.MY_LOCATION);
//					break;
//				case 1: //臨時保母
//					query.whereContains("babycareTime", "臨時");
//					query.whereNear("location", Config.MY_LOCATION);
//					break;
//				case 2:
//					String keyword = Config.keyWord;
//					keyword = keyword.replace("台", "臺");
//					query.whereContains("address", keyword);
//					break;
//				case 3:
//					query.orderByDescending("updatedAt");
//				default:
//					break;
//				}
				
				
/*				if (!DisplayUtils.hasNetwork(context)) {
					LogUtils.LOGD("vic", "babysitters fromLocalDatastore()");
					query.fromLocalDatastore();
				}
*/				return query;
			}
		};
		return factory;
	}

/*	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}

		return v;
	}
*/
}
