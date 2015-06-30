package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class SitterMessageParseQueryAdapter extends
		ParseQueryAdapter<BabysitterFavorite> {

	private TextView mAge;
	private RatingBar mBabyCount;



	public SitterMessageParseQueryAdapter(Context context) {
		super(context, getQueryFactory(context));
	}

	@Override
	public View getItemView(BabysitterFavorite favorite, View view,
			ViewGroup parent) {


		View rootView;
		if (view == null) {
			rootView = View.inflate(getContext(), R.layout.list_item_sitter_message, null);
		} else {
			rootView = view;
		}

		Babysitter babysitter = favorite.getBabysitter();
		
		String status = "";
		if (favorite.getIsParentConfirm()) {
			status = "";
		} else {
			status = "*";
		}
		
		
		TextView name = (TextView) rootView.findViewById(R.id.name);
		name.setText(babysitter.getName() + " " + status);

//		TextView address = (TextView) rootView.findViewById(R.id.address);
//		address.setText(babysitter.getAddress());

		TextView babycareTime = (TextView) rootView.findViewById(R.id.babycare_time);
		String changeText = getChangeText(babysitter.getBabycareTime());
		babycareTime.setText(changeText);

//		TextView babysitterNumber = (TextView) rootView
//				.findViewById(R.id.babysitterNumber);

		mAge = (TextView) rootView.findViewById(R.id.age);
		mAge.setText("("+babysitter.getAge()+")");

//		mBabyCount = (RatingBar) rootView.findViewById(R.id.babycareCount);
//		int babyCount = getBabyCount(babysitter.getBabycareCount());
//		mBabyCount.setRating(babyCount);

		// 加入距離計算公式
//        float distance = (float) babysitter.getLocation().distanceInKilometersTo(Config.MY_LOCATION);
//        babysitter.setDistance(distance);



		return rootView;
	}

	private String getChangeText(String babycareTime) {
		String changeText = "";
		changeText = babycareTime
				.replace("白天", "日托")
				.replace("夜間", "夜托")
				.replace("全天(24小時)", "全日")
				.replace("半天", "半日")
				.replace("到宅服務", "到府服務");
		return changeText;
	}

	private int getBabyCount(String babycareCount) {
		
		int count;
		if (babycareCount.isEmpty()) {
			count = 0;
		} else {
			String[] babies = babycareCount.split(" ");
			count = babies.length;
		}
		
		return count;
	}


	private static ParseQueryAdapter.QueryFactory<BabysitterFavorite> getQueryFactory(final Context context) {
		ParseQueryAdapter.QueryFactory<BabysitterFavorite> factory = new ParseQueryAdapter.QueryFactory<BabysitterFavorite>() {
			public ParseQuery<BabysitterFavorite> create() {
				ParseQuery<BabysitterFavorite> query = BabysitterFavorite
						.getQuery();
				//query.include("user");
				query.include("Babysitter");
				query.include("UserInfo");
				query.whereEqualTo("UserInfo", Config.userInfo);
				//query.whereEqualTo("isParentConfirm", true);
				query.whereEqualTo("isSitterConfirm", true);
				query.orderByDescending("updatedAt");
				
/*				if (!DisplayUtils.hasNetwork(context)) {
					query.fromLocalDatastore();
				}
*/
				return query;
			}
		};
		return factory;
	}

}
