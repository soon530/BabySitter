package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.BabysitterMapPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterMapPresenterImpl;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

public class BabysitterMapActivity extends ActionBarActivity implements
		OnInfoWindowClickListener, OnMapLoadedCallback {
	private static final String TAG = makeLogTag(BabysitterMapActivity.class);

	private GoogleMap mMap;
	private BabysitterMapPresenter mPresneter;
	private HashMap<String, Babysitter> map_model= new HashMap<String, Babysitter>();
    private ClusterManager<BabysitterItem> mClusterManager;

	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);
		ProgressBarUtils.show(this);
		setContentView(R.layout.fragment_search_babysitter_map);
		mPresneter = new BabysitterMapPresenterImpl(this);
		setUpMapIfNeeded();
		if (mMap != null) {
			mMap.setOnMapLoadedCallback(this);
			mMap.setOnInfoWindowClickListener(this);
		
			mClusterManager = new ClusterManager<BabysitterItem>(this, mMap);
			mClusterManager.setRenderer(new BabysitterRenderer(this, mMap, mClusterManager, map_model));
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

	public void showMyLocation(LatLngBounds latLngBounds) {
		//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5);
		//mMap.animateCamera(cameraUpdate);
		mMap.setMyLocationEnabled(true);
	}

	public void AddMarkers(List<Babysitter> babysitters) {
		ArrayList<BabysitterItem> items = new ArrayList<BabysitterItem>();
		
		for (Babysitter babysitter : babysitters) {
			items.add(new BabysitterItem(babysitter));
		}
		mClusterManager.addItems(items);
		
		ProgressBarUtils.hide(this);
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
