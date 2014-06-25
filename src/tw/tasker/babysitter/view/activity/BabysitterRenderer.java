package tw.tasker.babysitter.view.activity;

import java.util.HashMap;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.adapter.MyInfoWindowAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class BabysitterRenderer extends DefaultClusterRenderer<BabysitterItem> implements OnInfoWindowClickListener {

	private HashMap<String, Babysitter> mMapModel = new HashMap<String, Babysitter>();
	private Context mContext;
	
	public BabysitterRenderer(Context context, GoogleMap map, ClusterManager<BabysitterItem> clusterManager) {
		super(context, map, clusterManager);
		map.setInfoWindowAdapter(clusterManager.getMarkerManager());
		
		clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyInfoWindowAdapter(context, mMapModel));
		map.setOnCameraChangeListener(clusterManager);
		map.setOnInfoWindowClickListener(this);
		mContext = context;
		//map.setOnMarkerClickListener(clusterManager);
	}
	
	@Override
	protected void onClusterItemRendered(BabysitterItem clusterItem, Marker marker) {
		super.onClusterItemRendered(clusterItem, marker);
		mMapModel.put(marker.getId(), clusterItem.getBabysitter());
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		final Intent intent = new Intent();
		intent.setClass(mContext, BabysitterActivity.class);
		Bundle bundle = new Bundle();
		Babysitter babysitter = mMapModel.get(marker.getId());
		bundle.putString(Config.BABYSITTER_OBJECT_ID, babysitter.getObjectId());
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}
	
}