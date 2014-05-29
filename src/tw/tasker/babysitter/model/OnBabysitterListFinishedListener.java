package tw.tasker.babysitter.model;

import tw.tasker.babysitter.model.data.BabysitterOutline;

import com.parse.ParseQueryAdapter;

public interface OnBabysitterListFinishedListener {
	void onFinished(ParseQueryAdapter<BabysitterOutline> adapter);
}
