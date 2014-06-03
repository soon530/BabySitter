package tw.tasker.babysitter;

import java.util.List;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.FavoriteBabysitter;
import tw.tasker.babysitter.presenter.adapter.FavoriteBabysitterParseQueryAdapter;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.impl.BabysitterDetailActivity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class FavoriteBabysitterActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);

		setContentView(R.layout.activity_favorite_babysitter);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorite_babysitter, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnItemClickListener, OnQueryLoadListener<FavoriteBabysitter> {
		private ParseQueryAdapter<FavoriteBabysitter> mAdapter;
		private ListView mList;
		private TextView mEmpty;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_favorite_babysitter, container, false);

			mList = (ListView) rootView.findViewById(R.id.list);
			mList.setOnItemClickListener(this);
			mEmpty = (TextView) rootView.findViewById(R.id.empty);
			mList.setEmptyView(mEmpty);

			return rootView;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			doListQuery();
		}
		
		public void doListQuery() {
			mAdapter = new FavoriteBabysitterParseQueryAdapter(getActivity());
			mAdapter.setAutoload(false);
			mAdapter.setPaginationEnabled(false);
			mAdapter.addOnQueryLoadListener(this);
			mList.setAdapter(mAdapter);
			mAdapter.loadObjects();
		}

		@Override
		public void onLoading() {
			ProgressBarUtils.show(getActivity());
		}

		@Override
		public void onLoaded(List<FavoriteBabysitter> arg0, Exception arg1) {
			ProgressBarUtils.hide(getActivity());
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FavoriteBabysitter favorite = mAdapter.getItem(position);
			Babysitter babysitter = favorite.getBabysitter();
			seeBabysitterDetail(babysitter.getObjectId());
		}

		public void seeBabysitterDetail(String babysitterObjectId) {
			Bundle bundle = new Bundle();
			bundle.putString(Config.BABYSITTER_OBJECT_ID, babysitterObjectId);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(getActivity(), BabysitterDetailActivity.class);
			startActivity(intent);
		}
	}

}
