package tw.tasker.babysitter.view;

import java.util.List;

import com.google.android.gms.maps.model.MarkerOptions;

public interface BabysitterMapView {
	void showMyLocation();

	void setMarkers(List<MarkerOptions> markerOptions);
}
