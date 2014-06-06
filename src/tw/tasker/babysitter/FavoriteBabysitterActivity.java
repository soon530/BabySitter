package tw.tasker.babysitter;

import java.util.List;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.FavoriteBabysitter;
import tw.tasker.babysitter.presenter.adapter.FavoriteBabysitterParseQueryAdapter;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.impl.BabysitterDetailActivity;
import tw.tasker.babysitter.view.impl.FavoriteBabyActivity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnItemClickListener, OnQueryLoadListener<FavoriteBabysitter> {
		private ParseQueryAdapter<FavoriteBabysitter> mAdapter;
		private GridView mList;
		private TextView mEmpty;

		public PlaceholderFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.favorite_babysitter, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}
		

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			
			switch (id) {
			case R.id.refresh:
				mAdapter.loadObjects();
				break;

			case R.id.favorite_baby:
				Intent intent = new Intent();
				intent.setClass(getActivity(), FavoriteBabyActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
			
			return super.onOptionsItemSelected(item);
		}

		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_baby_list, container, false);

			mList = (GridView) rootView.findViewById(R.id.list);
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
			//mAdapter.setPaginationEnabled(false);
			mAdapter.setObjectsPerPage(6);
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
