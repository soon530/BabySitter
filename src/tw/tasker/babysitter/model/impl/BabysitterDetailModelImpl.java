package tw.tasker.babysitter.model.impl;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.model.BabysitterDetailModel;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.impl.BabysitterDetailPresenterImpl;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysitterDetailModelImpl extends GetCallback<Babysitter>
		implements BabysitterDetailModel {
	// Maximum results returned from a Parse query
	// private static final int MAX_POST_SEARCH_RESULTS = 20;
	private BabysitterDetailPresenterImpl mListener;

	public BabysitterDetailModelImpl(BabysitterDetailPresenterImpl listener) {
		mListener = listener;
	}

	@Override
	public void doDetailQuery(String objectId) {
		LOGD("vic", "objectId" + objectId);
		ParseQuery<Babysitter> detailQuery = Babysitter.getQuery();
		detailQuery.getInBackground(objectId, this);
	}

	@Override
	public void done(Babysitter outline, ParseException e) {
		if (e != null) {
			LOGD("vic", "done", e);
		} else {
			mListener.fillHeaderUI(outline);
		}

	}

}
