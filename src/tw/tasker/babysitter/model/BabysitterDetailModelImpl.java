package tw.tasker.babysitter.model;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenterImpl;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysitterDetailModelImpl extends GetCallback<BabysitterOutline>
		implements BabysitterDetailModel {
	// Maximum results returned from a Parse query
	private static final int MAX_POST_SEARCH_RESULTS = 20;
	private BabysitterDetailPresenterImpl mListener;

	public BabysitterDetailModelImpl(BabysitterDetailPresenterImpl listener) {
		mListener = listener;
	}

	@Override
	public void doDetailQuery(String objectId) {
		LOGD("vic", "objectId" + objectId);
		ParseQuery<BabysitterOutline> detailQuery = BabysitterOutline
				.getQuery();
		detailQuery.getInBackground(objectId, this);
	}

	@Override
	public void done(BabysitterOutline outline, ParseException e) {
		if (e != null) {
			LOGD("vic", "done", e);
		} else {
			mListener.fillHeaderUI(outline);
}

	}

	@Override
	public ParseQueryAdapter.QueryFactory<BabysitterComment> getFactory(
			final String objectId) {
		// Set up a customized query
		ParseQueryAdapter.QueryFactory<BabysitterComment> factory = new ParseQueryAdapter.QueryFactory<BabysitterComment>() {
			public ParseQuery<BabysitterComment> create() {
				// Location myLoc = (currentLocation == null) ? lastLocation :
				// currentLocation;
				ParseQuery<BabysitterComment> query = BabysitterComment
						.getQuery();
				// query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("babysitterId", objectId);
				// query.whereWithinKilometers("location",
				// geoPointFromLocation(myLoc), radius * METERS_PER_FEET /
				// METERS_PER_KILOMETER);
				query.setLimit(MAX_POST_SEARCH_RESULTS);
				return query;
			}
		};
		return factory;
	}
}
