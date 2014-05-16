package tw.tasker.babysitter.presenter;

public interface BabysitterDetailPresenter {
	void doDirections(String targetLat, String targetLng);

	void seeBabyDetail();

	void makePhoneCall(String phoneNumber);

	void doDetailQuery(String objectId);

	void doCommentQuery(String objectId);
}
