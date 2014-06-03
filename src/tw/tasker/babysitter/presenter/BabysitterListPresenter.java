package tw.tasker.babysitter.presenter;

public interface BabysitterListPresenter {
	void onCreate();

	void onListItemClick(int position);

	void refresh();
}
