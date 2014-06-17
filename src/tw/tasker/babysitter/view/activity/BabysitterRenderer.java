package tw.tasker.babysitter.view.activity;

import java.util.HashMap;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.adapter.MyInfoWindowAdapter;
import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class BabysitterRenderer extends DefaultClusterRenderer<BabysitterItem> {

	private HashMap<String, Babysitter> mMapModel= new HashMap<String, Babysitter>();
	public BabysitterRenderer(Context context, GoogleMap map, ClusterManager<BabysitterItem> clusterManager, HashMap<String, Babysitter> map_model) {
		super(context, map, clusterManager);
		map.setInfoWindowAdapter(clusterManager.getMarkerManager());
		
		clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyInfoWindowAdapter(context, mMapModel));
		map.setOnCameraChangeListener(clusterManager);
		//map.setOnMarkerClickListener(clusterManager);
	}
	
	@Override
	protected void onClusterItemRendered(BabysitterItem clusterItem, Marker marker) {
		super.onClusterItemRendered(clusterItem, marker);

		mMapModel.put(marker.getId(), clusterItem.getBabysitter());
	}
		
}