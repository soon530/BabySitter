package tw.tasker.babysitter.view.activity;

import java.util.ArrayList;
import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.view.fragment.HomeFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.parse.ParseGeoPoint;

public class HomeActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {
			
			HomeFragment fragment = new HomeFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
		
		// 後續看要不要放在ActionBar之類的
		//mUserInfo.setText("使用者資訊(" + user.getObjectId() + ")："+ user.getUsername() );

	}

	@Override
	protected void onResume() {
		super.onResume();
		initLocation();
	}
	
	private void initLocation() {
		// 初始化現在的位置
		//if (Config.MY_LOCATION == null) {
			MyLocation myLocation = new MyLocation(this, new GetLocation() {
				
				@Override
				public void done(ParseGeoPoint parseGeoPoint) {
					Config.MY_LOCATION= parseGeoPoint;
					//Config.MY_LOCATION = Config.MY_TEST_LOCATION;
					//LogUtils.LOGD("vic", "get my location at ("+parseGeoPoint.getLatitude()+","+parseGeoPoint.getLongitude()+")");
				}

			});
		//}
	}
}
