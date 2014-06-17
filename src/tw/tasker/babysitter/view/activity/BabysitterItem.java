package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.model.data.Babysitter;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class BabysitterItem implements ClusterItem {
    private final LatLng mPosition;
	private final Babysitter mBabysitter;

    public BabysitterItem(Babysitter babysitter) {
		double lat = babysitter.getLocation().getLatitude();
		double lng = babysitter.getLocation().getLongitude();
        mPosition = new LatLng(lat, lng);
        mBabysitter = babysitter;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    
    public Babysitter getBabysitter() {
    	return mBabysitter;
    }

}
