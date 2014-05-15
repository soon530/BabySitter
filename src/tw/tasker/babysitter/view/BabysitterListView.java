package tw.tasker.babysitter.view;

import android.widget.BaseAdapter;

public interface BabysitterListView {
	void showProgress();
	void hideProgress();
	void setAdapter(BaseAdapter adapter);
}
