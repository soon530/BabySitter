package tw.tasker.babysitter;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;

public class MyLocation implements ConnectionCallbacks,
		OnConnectionFailedListener {
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private MapActivity mMapAcivity;

	public MyLocation(MapActivity mapActivity) {
		mMapAcivity = mapActivity;
		mLocationClient = new LocationClient(mapActivity, this, this);
	}
	
	public void connect() {
		mLocationClient.connect();
	}
	
	public void disconnect() {
		mLocationClient.disconnect();
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		mCurrentLocation = getLocation();
	}

	private Location getLocation() {
		// If Google Play Services is available
		if (servicesConnected()) {
			// Get the current location
			return mLocationClient.getLastLocation();
		} else {
			return null;
		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mMapAcivity);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// if (Application.APPDEBUG) {
				// In debug mode, log the status
				Log.d(MapActivity.MAP_ACTIVITY_TAG, "Google play services available");
			// }
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
/*			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(),
						Application.APPTAG);
			}*/
			return false;
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

}