package tw.tasker.babysitter.presenter.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.LogUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SendCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParentMessageParseQueryAdapter extends
		ParseQueryAdapter<BabysitterFavorite> {

	private TextView mAge;
	private RatingBar mBabyCount;
	private CircleImageView mAvatar;


	public ParentMessageParseQueryAdapter(Context context) {
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

		final UserInfo userInfo = favorite.getUserInfo();

		String status = "";
		if (favorite.getIsSitterConfirm()) {
			status = "";
		} else {
			status = "*";
		}
		
		TextView name = (TextView) rootView.findViewById(R.id.name);
		name.setText(userInfo.getName() + " " + status);

		TextView address = (TextView) rootView.findViewById(R.id.address);
		address.setText(userInfo.getAddress());
		mAvatar = (CircleImageView) rootView.findViewById(R.id.avatar);
		mAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateSitterConfirm(favorite);
				pushTextToParent(userInfo);
			}
		});
		
//		TextView babycareTime = (TextView) rootView.findViewById(R.id.babycare_time);
//		String changeText = getChangeText(userInfo.getBabycareTime());
//		babycareTime.setText(changeText);

//		TextView babysitterNumber = (TextView) rootView
//				.findViewById(R.id.babysitterNumber);

//		mAge = (TextView) rootView.findViewById(R.id.age);
//		mAge.setText("("+babysitter.getAge()+")");

//		mBabyCount = (RatingBar) rootView.findViewById(R.id.babycareCount);
//		int babyCount = getBabyCount(babysitter.getBabycareCount());
//		mBabyCount.setRating(babyCount);

		// 加入距離計算公式
//        float distance = (float) babysitter.getLocation().distanceInKilometersTo(Config.MY_LOCATION);
//        babysitter.setDistance(distance);



		return rootView;
	}

	protected void updateSitterConfirm(BabysitterFavorite favorite) {
		ParseQuery<BabysitterFavorite> query = BabysitterFavorite.getQuery();
		query.getInBackground(favorite.getObjectId(), new GetCallback<BabysitterFavorite>() {
			
			@Override
			public void done(BabysitterFavorite favorite, ParseException exception) {
				if (favorite != null) {
					favorite.setIsSitterConfirm(true);
					favorite.saveInBackground();
				}
			}
		});
	}

	private void pushTextToParent(UserInfo userInfo) {
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		LogUtils.LOGD("vic", "push obj:" + userInfo.getUser().getObjectId());
		//ParseObject obj = ParseObject.createWithoutData("user", "KMyQfnc5k3");
		pushQuery.whereEqualTo("user", userInfo.getUser());
		
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

	private JSONObject getJSONDataMessageForIntent() {
		try
	    {
	        JSONObject data = new JSONObject();
	        data.put("alert", "保母["+ParseUser.getCurrentUser().getUsername()+"]，可以幫你帶小孩~");
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
	
	private static ParseQueryAdapter.QueryFactory<BabysitterFavorite> getQueryFactory(final Context context) {
		ParseQueryAdapter.QueryFactory<BabysitterFavorite> factory = new ParseQueryAdapter.QueryFactory<BabysitterFavorite>() {
			public ParseQuery<BabysitterFavorite> create() {
				ParseQuery<BabysitterFavorite> query = BabysitterFavorite
						.getQuery();
				query.include("Babysitter");
				query.include("UserInfo");
				query.whereEqualTo("Babysitter", Config.sitterInfo);
				query.whereEqualTo("isParentConfirm", true);
				//query.whereEqualTo("isSitterConfirm", true);
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
