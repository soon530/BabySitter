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


	public BabysitterListModelImpl(
			BabysitterListPresenterImpl babysitterListPresenterImpl) {
		mBabysitterListPresenterImpl = babysitterListPresenterImpl;
	}

	@Override
	public void doListQuery() {
	}

	@Override
	public BabysitterOutline getOutline(int position) {
		return null;
	}

}
