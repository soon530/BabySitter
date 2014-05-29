package tw.tasker.babysitter.model;

import java.util.List;

import tw.tasker.babysitter.model.data.BabysitterOutline;

import com.google.android.gms.maps.model.MarkerOptions;

public interface OnFinishedListener {
	void onFinished(List<MarkerOptions> markerOptions);
	void onDataFinished(List<BabysitterOutline> outlines);
}
