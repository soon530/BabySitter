package tw.tasker.babysitter.model;

import com.parse.ParseGeoPoint;

public interface BabysitterMapModel {
	//void setOnFinishedListener(OnFinishedListener finishedListener);
	
	void doMapQuery(ParseGeoPoint parseGeoPoint);
}
