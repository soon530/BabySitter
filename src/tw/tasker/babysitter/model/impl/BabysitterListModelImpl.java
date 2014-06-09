package tw.tasker.babysitter.model.impl;

import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.impl.BabysitterListPresenterImpl;

public class BabysitterListModelImpl implements BabysitterListModel {

	// Maximum results returned from a Parse query
	//private static final int MAX_POST_SEARCH_RESULTS = 20;
	private BabysitterListPresenterImpl mBabysitterListPresenterImpl;


	public BabysitterListModelImpl(
			BabysitterListPresenterImpl babysitterListPresenterImpl) {
		mBabysitterListPresenterImpl = babysitterListPresenterImpl;
	}

	@Override
	public void doListQuery() {
	}

	@Override
	public Babysitter getOutline(int position) {
		return null;
	}

}
