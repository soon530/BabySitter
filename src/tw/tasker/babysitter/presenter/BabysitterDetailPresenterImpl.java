package tw.tasker.babysitter.presenter;

import java.util.List;

import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.model.BabysitterDetailModel;
import tw.tasker.babysitter.model.BabysitterDetailModelImpl;
import tw.tasker.babysitter.model.BabysitterOutline;
import tw.tasker.babysitter.model.CommentParseQueryAdapter;
import tw.tasker.babysitter.view.BabyDetailActivity;
import tw.tasker.babysitter.view.BabysitterDetailFragment;
import android.content.Intent;
import android.net.Uri;

import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabysitterDetailPresenterImpl implements BabysitterDetailPresenter, OnQueryLoadListener<BabysitterComment> {

	private BabysitterDetailFragment mView;
	private BabysitterDetailModel mModel;
	ParseQueryAdapter<BabysitterComment> mCommentAdapter;
	
	
	public BabysitterDetailPresenterImpl(
			BabysitterDetailFragment babysitterDetailFragment) {
		mView = babysitterDetailFragment;
		mModel = new BabysitterDetailModelImpl(this);
	}

	@Override
	public void doDirections(String targetLat, String targetLng) {
		Intent intent;

		// String currentLattitude = "22.635725";
		// String currentLongitude = "120.377175";
		// String targetLat = "22.634599";
		// String targetLang = "120.350349";
		// String url =
		// "http://maps.google.com/maps?saddr="+currentLattitude+","+currentLongitude+"&daddr="+targetLat+","+targetLang;
		// String url =
		// "http://maps.google.com/maps?saddr="+mSlat+","+mSlng+"&daddr="+mDlat+","+mDlng;
		String url = "geo:0,0?q=" + targetLat + "," + targetLng;

		intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		mView.startActivity(intent);

	}

	@Override
	public void seeBabyDetail() {
		Intent intent = new Intent();
		intent.setClass(mView.getActivity(), BabyDetailActivity.class);
		mView.startActivity(intent);
	}

	@Override
	public void makePhoneCall(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		mView.startActivity(intent);
	}

	@Override
	public void doDetailQuery(String objectId) {
		mModel.doDetailQuery(objectId);
	}

	@Override
	public void doCommentQuery(String objectId) {
	
		mCommentAdapter = new CommentParseQueryAdapter(mView.getActivity(), mModel.getFactory(objectId));

		// Disable automatic loading when the list_item_babysitter_comment is
		// attached to a view.
		mCommentAdapter.setAutoload(false);

		// Disable pagination, we'll manage the query limit ourselves
		mCommentAdapter.setPaginationEnabled(false);

		mCommentAdapter.addOnQueryLoadListener(this);
		
		mCommentAdapter.loadObjects();
	}

	@Override
	public void onLoaded(List<BabysitterComment> arg0, Exception arg1) {
		mView.setCommentData(mCommentAdapter);
	}

	@Override
	public void onLoading() {
		// TODO Auto-generated method stub
		
	}

	public void fillHeaderUI(BabysitterOutline outline) {
		mView.fillHeaderUI(outline);
	};
}