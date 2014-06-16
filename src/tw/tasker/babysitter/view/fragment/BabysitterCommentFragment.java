package tw.tasker.babysitter.view.fragment;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.presenter.adapter.BabysitterCommentParseQueryAdapter;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.BabysitterCommentActivity;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabysitterCommentFragment extends Fragment implements
		OnRefreshListener, OnQueryLoadListener<BabysitterComment> {

	private String mBabysitterObjectId;
	private PullToRefreshLayout mPullToRefreshLayout;
	private BabysitterCommentParseQueryAdapter mAdapter;

	public static Fragment newInstance(int position) {
		Fragment fragment = new BabysitterCommentFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
			mBabysitterObjectId = getArguments().getString(
					Config.BABYSITTER_OBJECT_ID);
		}

		setHasOptionsMenu(true);
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

/*			Intent intent = new Intent();

			Bundle bundle = new Bundle();
			bundle.putString(Config.BABYSITTER_OBJECT_ID, mBabysitterObjectId);
			//bundle.putInt(Config.TOTAL_RATING, mTotalRating);
			//bundle.putInt(Config.TOTAL_COMMENT, mTotalComment);
			intent.putExtras(bundle);

			intent.setClass(getActivity(), BabysitterCommentActivity.class);
			startActivity(intent);
*/			
			addBabysitterComment();
			
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void addBabysitterComment() {
		DialogFragment newFragment = new AddCommentDialogFragment(mBabysitterObjectId);
		newFragment.show(getFragmentManager(), "dialog");		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);

		// Retrieve the PullToRefreshLayout from the content view
		mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.carddemo_extra_ptr_layout);

		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initCards();
	}

	private void initCards() {

		mAdapter = new BabysitterCommentParseQueryAdapter(getActivity(),
				mBabysitterObjectId);
		mAdapter.addOnQueryLoadListener(this);
		ListView listView = (ListView) getActivity().findViewById(R.id.list);
		if (listView != null) {
			listView.setAdapter(mAdapter);
		}
	}

	@Override
	public void onLoading() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void onLoaded(List<BabysitterComment> babysitterComments, Exception e) {
		
/*		if (DisplayUtils.hasNetwork(getActivity())) {
			ParseObject.pinAllInBackground(babysitterComments);
			LogUtils.LOGD("vic", "babysitterComments pin size:" + babysitterComments.size());
		}
*/		
		ProgressBarUtils.hide(getActivity());
		// Notify PullToRefreshAttacher that the refresh has finished
		mPullToRefreshLayout.setRefreshComplete();
	}

	@Override
	public void onRefreshStarted(View view) {
		mAdapter.loadObjects();
	}
}
