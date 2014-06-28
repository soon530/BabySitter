package tw.tasker.babysitter.presenter.impl;

import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.BabysitterMapPresenter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.view.activity.BabysitterMapActivity;
import android.location.Location;

import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class BabysitterMapPresenterImpl extends FindCallback<Babysitter> implements BabysitterMapPresenter {
	private static final String TAG = makeLogTag(BabysitterMapPresenterImpl.class);

	private BabysitterMapActivity mView;
	private MyLocation mMyLocation;

	public BabysitterMapPresenterImpl(
			BabysitterMapActivity babysitterMapActivity) {
		mView = babysitterMapActivity;
	}

	@Override
	public void onMapLoaded() {
		doMapQuery();
		LogUtils.LOGD("vic", "onMapLoaded..");
	}

	public void doMapQuery() {

		ParseQuery<Babysitter> mapQuery = Babysitter.getQuery();
		mapQuery.whereWithinKilometers("location", Config.MY_LOCATION, Config.MAX_POST_SEARCH_DISTANCE);
		mapQuery.findInBackground(this);
		// mapQuery.fromLocalDatastore();
		//mapQuery.whereWithinGeoBox(key, southwest, northeast)
	}

	@Override
	public void done(List<Babysitter> babysitters, ParseException arg1) {
		mView.AddMarkers(babysitters, Config.MY_LOCATION);
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
/*		final Intent intent = new Intent();
		intent.setClass(mView, BabysitterActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABYSITTER_OBJECT_ID, marker.getTitle());
		intent.putExtras(bundle);
		mView.startActivity(intent);
*/	}
}
