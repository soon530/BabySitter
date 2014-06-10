package tw.tasker.babysitter.presenter;

public interface BabysitterDetailPresenter {
	void doDirections(double targetLat, double targetLng);

	void seeBabyDetail(String string);

	void makePhoneCall(String phoneNumber);

	void doDetailQuery(String objectId);

	void doCommentQuery(String objectId);

	void refresh();
}
