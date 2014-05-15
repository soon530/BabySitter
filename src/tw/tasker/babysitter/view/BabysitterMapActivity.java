package tw.tasker.babysitter.view;

import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.model.BabysitterOutline;
import tw.tasker.babysitter.presenter.BabysitterMapPresenter;
import tw.tasker.babysitter.presenter.BabysitterMapPresenterImpl;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseObject;

public class BabysitterMapActivity extends ActionBarActivity implements
		OnInfoWindowClickListener, BabysitterMapView, OnMapLoadedCallback {
	private static final String TAG = makeLogTag(BabysitterMapActivity.class);

	private GoogleMap mMap;
	private BabysitterMapPresenter mPresneter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search_babysitter_map);

		mPresneter = new BabysitterMapPresenterImpl(this);
		setUpMapIfNeeded();
		if (mMap != null) {
			mMap.setOnMapLoadedCallback(this);
			mMap.setOnInfoWindowClickListener(this);
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
	}

	@Override
	public void onMapLoaded() {
		mPresneter.onMapLoaded();
	}

	@Override
	public void showMyLocation(LatLngBounds latLngBounds) {
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
				latLngBounds, 5);
		mMap.animateCamera(cameraUpdate);
		mMap.setMyLocationEnabled(true);
	}

	@Override
	public void showMarkers(List<MarkerOptions> markerOptions) {
		for (MarkerOptions maker : markerOptions) {
			mMap.addMarker(maker);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.babysitter_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		mPresneter.onOptionsItemSelected(id);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		mPresneter.onInfoWindowClick(marker);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		// mMyLocation.disconnect();
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
