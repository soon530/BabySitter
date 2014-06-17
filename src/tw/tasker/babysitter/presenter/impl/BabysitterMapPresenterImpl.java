package tw.tasker.babysitter.presenter.impl;

import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.BabysitterMapPresenter;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.view.activity.BabysitterActivity;
import tw.tasker.babysitter.view.activity.BabysitterMapActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class BabysitterMapPresenterImpl extends FindCallback<Babysitter> implements BabysitterMapPresenter {
	private static final String TAG = makeLogTag(BabysitterMapPresenterImpl.class);

	private BabysitterMapActivity mView;
	private MyLocation mMyLocation;

	private static final int MAX_POST_SEARCH_RESULTS = 20;
	// Maximum post search radius for map in kilometers
	private static final int MAX_POST_SEARCH_DISTANCE = 100;

	
	public BabysitterMapPresenterImpl(
			BabysitterMapActivity babysitterMapActivity) {
		mView = babysitterMapActivity;

		mMyLocation = new MyLocation(mView);
	}

	@Override
	public void onMapLoaded() {
		mView.showMyLocation(mMyLocation.getmBounds());
		doMapQuery();
	}

	private void doMapQuery() {
		Location myLoc = mMyLocation.getCurLocation();

		doMapQuery(myLoc);
	}
	
	
	public void doMapQuery(Location myLoc) {
		final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);

		ParseQuery<Babysitter> mapQuery = Babysitter.getQuery();
		// Set up additional query filters
		//.whereWithinKilometers("location", myPoint, MAX_POST_SEARCH_DISTANCE);
		// mapQuery.include("user");
		mapQuery.orderByDescending("createdAt");
		//mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);
		// mapQuery.fromLocalDatastore();
		// Kick off the query in the background
		mapQuery.findInBackground(this);
		//mapQuery.whereWithinGeoBox(key, southwest, northeast)
		
	}

	/*
	 * Helper method to get the Parse GEO point representation of a location
	 */
	private ParseGeoPoint geoPointFromLocation(Location loc) {
		return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
	}

	@Override
	public void done(List<Babysitter> babysitters, ParseException arg1) {
		mView.AddMarkers(babysitters);
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		final Intent intent = new Intent();
		intent.setClass(mView, BabysitterActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABYSITTER_OBJECT_ID, marker.getTitle());
		intent.putExtras(bundle);
		mView.startActivity(intent);
	}
}
