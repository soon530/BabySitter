package tw.tasker.babysitter.view;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.HashMap;
import java.util.List;

import tw.tasker.babysitter.R;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BabysitterMapActivity extends ActionBarActivity implements
		OnInfoWindowClickListener, BabysitterMapView, OnMapLoadedCallback {
	private static final String TAG = makeLogTag(BabysitterMapActivity.class);

	private GoogleMap mMap;
	private BabysitterMapPresenter mPresneter;
	private HashMap<String, BabysitterOutline> map_model= new HashMap<String, BabysitterOutline>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search_babysitter_map);

		mPresneter = new BabysitterMapPresenterImpl(this);
		setUpMapIfNeeded();
		if (mMap != null) {
			mMap.setOnMapLoadedCallback(this);
			mMap.setOnInfoWindowClickListener(this);
			mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getLayoutInflater(), map_model));
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
	
	public void AddMarkers(List<BabysitterOutline> outlines) {
		for (BabysitterOutline outline : outlines) {

			MarkerOptions markerOpts = getOutlineMarkerOptions(outline);
			Marker mark = mMap.addMarker(markerOpts);
			map_model.put(mark.getId(), outline);
		}
		
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
