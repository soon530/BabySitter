package tw.tasker.babysitter.view.impl;

import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.presenter.adapter.BabyDiaryParseQueryAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabyDiaryActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** Enabling Progress bar for this activity */
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_baby_diary);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baby_diary, menu);
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
	public static class PlaceholderFragment extends Fragment implements
			OnItemClickListener, OnQueryLoadListener<Baby> {
		private ParseQueryAdapter<Baby> mAdapter;
		private ListView mList;
		private TextView mEmpty;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baby_list,
					container, false);
			mList = (ListView) rootView.findViewById(R.id.list);
			mList.setOnItemClickListener(this);
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
			mAdapter = new BabyDiaryParseQueryAdapter(getActivity());
			mAdapter.setAutoload(false);
			mAdapter.setPaginationEnabled(false);
			mList.setAdapter(mAdapter);
			mAdapter.loadObjects();
			mAdapter.addOnQueryLoadListener(this);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Baby baby = mAdapter.getItem(position);
			if (baby.getIsPublic()) {

				seeBabyDetail(baby.getObjectId());
			}
		}

		@Override
		public void onLoaded(List<Baby> arg0, Exception arg1) {
			getActivity().setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onLoading() {
			getActivity().setProgressBarIndeterminateVisibility(true);
		}
	}
}
