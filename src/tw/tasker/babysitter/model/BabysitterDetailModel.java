package tw.tasker.babysitter.model;

import com.parse.ParseQueryAdapter;

public interface BabysitterDetailModel {

	void doDetailQuery(String objectId);

	ParseQueryAdapter.QueryFactory<BabysitterComment> getFactory(String objectId);

}
