package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.fragment.HomeFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends ActionBarActivity {
	private static final String[] mSearchCondition = new String[] { "附近保母",
			"寶寶日記","寶寶收藏", "保母收藏", "登出" };
	//,"托育時段", "收托對象", "收托年齡", "可接受托育人數"
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
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
