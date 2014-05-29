package tw.tasker.babysitter.view.impl;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.presenter.adapter.BabyDiaryParseQueryAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
			
			Bundle arguments = new Bundle();
			arguments.putString(
					Config.BABYSITTER_OBJECT_ID,
					getIntent().getStringExtra(
							Config.BABYSITTER_OBJECT_ID));
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnItemClickListener, OnQueryLoadListener<Baby> {
		private ParseQueryAdapter<Baby> mAdapter;
		private ListView mList;
		private TextView mEmpty;
		private String mBabysitterObjectId;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
				mBabysitterObjectId = getArguments()
						.getString(Config.BABYSITTER_OBJECT_ID);
			}
			
			if (mBabysitterObjectId != null) {
				setHasOptionsMenu(true);
			}
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.baby_add_list, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				Bundle bundle = new Bundle();
				bundle.putString("objectId", mBabysitterObjectId);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(getActivity(), BabyAddActivity.class);
				startActivity(intent);
				
				return true;
			}

			return super.onOptionsItemSelected(item);
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
			mAdapter = new BabyDiaryParseQueryAdapter(getActivity(), mBabysitterObjectId);
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
		public void onLoading() {
			getActivity().setProgressBarIndeterminateVisibility(true);
		}

		@Override
		public void onLoaded(List<Baby> arg0, Exception arg1) {
			getActivity().setProgressBarIndeterminateVisibility(false);
		}

	}
}
