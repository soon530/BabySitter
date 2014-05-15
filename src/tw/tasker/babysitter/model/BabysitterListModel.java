package tw.tasker.babysitter.model;

public interface BabysitterListModel {
	void doListQuery();

	BabysitterOutline getOutline(int position);
}
