package tw.tasker.babysitter.view.impl;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.drawable;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.R.menu;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.Favorite;
import tw.tasker.babysitter.presenter.adapter.FavoriteBabyParseQueryAdapter;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.QueryFactory;
import com.parse.ParseUser;

public class FavoriteBabyActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_list);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baby_list, menu);
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
		private ParseQueryAdapter<Favorite> mAdapter;
		private ListView mList;
		private TextView mEmpty;
		
		
		public PlaceholderFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baby_list,
					container, false);

			mList = (ListView) rootView.findViewById(R.id.list);
			
			mList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Favorite favorite = mAdapter.getItem(position);
					Baby baby = favorite.getBaby();
					
					seeBabyDetail(baby.getObjectId());
				}
			
			});
			
			mEmpty = (TextView) rootView.findViewById(R.id.empty);
			mList.setEmptyView(mEmpty);
			
			return rootView;
		}

		public void seeBabyDetail(String babyObjectId) {
			Bundle bundle = new Bundle();
			bundle.putString("objectId", babyObjectId);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(getActivity(), BabyDetailActivity.class);
			startActivity(intent);
		}

		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			doListQuery();

		}

		public void doListQuery() {



			mAdapter = new FavoriteBabyParseQueryAdapter(getActivity());

			// Disable automatic loading when the list_item_babysitter_comment
			// is
			// attached to a view.
			mAdapter.setAutoload(false);

			// Disable pagination, we'll manage the query limit ourselves
			mAdapter.setPaginationEnabled(false);

			mList.setAdapter(mAdapter);

			mAdapter.loadObjects();
		}



	}

}
