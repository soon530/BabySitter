package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.fragment.HomeFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

public class HomeActivity extends ActionBarActivity {
	private static final String[] mSearchCondition = new String[] { "附近保母",
			"寶寶日記","寶寶收藏", "保母收藏", "登出" };
	//,"托育時段", "收托對象", "收托年齡", "可接受托育人數"
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			
			HomeFragment fragment = new HomeFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ListView mBabysitterSearchCondition;
		private TextView mUserInfo;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			
			
			mUserInfo = (TextView)rootView.findViewById(R.id.user_info);

			ParseUser user = ParseUser.getCurrentUser();
			
			mUserInfo.setText("使用者資訊(" + user.getObjectId() + ")："+ user.getUsername() );
			
			mBabysitterSearchCondition = (ListView) rootView
					.findViewById(R.id.babysitter_search_condition);
			mBabysitterSearchCondition.setAdapter(new ArrayAdapter<String>(
					getActivity().getApplicationContext(),
					android.R.layout.simple_list_item_1, mSearchCondition));

			mBabysitterSearchCondition
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							
							Intent intent;
							switch (position) {
							case 0:
								intent = new Intent();
								intent.setClass(getActivity(),
										BabysittersActivity.class);
								startActivity(intent);
								break;
								
							case 1:
								intent = new Intent();
								intent.setClass(getActivity(), BabyDiaryActivity.class);
								startActivity(intent);
								break;
								
							case 2:
								intent = new Intent();
								intent.setClass(getActivity(), FavoriteBabyActivity.class);
								startActivity(intent);
								break;

							case 3:
								intent = new Intent();
								intent.setClass(getActivity(), FavoriteBabysitterActivity.class);
								startActivity(intent);
								
								break;

							case 4:
						        // Call the Parse log out method
						        ParseUser.logOut();
						        intent = new Intent();
						        // Start and intent for the dispatch activity
						        intent.setClass(getActivity(), DispatchActivity.class);
						        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						        startActivity(intent);
								break;
								
							default:
								break;
							}
						}
					});

			return rootView;
		}
	}

}
