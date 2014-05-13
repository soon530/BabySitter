package tw.tasker.babysitter.view;

import java.util.List;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public interface BabysitterMapView {
	void showMyLocation(LatLngBounds latlngBounds);

	void showMarkers(List<MarkerOptions> markerOptions);
}
