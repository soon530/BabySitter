package tw.tasker.babysitter.presenter;

import com.google.android.gms.maps.model.Marker;

public interface BabysitterMapPresenter {
	void onOptionsItemSelected(int id);

	void onInfoWindowClick(Marker marker);
}
