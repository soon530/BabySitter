package tw.tasker.babysitter.model;

import tw.tasker.babysitter.model.data.Babysitter;

import com.parse.ParseQueryAdapter;

public interface OnBabysitterListFinishedListener {
	void onFinished(ParseQueryAdapter<Babysitter> adapter);
}
