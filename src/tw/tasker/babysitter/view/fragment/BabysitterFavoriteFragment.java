package tw.tasker.babysitter.view.fragment;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.presenter.adapter.BabysitterFavoriteParseQueryAdapter;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.BabysitterActivity;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class BabysitterFavoriteFragment extends Fragment implements
		OnItemClickListener, OnQueryLoadListener<BabysitterFavorite>, OnRefreshListener {
	private ParseQueryAdapter<BabysitterFavorite> mAdapter;
	private GridView mList;
	private TextView mEmpty;
	private PullToRefreshLayout mPullToRefreshLayout;

	public static Fragment newInstance(int position) {
		BabysitterFavoriteFragment fragment = new BabysitterFavoriteFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_grid,
				container, false);

		mList = (GridView) rootView.findViewById(R.id.grid);
		mList.setOnItemClickListener(this);

		// Retrieve the PullToRefreshLayout from the content view
		mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.carddemo_extra_ptr_layout);

		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		doListQuery();
	}

	public void doListQuery() {
		mAdapter = new BabysitterFavoriteParseQueryAdapter(getActivity());
		mAdapter.setAutoload(false);
		// mAdapter.setPaginationEnabled(false);
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
	public void onLoaded(List<BabysitterFavorite> arg0, Exception arg1) {
		ProgressBarUtils.hide(getActivity());
		mPullToRefreshLayout.setRefreshComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BabysitterFavorite favorite = mAdapter.getItem(position);
		Babysitter babysitter = favorite.getBabysitter();
		seeBabysitterDetail(babysitter.getObjectId());
	}

	public void seeBabysitterDetail(String babysitterObjectId) {
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABYSITTER_OBJECT_ID, babysitterObjectId);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(getActivity(), BabysitterActivity.class);
		startActivity(intent);
	}

	@Override
	public void onRefreshStarted(View v) {
		mAdapter.loadObjects();
	}
}
