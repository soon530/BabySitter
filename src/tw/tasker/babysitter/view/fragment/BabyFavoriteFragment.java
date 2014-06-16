package tw.tasker.babysitter.view.fragment;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyFavorite;
import tw.tasker.babysitter.presenter.adapter.BabyFavoriteParseQueryAdapter;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.EndlessScrollListener;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.BabyRecordActivity;
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

public class BabyFavoriteFragment extends BaseFragment implements
		OnItemClickListener, OnQueryLoadListener<BabyFavorite>, OnRefreshListener {
	private ParseQueryAdapter<BabyFavorite> mAdapter;

	public static Fragment newInstance(int position) {
		BabyFavoriteFragment fragment = new BabyFavoriteFragment();
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		doListQuery();
	}

	public void doListQuery() {
		mAdapter = new BabyFavoriteParseQueryAdapter(getActivity());
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		mAdapter.addOnQueryLoadListener(this);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BabyFavorite favorite = mAdapter.getItem(position);
		BabyDiary baby = favorite.getBabyDiary();
		seeBabyDetail(baby.getObjectId());
	}

	public void seeBabyDetail(String babyObjectId) {
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABY_OBJECT_ID, babyObjectId);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(getActivity(), BabyRecordActivity.class);
		startActivity(intent);
	}

	@Override
	public void onLoading() {
		showLoading();
	}

	@Override
	public void onLoaded(List<BabyFavorite> babyFavorites, Exception e) {
/*		if (DisplayUtils.hasNetwork(getActivity())) {
			ParseObject.pinAllInBackground(babyFavorites);
			LogUtils.LOGD("vic", "babyFavorites pin size:" + babyFavorites.size());
		}
*/
		hideLoading();
	}

	@Override
	public void onRefreshStarted(View view) {
		mAdapter.loadObjects();
	}
}
