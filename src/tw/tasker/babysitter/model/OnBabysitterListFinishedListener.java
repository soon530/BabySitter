package tw.tasker.babysitter.model;

import com.parse.ParseQueryAdapter;

public interface OnBabysitterListFinishedListener {
	void onFinished(ParseQueryAdapter<BabysitterOutline> adapter);
}
