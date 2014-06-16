package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class BaseFragment extends Fragment implements OnRefreshListener, OnItemClickListener {
	protected PullToRefreshLayout mPullToRefreshLayout;

	protected GridView mGridView;
	protected ListView mListView;
	protected TextView mEmpty;

	private boolean mIsGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mIsGridView = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return loadGridView(inflater, container);
	}
	
	public View loadGridView(LayoutInflater inflater, ViewGroup container) {
		View rootView = null;
		if (mIsGridView) {
			rootView = inflater.inflate(R.layout.fragment_grid, container, false);
			mGridView = (GridView) rootView.findViewById(R.id.grid);
			mGridView.setOnItemClickListener(this);
		}else{
			rootView = inflater.inflate(R.layout.fragment_list, container, false);
			mListView = (ListView) rootView.findViewById(R.id.list);
			mListView.setOnItemClickListener(this);
		}
		
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

	protected void loadGridView() {
		mIsGridView = true;
	}
	
	@Override
	public void onRefreshStarted(View arg0) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
}
