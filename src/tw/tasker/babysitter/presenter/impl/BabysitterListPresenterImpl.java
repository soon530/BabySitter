package tw.tasker.babysitter.presenter.impl;

import java.util.List;

import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.impl.BabysitterListModelImpl;
import tw.tasker.babysitter.presenter.BabysitterListPresenter;
import tw.tasker.babysitter.presenter.adapter.BabysitterListParseQueryAdapter;
import tw.tasker.babysitter.view.impl.BabysitterDetailActivity;
import tw.tasker.babysitter.view.impl.BabysitterListFragment;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabysitterListPresenterImpl implements BabysitterListPresenter, OnQueryLoadListener<Babysitter> {

	private BabysitterListFragment mBabysitterListFragment;
	private BabysitterListModel mBabysitterListModel;
	private ParseQueryAdapter<Babysitter> mAdapter;

	public BabysitterListPresenterImpl(
			BabysitterListFragment babysitterListFragment) {
		mBabysitterListFragment = babysitterListFragment;
		mBabysitterListModel = new BabysitterListModelImpl(this);
	}

	@Override
	public void onCreate() {
		doListQuery();
	}

	private void doListQuery() {
		mAdapter = new BabysitterListParseQueryAdapter(
				mBabysitterListFragment.getActivity());

		// Disable automatic loading when the list_item_babysitter_comment is
		// attached to a view.
		mAdapter.setAutoload(false);

		// Disable pagination, we'll manage the query limit ourselves
		mAdapter.setPaginationEnabled(false);
		mAdapter.addOnQueryLoadListener(this);
		setAdapter(mAdapter);
		mAdapter.loadObjects();
	}

	public void setAdapter(BaseAdapter adapter) {
		mBabysitterListFragment.setAdapter(adapter);
	}

	@Override
	public void onListItemClick(int position) {
		Babysitter outline = getOutline(position);
		Intent detailIntent = new Intent(mBabysitterListFragment.getActivity(),
				BabysitterDetailActivity.class);
		detailIntent.putExtra("objectId", outline.getObjectId());
		mBabysitterListFragment.getActivity().startActivity(detailIntent);
	}

	private Babysitter getOutline(int position) {
		return mAdapter.getItem(position);
	}

	@Override
	public void onLoading() {
		mBabysitterListFragment.showProgress();
	}
	
	@Override
	public void onLoaded(List<Babysitter> babysitter, Exception e) {
		mBabysitterListFragment.hideProgress();
	}
}
