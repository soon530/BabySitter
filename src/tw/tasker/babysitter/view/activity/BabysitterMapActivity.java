package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.makeLogTag;

import java.util.ArrayList;
import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.City;
import tw.tasker.babysitter.presenter.BabysitterMapPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterMapPresenterImpl;
import tw.tasker.babysitter.utils.LogUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class BabysitterMapActivity extends ActionBarActivity implements
		OnInfoWindowClickListener, OnMapLoadedCallback {
	private static final String TAG = makeLogTag(BabysitterMapActivity.class);

	private GoogleMap mMap;
	private BabysitterMapPresenter mPresneter;
    private ClusterManager<BabysitterItem> mClusterManager;
	private ProgressDialog mRingProgressDialog;

	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ProgressBarUtils.init(this);
	    //setProgressBarVisibility(true);
		
		setContentView(R.layout.fragment_search_babysitter_map);
		mRingProgressDialog = ProgressDialog.show(
				this, "請稍等 ...", "資料準備中...", true);

		mPresneter = new BabysitterMapPresenterImpl(this);
		setUpMapIfNeeded();
		if (mMap != null) {
			mMap.setOnMapLoadedCallback(this);
			//mMap.setOnInfoWindowClickListener(this);
		
			mClusterManager = new ClusterManager<BabysitterItem>(this, mMap);
			mClusterManager.setRenderer(new BabysitterRenderer(this, mMap, mClusterManager));
			
/*			mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				
				@Override
				public void onCameraChange(CameraPosition position) {
					
					LatLngBounds latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
					LatLng northeast = latLngBounds.northeast;
					LatLng southwest =latLngBounds.southwest;
					LatLng center = latLngBounds.getCenter();
					
					LogUtils.LOGD("vic", "northeast["+northeast.latitude+"]["+northeast.longitude+"] "
									+ "- center["+center.latitude+"]["+center.longitude+"] "
									+ "- southwest["+southwest.latitude+"]["+southwest.longitude+"]");
				}
			});
*/		}
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

	public void showMyLocation(double lat, double lng/*LatLngBounds latLngBounds*/) {
		//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5);
		
		LatLng latLng = new LatLng(lat, lng);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
		mMap.animateCamera(cameraUpdate);
		mMap.setMyLocationEnabled(true);
	}

	public void AddMarkers(List<Babysitter> babysitters, ParseGeoPoint myPoint) {
		ArrayList<BabysitterItem> items = new ArrayList<BabysitterItem>();
		
		for (Babysitter babysitter : babysitters) {
			items.add(new BabysitterItem(babysitter, myPoint));
		}
		mClusterManager.addItems(items);
		
		
		getCity();
		
		//TODO 先預設抓高雄
		//showMyLocation(null);
		
		//ProgressBarUtils.hide(this);
	}
	
	private void getCity() {
		ParseQuery<City> mapQuery = City.getQuery();
		mapQuery.whereNear("location", Config.MY_LOCATION);
		mapQuery.getFirstInBackground(new GetCallback<City>() {
			
			@Override
			public void done(City city, ParseException e) {
				
				ParseGeoPoint point = city.getLocation();
				showMyLocation(point.getLatitude(), point.getLongitude());
				LogUtils.LOGD("vic", "city=" + city.getName());
				mRingProgressDialog.dismiss();
			}
		});
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		//mPresneter.onInfoWindowClick(marker);
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
