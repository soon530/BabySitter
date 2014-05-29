package tw.tasker.babysitter.model.impl;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.data.BabysitterOutline;
import tw.tasker.babysitter.presenter.adapter.BabysitterListParseQueryAdapter;
import tw.tasker.babysitter.presenter.impl.BabysitterListPresenterImpl;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.QueryFactory;

public class BabysitterListModelImpl implements BabysitterListModel {

	// Maximum results returned from a Parse query
	//private static final int MAX_POST_SEARCH_RESULTS = 20;
	private BabysitterListPresenterImpl mBabysitterListPresenterImpl;
	private Context mContext;
	private ParseQueryAdapter<BabysitterOutline> mAdapter;


	public BabysitterListModelImpl(
			BabysitterListPresenterImpl babysitterListPresenterImpl,
			Context applicationContext) {
		mBabysitterListPresenterImpl = babysitterListPresenterImpl;
		mContext = applicationContext;
	}

	@Override
	public void doListQuery() {
		

		mAdapter = new BabysitterListParseQueryAdapter(mContext);

		// Disable automatic loading when the list_item_babysitter_comment is
		// attached to a view.
		mAdapter.setAutoload(false);

		// Disable pagination, we'll manage the query limit ourselves
		mAdapter.setPaginationEnabled(false);

		mBabysitterListPresenterImpl.setAdapter(mAdapter);

		mAdapter.loadObjects();
	}



	@Override
	public BabysitterOutline getOutline(int position) {
		return mAdapter.getItem(position);
	}

}
