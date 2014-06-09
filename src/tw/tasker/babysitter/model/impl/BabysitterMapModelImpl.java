package tw.tasker.babysitter.model.impl;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.List;

import tw.tasker.babysitter.model.BabysitterMapModel;
import tw.tasker.babysitter.model.OnFinishedListener;
import tw.tasker.babysitter.model.data.Babysitter;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class BabysitterMapModelImpl extends FindCallback<Babysitter>
		implements BabysitterMapModel {
	private static final String TAG = makeLogTag(BabysitterMapModelImpl.class);

	private static final int MAX_POST_SEARCH_RESULTS = 20;
	// Maximum post search radius for map in kilometers
	private static final int MAX_POST_SEARCH_DISTANCE = 100;

	private OnFinishedListener mFinishedListener;

	public BabysitterMapModelImpl(OnFinishedListener finishedListener) {

		mFinishedListener = finishedListener;
	}

	@Override
	public void doMapQuery(Location myLoc) {
		final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);

		ParseQuery<Babysitter> mapQuery = Babysitter.getQuery();
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
	public void done(List<Babysitter> objects, ParseException e) {
		LOGD(TAG, "findInBackground done()");
/*		List<MarkerOptions> babysitterMarkerOptions = new ArrayList<MarkerOptions>();

		for (Babysitter outline : objects) {

			MarkerOptions markerOpts = getOutlineMarkerOptions(outline);
			babysitterMarkerOptions.add(markerOpts);

		}
		mFinishedListener.onFinished(babysitterMarkerOptions);*/
		
		mFinishedListener.onDataFinished(objects);
	}

	private MarkerOptions getOutlineMarkerOptions(Babysitter outline) {
		double lat = outline.getLocation().getLatitude();
		double lng = outline.getLocation().getLongitude();
		LOGD(TAG, "outline" + outline.getName() + ",lat" + lat + ",lng" + lng);

		LatLng latLng = new LatLng(lat, lng);

		MarkerOptions markerOpts = new MarkerOptions();
		markerOpts.position(latLng);
		markerOpts.title(outline.getObjectId());
		markerOpts.snippet("保母：" + outline.getName() + "\n已托育："
				+ outline.getBabycareCount());
		BitmapDescriptor icon = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED);
		markerOpts.icon(icon);
		return markerOpts;
	}
}
