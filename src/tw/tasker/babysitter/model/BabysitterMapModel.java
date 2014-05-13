package tw.tasker.babysitter.model;

import android.location.Location;

public interface BabysitterMapModel {
	//void setOnFinishedListener(OnFinishedListener finishedListener);
	
	void doMapQuery(Location location);
}
