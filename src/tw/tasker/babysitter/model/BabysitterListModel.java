package tw.tasker.babysitter.model;

import tw.tasker.babysitter.model.data.Babysitter;

public interface BabysitterListModel {
	void doListQuery();

	Babysitter getOutline(int position);
}
