package tw.tasker.babysitter.view.fragment;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.Favorite;
import tw.tasker.babysitter.presenter.adapter.FavoriteBabyParseQueryAdapter;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.BabyDetailActivity;
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


public class BabyFavoriteFragment extends Fragment implements
		OnItemClickListener, OnQueryLoadListener<Favorite> {
	private ParseQueryAdapter<Favorite> mAdapter;
	private GridView mList;
	private TextView mEmpty;

	public static Fragment newInstance(int position) {
		BabyFavoriteFragment fragment = new BabyFavoriteFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.baby_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id) {
		case R.id.refresh:
			mAdapter.loadObjects();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_baby_list,
				container, false);

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
		mAdapter = new FavoriteBabyParseQueryAdapter(getActivity());
		mAdapter.setAutoload(false);
		//mAdapter.setPaginationEnabled(false);
		mAdapter.setObjectsPerPage(6);
		mAdapter.addOnQueryLoadListener(this);
		mList.setAdapter(mAdapter);
		mAdapter.loadObjects();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Favorite favorite = mAdapter.getItem(position);
		Baby baby = favorite.getBaby();
		seeBabyDetail(baby.getObjectId());
	}

	public void seeBabyDetail(String babyObjectId) {
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABY_OBJECT_ID, babyObjectId);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(getActivity(), BabyDetailActivity.class);
		startActivity(intent);
	}

	@Override
	public void onLoading() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void onLoaded(List<Favorite> favorite, Exception e) {
		ProgressBarUtils.hide(getActivity());
	}

}
