package tw.tasker.babysitter.presenter.impl;

import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.data.BabysitterOutline;
import tw.tasker.babysitter.model.impl.BabysitterListModelImpl;
import tw.tasker.babysitter.presenter.BabysitterListPresenter;
import tw.tasker.babysitter.view.BabysitterDetailActivity;
import tw.tasker.babysitter.view.BabysitterListFragment;
import android.content.Intent;
import android.widget.BaseAdapter;

public class BabysitterListPresenterImpl implements BabysitterListPresenter {

	private BabysitterListFragment mBabysitterListFragment;
	private BabysitterListModel mBabysitterListModel;

	public BabysitterListPresenterImpl(
			BabysitterListFragment babysitterListFragment) {
		mBabysitterListFragment = babysitterListFragment;
		mBabysitterListModel = new BabysitterListModelImpl(this, mBabysitterListFragment.getActivity().getApplicationContext());
	}

	@Override
	public void onCreate() {
		mBabysitterListFragment.showProgress();
		mBabysitterListModel.doListQuery();
	}


	public void setAdapter(BaseAdapter adapter) {
		mBabysitterListFragment.setAdapter(adapter);
		mBabysitterListFragment.hideProgress();
	}

	@Override
	public void onListItemClick(int position) {
		BabysitterOutline outline = getOutline(position);
		Intent detailIntent = new Intent(mBabysitterListFragment.getActivity(), BabysitterDetailActivity.class);
		detailIntent.putExtra("objectId", outline.getObjectId());
		mBabysitterListFragment.getActivity().startActivity(detailIntent);
	}

	private BabysitterOutline getOutline(int position) {
		return mBabysitterListModel.getOutline(position);
	}

}
