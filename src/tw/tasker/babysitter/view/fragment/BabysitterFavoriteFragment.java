package tw.tasker.babysitter.view.fragment;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.presenter.adapter.BabysitterFavoriteParseQueryAdapter;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
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

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabysitterFavoriteFragment extends BaseFragment implements
		OnItemClickListener, OnQueryLoadListener<BabysitterFavorite>, OnRefreshListener {
	private ParseQueryAdapter<BabysitterFavorite> mAdapter;

	public static Fragment newInstance(int position) {
		BabysitterFavoriteFragment fragment = new BabysitterFavoriteFragment();
		return fragment;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		doListQuery();
	}

	public void doListQuery() {
		mAdapter = new BabysitterFavoriteParseQueryAdapter(getActivity());
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		mAdapter.addOnQueryLoadListener(this);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void onLoading() {
		showLoading();
	}

	@Override
	public void onLoaded(List<BabysitterFavorite> babysitterFavorites, Exception arg1) {

/*		if (DisplayUtils.hasNetwork(getActivity())) {
			ParseObject.pinAllInBackground(babysitterFavorites);
			LogUtils.LOGD("vic", "babysitterFavorites pin size:" + babysitterFavorites.size());
		}
*/
		hideLoading();
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
