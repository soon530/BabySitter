package tw.tasker.babysitter;

import static tw.tasker.babysitter.LogUtils.LOGD;
import static tw.tasker.babysitter.LogUtils.makeLogTag;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MyLocation implements ConnectionCallbacks,
		OnConnectionFailedListener {
	private static final String TAG = makeLogTag(MyLocation.class);

	// Initial offset for calculating the map bounds
	private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

	// Accuracy for calculating the map bounds
	private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

	private static final float METERS_PER_FEET = 0.3048f;

	private static final int RADIUS = 250;

	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private MapActivity mMapAcivity;
	private LatLngBounds mBounds;

	public LatLngBounds getmBounds() {
		return mBounds;
	}

	public MyLocation(MapActivity mapActivity) {
		mMapAcivity = mapActivity;
		mLocationClient = new LocationClient(mapActivity, this, this);
		connect();
	}

	public void connect() {
		mLocationClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mCurrentLocation = getLocation();
		updateZoom();
	}

	private Location getLocation() {
		// If Google Play Services is available
		if (servicesConnected()) {
			// Get the current location
			Log.i("vic", "getLocation()");
			return mLocationClient.getLastLocation();
		} else {
			return null;
		}
	}

	private void updateZoom() {
		double latitude = mCurrentLocation.getLatitude();
		double longitude = mCurrentLocation.getLongitude();
		LatLng myLatLng = new LatLng(latitude, longitude);
		mBounds = calculateBoundsWithCenter(myLatLng);
	}

	public LatLngBounds calculateBoundsWithCenter(LatLng myLatLng) {
		// Create a bounds
		LatLngBounds.Builder builder = LatLngBounds.builder();

		// Calculate east/west points that should to be included
		// in the bounds
		double lngDifference = calculateLatLngOffset(myLatLng, false);
		LatLng east = new LatLng(myLatLng.latitude, myLatLng.longitude
				+ lngDifference);
		builder.include(east);
		LatLng west = new LatLng(myLatLng.latitude, myLatLng.longitude
				- lngDifference);
		builder.include(west);

		// Calculate north/south points that should to be included
		// in the bounds
		double latDifference = calculateLatLngOffset(myLatLng, true);
		LatLng north = new LatLng(myLatLng.latitude + latDifference,
				myLatLng.longitude);
		builder.include(north);
		LatLng south = new LatLng(myLatLng.latitude - latDifference,
				myLatLng.longitude);
		builder.include(south);

		return builder.build();
	}

	public void disconnect() {
		mLocationClient.disconnect();
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mMapAcivity);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			LOGD(TAG, "Google play services available");
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog

			// Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
			// mMapAcivity, 0);
			// if (dialog != null) {
			// ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// errorFragment.setDialog(dialog);
			// errorFragment.show(mMapAcivity.getSupportFragmentManager(),
			// "tasker");
			// }

			return false;
		}
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	}

	private double calculateLatLngOffset(LatLng myLatLng, boolean bLatOffset) {
		// The return offset, initialized to the default difference
		double latLngOffset = OFFSET_CALCULATION_INIT_DIFF;
		// Set up the desired offset distance in meters
		float desiredOffsetInMeters = RADIUS * METERS_PER_FEET;
		// Variables for the distance calculation
		float[] distance = new float[1];
		boolean foundMax = false;
		double foundMinDiff = 0;
		// Loop through and get the offset
		do {
			// Calculate the distance between the point of interest
			// and the current offset in the latitude or longitude direction
			if (bLatOffset) {
				Location.distanceBetween(myLatLng.latitude, myLatLng.longitude,
						myLatLng.latitude + latLngOffset, myLatLng.longitude,
						distance);
			} else {
				Location.distanceBetween(myLatLng.latitude, myLatLng.longitude,
						myLatLng.latitude, myLatLng.longitude + latLngOffset,
						distance);
			}
			// Compare the current difference with the desired one
			float distanceDiff = distance[0] - desiredOffsetInMeters;
			if (distanceDiff < 0) {
				// Need to catch up to the desired distance
				if (!foundMax) {
					foundMinDiff = latLngOffset;
					// Increase the calculated offset
					latLngOffset *= 2;
				} else {
					double tmp = latLngOffset;
					// Increase the calculated offset, at a slower pace
					latLngOffset += (latLngOffset - foundMinDiff) / 2;
					foundMinDiff = tmp;
				}
			} else {
				// Overshot the desired distance
				// Decrease the calculated offset
				latLngOffset -= (latLngOffset - foundMinDiff) / 2;
				foundMax = true;
			}
		} while (Math.abs(distance[0] - desiredOffsetInMeters) > OFFSET_CALCULATION_ACCURACY);
		return latLngOffset;
	}
}