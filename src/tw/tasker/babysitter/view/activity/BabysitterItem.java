package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.LogUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.parse.ParseGeoPoint;

public class BabysitterItem implements ClusterItem {
    private final LatLng mPosition;
	private final Babysitter mBabysitter;

    public BabysitterItem(Babysitter babysitter, ParseGeoPoint myPoint) {
		double lat = babysitter.getLocation().getLatitude();
		double lng = babysitter.getLocation().getLongitude();
        mPosition = new LatLng(lat, lng);
        float distance = (float) babysitter.getLocation().distanceInKilometersTo(myPoint);
        babysitter.setDistance(distance);
        mBabysitter = babysitter;
		//LogUtils.LOGD("vic", babysitter.getName() + ", 距離:" + distance);

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    
    public Babysitter getBabysitter() {
    	return mBabysitter;
    }
}
