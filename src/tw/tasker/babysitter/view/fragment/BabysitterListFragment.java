package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.presenter.BabysitterListPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterListPresenterImpl;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterListView;
import tw.tasker.babysitter.view.activity.BabysitterMapActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link BabysitterDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class BabysitterListFragment extends Fragment implements
		BabysitterListView, OnItemClickListener {

	private BabysitterListPresenter mPresenter;

	private GridView mList;
	private TextView mEmpty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mPresenter = new BabysitterListPresenterImpl(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.babysitter_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {
		case R.id.action_map:
			Intent intent = new Intent();
			intent.setClass(getActivity(), BabysitterMapActivity.class);
			startActivity(intent);
			break;

		case R.id.refresh:
			mPresenter.refresh();

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_baby_list,
				container, false);
		mList = (GridView) rootView.findViewById(R.id.list);
		mList.setOnItemClickListener(this);
		mEmpty = (TextView) rootView.findViewById(R.id.empty);
		mList.setEmptyView(mEmpty);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onCreate();
	}

	@Override
	public void showProgress() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void hideProgress() {
		ProgressBarUtils.hide(getActivity());
	}

	@Override
	public void setAdapter(BaseAdapter adapter) {
		mList.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mPresenter.onListItemClick(position);
	}
}
