package tw.tasker.babysitter.presenter.impl;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.impl.BabysitterListModelImpl;
import tw.tasker.babysitter.presenter.BabysitterListPresenter;
import tw.tasker.babysitter.presenter.adapter.BabysittersParseQueryAdapter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.activity.BabysitterActivity;
import tw.tasker.babysitter.view.fragment.BabysittersFragment;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class BabysitterListPresenterImpl implements BabysitterListPresenter,
		OnQueryLoadListener<Babysitter> {

	private BabysittersFragment mView;
	private BabysitterListModel mModel;
	private ParseQueryAdapter<Babysitter> mAdapter;

	public BabysitterListPresenterImpl(
			BabysittersFragment babysitterListFragment) {
		mView = babysitterListFragment;
		mModel = new BabysitterListModelImpl(this);
	}

	@Override
	public void onCreate() {
		doListQuery();
	}

	private void doListQuery() {
		
		
		ParseQuery<Babysitter> query = Babysitter.getQuery();
		query.orderByDescending("createdAt");
		query.fromLocalDatastore();
		int count = 0;
		try {
			count = query.count();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.LOGD("vic", "pin count: " + count);		
		
		if (count == 0) {
			ParseQuery<Babysitter> query2 = Babysitter.getQuery();
			query2.orderByDescending("createdAt");
			query2.findInBackground(new FindCallback<Babysitter>() {
				
				@Override
				public void done(List<Babysitter> babysitters, ParseException e) {
					try {
						ParseObject.pinAll(babysitters);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});

		}
		
		mAdapter = new BabysittersParseQueryAdapter(mView.getActivity());
		mAdapter.setAutoload(false);
		//mAdapter.setPaginationEnabled(false);
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		mAdapter.addOnQueryLoadListener(this);
		setAdapter(mAdapter);
		mAdapter.loadObjects();
	}

	public void setAdapter(BaseAdapter adapter) {
		mView.setAdapter(adapter);
	}

	@Override
	public void onListItemClick(int position) {
		Babysitter babysitter = mAdapter.getItem(position);
		Intent detailIntent = new Intent(mView.getActivity(),
				BabysitterActivity.class);
		detailIntent.putExtra(Config.BABYSITTER_OBJECT_ID,
				babysitter.getObjectId());
		mView.getActivity().startActivity(detailIntent);
	}

	@Override
	public void onLoading() {
		mView.showProgress();
	}

	@Override
	public void onLoaded(List<Babysitter> babysitter, Exception e) {
		mView.hideProgress();
	}

	@Override
	public void refresh() {
		mAdapter.loadObjects();
	}
}
