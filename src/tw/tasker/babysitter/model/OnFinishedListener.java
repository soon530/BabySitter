package tw.tasker.babysitter.model;

import java.util.List;

import com.google.android.gms.maps.model.MarkerOptions;

public interface OnFinishedListener {
	void onFinished(List<MarkerOptions> markerOptions);
	void onDataFinished(List<BabysitterOutline> outlines);
}
