package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.GetLocation;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.view.fragment.NewHomeFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HomeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {

			Fragment fragment = new NewHomeFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
		//getActionBar().setDisplayShowHomeEnabled(false);
		ParseAnalytics.trackAppOpened(getIntent());
		// 後續看要不要放在ActionBar之類的
		// mUserInfo.setText("使用者資訊(" + user.getObjectId() + ")："+
		// user.getUsername() );

	}

	@Override
	protected void onResume() {
		super.onResume();
		initLocation();
	}

	private void initLocation() {
		// 初始化現在的位置
		// if (Config.MY_LOCATION == null) {
		MyLocation myLocation = new MyLocation(this, new GetLocation() {

			@Override
			public void done(ParseGeoPoint parseGeoPoint) {
				Config.MY_LOCATION = parseGeoPoint;
				updateMyLocaton();
				// Config.MY_LOCATION = Config.MY_TEST_LOCATION;
				// LogUtils.LOGD("vic",
				// "get my location at ("+parseGeoPoint.getLatitude()+","+parseGeoPoint.getLongitude()+")");
			}

		});
		// }
	}

	private void updateMyLocaton() {
		if (ParseUser.getCurrentUser() != null) {
			hasUserInfo();
		}
	}
	
	private void hasUserInfo() {
		ParseQuery<UserInfo> userInfoQuery = UserInfo.getQuery();
		userInfoQuery.whereEqualTo("user", ParseUser.getCurrentUser());
		userInfoQuery.getFirstInBackground(new GetCallback<UserInfo>() {
			
			@Override
			public void done(UserInfo userInfo, ParseException e) {
				if (userInfo == null ) {
					addUserInfo();
				} else {
					updateUserInfo(userInfo);
				}
			}
		});
	}
	
	private void addUserInfo() {
		LogUtils.LOGD("vic", "addUserInfo");

		UserInfo userInfo = new UserInfo();
		userInfo.setLocation(Config.MY_LOCATION);
		userInfo.setUser(ParseUser.getCurrentUser());

		userInfo.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
				} else {
					LOGD("vic", e.getMessage());
				}
			}
		});
	}
	
	private void updateUserInfo(UserInfo userInfo) {
		LogUtils.LOGD("vic", "updateUserInfo");

		userInfo.setLocation(Config.MY_LOCATION);
		userInfo.saveInBackground();
	}
}
