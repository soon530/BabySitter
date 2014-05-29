package tw.tasker.babysitter.model;

import tw.tasker.babysitter.model.data.BabysitterOutline;

public interface BabysitterListModel {
	void doListQuery();

	BabysitterOutline getOutline(int position);
}
