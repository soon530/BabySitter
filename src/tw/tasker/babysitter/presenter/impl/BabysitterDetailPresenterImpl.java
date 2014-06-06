package tw.tasker.babysitter.presenter.impl;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.model.BabysitterDetailModel;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.impl.BabysitterDetailModelImpl;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenter;
import tw.tasker.babysitter.presenter.adapter.CommentParseQueryAdapter;
import tw.tasker.babysitter.view.activity.BabyDiaryActivity;
import tw.tasker.babysitter.view.fragment.BabysitterFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabysitterDetailPresenterImpl implements BabysitterDetailPresenter, OnQueryLoadListener<BabysitterComment> {

	private BabysitterFragment mView;
	private BabysitterDetailModel mModel;
	ParseQueryAdapter<BabysitterComment> mAdapter;
	
	
	public BabysitterDetailPresenterImpl(
			BabysitterFragment babysitterDetailFragment) {
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
	public void seeBabyDetail(String objectId) {
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABYSITTER_OBJECT_ID, objectId);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		//intent.setClass(mView.getActivity(), BabyDetailActivity.class);
		intent.setClass(mView.getActivity(), BabyDiaryActivity.class);
		mView.startActivity(intent);
	}

	@Override
	public void makePhoneCall(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		mView.startActivity(intent);
	}

	@Override
	public void doDetailQuery(String babysitterObjectId) {
		mModel.doDetailQuery(babysitterObjectId);
	}

	@Override
	public void doCommentQuery(String babysitterObjectId) {
		mAdapter = new CommentParseQueryAdapter(mView.getActivity(), babysitterObjectId);
		mAdapter.setAutoload(false);
		//mAdapter.setPaginationEnabled(false);
		mAdapter.setObjectsPerPage(5);
		mAdapter.addOnQueryLoadListener(this);
		mAdapter.loadObjects();
	}

	@Override
	public void onLoading() {
		mView.showProgress();
	}

	@Override
	public void onLoaded(List<BabysitterComment> babysitterComment, Exception e) {
		//mView.setCommentData(mAdapter);
		mView.hideProgress();
	}

	public void fillHeaderUI(Babysitter outline) {
		mView.fillHeaderUI(outline);
	};
	
	
	@Override
	public void refresh() {
		mAdapter.loadObjects();
	}
}
