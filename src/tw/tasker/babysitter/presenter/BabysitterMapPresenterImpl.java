package tw.tasker.babysitter.presenter;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.BabysitterDetailActivity;
import tw.tasker.babysitter.view.BabysitterListActivity;
import tw.tasker.babysitter.view.BabysitterMapActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;

public class BabysitterMapPresenterImpl implements BabysitterMapPresenter {

	private BabysitterMapActivity mBabysitterMap;

	public BabysitterMapPresenterImpl(
			BabysitterMapActivity babysitterMapActivity) {
		mBabysitterMap = babysitterMapActivity;
	}

	@Override
	public void onOptionsItemSelected(int id) {
		if (id == R.id.action_list) {
			Intent intent = new Intent();
			intent.setClass(mBabysitterMap, BabysitterListActivity.class);
			mBabysitterMap.startActivity(intent);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		final Intent intent = new Intent();
		intent.setClass(mBabysitterMap, BabysitterDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("objectId", marker.getTitle());
		intent.putExtras(bundle);
		mBabysitterMap.startActivity(intent);
	}
}
