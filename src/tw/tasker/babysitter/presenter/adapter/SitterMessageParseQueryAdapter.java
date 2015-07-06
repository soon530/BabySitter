package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.utils.DisplayUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class SitterMessageParseQueryAdapter extends
		ParseQueryAdapter<BabysitterFavorite> {

	private TextView mAge;
	private RatingBar mBabyCount;
	private Button mMatch;



	public SitterMessageParseQueryAdapter(Context context) {
		super(context, getQueryFactory(context));
	}

	@Override
	public View getItemView(final BabysitterFavorite favorite, View view,
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
		String changeText = DisplayUtils.getChangeText(babysitter.getBabycareTime());
		babycareTime.setText(changeText);

//		TextView babysitterNumber = (TextView) rootView
//				.findViewById(R.id.babysitterNumber);

		mAge = (TextView) rootView.findViewById(R.id.age);
		mAge.setText("("+babysitter.getAge()+")");
		
		mMatch = (Button) rootView.findViewById(R.id.match);
		mMatch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//doParentConfirm(favorite.getObjectId());
			}
		});

//		mBabyCount = (RatingBar) rootView.findViewById(R.id.babycareCount);
//		int babyCount = getBabyCount(babysitter.getBabycareCount());
//		mBabyCount.setRating(babyCount);

		// 加入距離計算公式
//        float distance = (float) babysitter.getLocation().distanceInKilometersTo(Config.MY_LOCATION);
//        babysitter.setDistance(distance);



		return rootView;
	}
	
	private void doParentConfirm(final String objectId) {
		ParseQuery<BabysitterFavorite> query = BabysitterFavorite.getQuery();
		query.whereEqualTo("BabyDiary", objectId);
		
		query.getInBackground(objectId, new GetCallback<BabysitterFavorite>() {

			@Override
			public void done(BabysitterFavorite favorite, ParseException e) {
				//babyFavorite.setBabyRecord(babyRecord);
				//babyFavorite.saveInBackground();
			}
		});

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
