package tw.tasker.babysitter;

import static tw.tasker.babysitter.LogUtils.LOGD;
import static tw.tasker.babysitter.LogUtils.makeLogTag;

import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysitterMapActivity extends ActionBarActivity {
	private static final String TAG = makeLogTag(BabysitterMapActivity.class);

	private static final int MAX_POST_SEARCH_RESULTS = 20;

	private GoogleMap mMap;
	private MyLocation mMyLocation;
	// Maximum post search radius for map in kilometers
	private static final int MAX_POST_SEARCH_DISTANCE = 100;
	private static final String YOUR_APPLICATION_ID = "NJFvH3uzP9EHAKydw7iSIICBBU4AfAHvhJzuTawu";
	private static final String YOUR_CLIENT_KEY = "FOwFRZ8hqGZ4NdZflfeLINvBQehNXOlihdEKnwTU";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search_babysitter_map);

		ParseObject.registerSubclass(BabysitterOutline.class);
		ParseObject.registerSubclass(BabysitterComment.class);

		Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

		setUpMapIfNeeded();
		mMyLocation = new MyLocation(this);

		if (mMap != null) {
			mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
				@Override
				public void onMapLoaded() {
					if (mMap != null) {
						mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
								mMyLocation.getmBounds(), 5));
						doMapQuery();
					}
				}
			});

			final Intent intent = new Intent();
			intent.setClass(this, BabysitterDetailActivity.class);

			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					Bundle bundle = new Bundle();
					bundle.putString("objectId", marker.getTitle());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		}

		ParseQueryAdapter.QueryFactory<BabysitterOutline> factor = new ParseQueryAdapter.QueryFactory<BabysitterOutline>() {
			public ParseQuery<BabysitterOutline> create() {
				return null;
			}
		};

	}

	private void doMapQuery() {
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
		mapQuery.findInBackground(new FindCallback<BabysitterOutline>() {
			@Override
			public void done(List<BabysitterOutline> objects, ParseException e) {
				LOGD(TAG, "findInBackground done()");

				for (BabysitterOutline outline : objects) {

					double lat = outline.getLocation().getLatitude();
					double lng = outline.getLocation().getLongitude();
					LOGD(TAG, "outline" + outline.getText() + ",lat" + lat
							+ ",lng" + lng);

					LatLng latLng = new LatLng(lat, lng);

					MarkerOptions markerOpts = new MarkerOptions();
					markerOpts.position(latLng);
					markerOpts.title(outline.getObjectId());
					markerOpts.snippet("保母：" + outline.getText() + "\n已托育："
							+ outline.getBabycareCount());
					BitmapDescriptor icon = BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED);
					markerOpts.icon(icon);

					mMap.addMarker(markerOpts);

				}
			}
		});
	}

	/*
	 * Helper method to get the Parse GEO point representation of a location
	 */
	private ParseGeoPoint geoPointFromLocation(Location loc) {
		return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.babysitter_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_list) {
			Intent intent = new Intent();
			intent.setClass(this, BabysitterListActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
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
		mMyLocation.disconnect();
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				// mMap.setOnMyLocationButtonClickListener(this);
			}
		}
	}
}
