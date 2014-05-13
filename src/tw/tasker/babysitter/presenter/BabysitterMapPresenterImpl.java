package tw.tasker.babysitter.presenter;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.ArrayList;
import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.BabysitterOutline;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.view.BabysitterDetailActivity;
import tw.tasker.babysitter.view.BabysitterListActivity;
import tw.tasker.babysitter.view.BabysitterMapActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class BabysitterMapPresenterImpl extends FindCallback<BabysitterOutline>
		implements BabysitterMapPresenter {

	private BabysitterMapActivity mBabysitterMap;
	private MyLocation mMyLocation;
	private static final int MAX_POST_SEARCH_RESULTS = 20;
	// Maximum post search radius for map in kilometers
	private static final int MAX_POST_SEARCH_DISTANCE = 100;
	private static final String TAG = makeLogTag(BabysitterMapPresenterImpl.class);

	public BabysitterMapPresenterImpl(
			BabysitterMapActivity babysitterMapActivity) {
		mBabysitterMap = babysitterMapActivity;
		mMyLocation = new MyLocation(mBabysitterMap);

	}

	@Override
	public void onMapLoaded() {
		mBabysitterMap.showMyLocation(mMyLocation.getmBounds());
		doMapQuery();
	}

	public void doMapQuery() {
		Location myLoc = mMyLocation.getCurLocation();
		final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
		ParseQuery<BabysitterOutline> mapQuery = BabysitterOutline.getQuery();
		// Set up additional query filters
		mapQuery.whereWithinKilometers("location", myPoint,
				MAX_POST_SEARCH_DISTANCE);
		// mapQuery.include("user");
		mapQuery.orderByDescending("createdAt");
		mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);

		// Kick off the query in the background
		mapQuery.findInBackground(this);
	}

	/*
	 * Helper method to get the Parse GEO point representation of a location
	 */
	private ParseGeoPoint geoPointFromLocation(Location loc) {
		return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
	}

	@Override
	public void done(List<BabysitterOutline> objects, ParseException e) {
		LOGD(TAG, "findInBackground done()");
		List<MarkerOptions> babysitterMarkerOptions = new ArrayList<MarkerOptions>();

		for (BabysitterOutline outline : objects) {

			MarkerOptions markerOpts = getOutlineMarkerOptions(outline);
			babysitterMarkerOptions.add(markerOpts);

		}
		mBabysitterMap.showMarkers(babysitterMarkerOptions);
	}

	private MarkerOptions getOutlineMarkerOptions(BabysitterOutline outline) {
		double lat = outline.getLocation().getLatitude();
		double lng = outline.getLocation().getLongitude();
		LOGD(TAG, "outline" + outline.getText() + ",lat" + lat + ",lng" + lng);

		LatLng latLng = new LatLng(lat, lng);

		MarkerOptions markerOpts = new MarkerOptions();
		markerOpts.position(latLng);
		markerOpts.title(outline.getObjectId());
		markerOpts.snippet("保母：" + outline.getText() + "\n已托育："
				+ outline.getBabycareCount());
		BitmapDescriptor icon = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED);
		markerOpts.icon(icon);
		return markerOpts;
	}

	@Override
	public void onOptionsItemSelected(int id) {
		if (id == R.id.action_list) {
			Intent intent = new Intent();
			intent.setClass(mBabysitterMap, BabysitterListActivity.class);
			mBabysitterMap.startActivity(intent);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		final Intent intent = new Intent();
		intent.setClass(mBabysitterMap, BabysitterDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("objectId", marker.getTitle());
		intent.putExtras(bundle);
		mBabysitterMap.startActivity(intent);
	}

}
