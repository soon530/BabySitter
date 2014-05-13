package tw.tasker.babysitter.presenter;

import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.BabysitterMapModel;
import tw.tasker.babysitter.model.BabysitterMapModelImpl;
import tw.tasker.babysitter.model.OnFinishedListener;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.view.BabysitterDetailActivity;
import tw.tasker.babysitter.view.BabysitterListActivity;
import tw.tasker.babysitter.view.BabysitterMapActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

public class BabysitterMapPresenterImpl implements BabysitterMapPresenter, OnFinishedListener {
	private static final String TAG = makeLogTag(BabysitterMapPresenterImpl.class);

	private BabysitterMapActivity mBabysitterMap;
	private MyLocation mMyLocation;
	private BabysitterMapModel mMapModel;

	public BabysitterMapPresenterImpl(
			BabysitterMapActivity babysitterMapActivity) {
		mBabysitterMap = babysitterMapActivity;
		mMapModel = new BabysitterMapModelImpl(this);

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

		mMapModel.doMapQuery(myPoint);
	}

	/*
	 * Helper method to get the Parse GEO point representation of a location
	 */
	private ParseGeoPoint geoPointFromLocation(Location loc) {
		return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
	}

	@Override
	public void onFinished(List<MarkerOptions> markerOptions) {
		mBabysitterMap.showMarkers(markerOptions);
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
