package tw.tasker.babysitter.presenter;

import com.google.android.gms.maps.model.Marker;

public interface BabysitterMapPresenter {
	void onInfoWindowClick(Marker marker);

	void onMapLoaded();
}
