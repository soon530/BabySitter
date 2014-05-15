package tw.tasker.babysitter.presenter;

import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.BabysitterListModelImpl;
import tw.tasker.babysitter.view.BabysitterListFragment;
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
		// TODO Auto-generated method stub
		mBabysitterListFragment.showProgress();
		mBabysitterListModel.doListQuery();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}
	

	public void setAdapter(BaseAdapter adapter) {
		mBabysitterListFragment.setAdapter(adapter);
		mBabysitterListFragment.hideProgress();
	}

}
