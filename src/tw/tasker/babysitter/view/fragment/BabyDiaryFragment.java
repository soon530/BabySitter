package tw.tasker.babysitter.view.fragment;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.presenter.adapter.BabyDiaryParseQueryAdapter;
import tw.tasker.babysitter.view.activity.BabyRecordActivity;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;

public class BabyDiaryFragment extends BaseFragment implements
		OnQueryLoadListener<BabyDiary> {
	private int mPosition;
	private ParseQueryAdapter<BabyDiary> mAdapter;
	private String mBabysitterObjectId;

	public BabyDiaryFragment(int position) {
		mPosition = position;
	}

	public static Fragment newInstance(int position) {
		BabyDiaryFragment fragment = new BabyDiaryFragment(position);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
			mBabysitterObjectId = getArguments().getString(
					Config.BABYSITTER_OBJECT_ID);
		}

		// if (mBabysitterObjectId != null) {
		// setHasOptionsMenu(true);
		// }
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_add:
			if (ParseUser.getCurrentUser() == null) { // 沒有登入
				Intent intent = new Intent();
				// Start and intent for the dispatch activity
				intent.setClass(getActivity(), DispatchActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else { // 有登入
				addBabyDiary();
			}
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void addBabyDiary() {
		DialogFragment newFragment = new AddDialogFragment(mBabysitterObjectId,
				mAdapter);
		newFragment.show(getFragmentManager(), "dialog");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		doListQuery();
	}

	public void doListQuery() {
		mAdapter = new BabyDiaryParseQueryAdapter(getActivity(),
				mBabysitterObjectId, this, mPosition);
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		mAdapter.addOnQueryLoadListener(this);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BabyDiary babyDiary = mAdapter.getItem(position);
		if (babyDiary.getIsPublic()) {
			seeBabyDetail(babyDiary.getObjectId());
		}
	}

	public void seeBabyDetail(String babyObjectId) {
		Bundle bundle = new Bundle();
		bundle.putString(Config.BABY_OBJECT_ID, babyObjectId);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(getActivity(), BabyRecordActivity.class);
		startActivity(intent);
	}

	@Override
	public void onLoading() {
		showLoading();
	}

	@Override
	public void onLoaded(List<BabyDiary> babyDiaries, Exception arg1) {
		/*
		 * if (DisplayUtils.hasNetwork(getActivity())) {
		 * ParseObject.pinAllInBackground(babyDiaries); }
		 */
		hideLoading();
	}

	@Override
	public void onRefreshStarted(View arg0) {
		mAdapter.loadObjects();
	}
}