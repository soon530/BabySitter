package tw.tasker.babysitter.model.impl;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.BabysitterListModel;
import tw.tasker.babysitter.model.data.BabysitterOutline;
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
	private static final int MAX_POST_SEARCH_RESULTS = 20;
	private BabysitterListPresenterImpl mBabysitterListPresenterImpl;
	private Context mContext;
	private ParseQueryAdapter<BabysitterOutline> mAdapter;

	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public BabysitterListModelImpl(
			BabysitterListPresenterImpl babysitterListPresenterImpl,
			Context applicationContext) {
		mBabysitterListPresenterImpl = babysitterListPresenterImpl;
		mContext = applicationContext;
	}

	@Override
	public void doListQuery() {
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20)).build();

		
		ParseQueryAdapter.QueryFactory<BabysitterOutline> factory = getQueryFactory();

		mAdapter = getParseQueryAdapter(factory);

		// Disable automatic loading when the list_item_babysitter_comment is
		// attached to a view.
		mAdapter.setAutoload(false);

		// Disable pagination, we'll manage the query limit ourselves
		mAdapter.setPaginationEnabled(false);

		mBabysitterListPresenterImpl.setAdapter(mAdapter);

		mAdapter.loadObjects();
	}

	private ParseQueryAdapter.QueryFactory<BabysitterOutline> getQueryFactory() {
		// Set up a customized query
		ParseQueryAdapter.QueryFactory<BabysitterOutline> factory = new ParseQueryAdapter.QueryFactory<BabysitterOutline>() {
			public ParseQuery<BabysitterOutline> create() {
				// Location myLoc = (currentLocation == null) ? lastLocation :
				// currentLocation;
				ParseQuery<BabysitterOutline> query = BabysitterOutline
						.getQuery();
				// query.include("user");
				query.orderByDescending("createdAt");
				// query.whereWithinKilometers("location",
				// geoPointFromLocation(myLoc), radius * METERS_PER_FEET /
				// METERS_PER_KILOMETER);
				query.setLimit(MAX_POST_SEARCH_RESULTS);
				return query;
			}
		};
		return factory;
	}

	private ParseQueryAdapter<BabysitterOutline> getParseQueryAdapter(
			QueryFactory<BabysitterOutline> factory) {
		ParseQueryAdapter<BabysitterOutline> adapter;
		// Set up the query list_item_babysitter_comment
		adapter = new ParseQueryAdapter<BabysitterOutline>(mContext, factory) {
			@Override
			public View getItemView(BabysitterOutline post, View view,
					ViewGroup parent) {
				if (view == null) {
					view = View.inflate(getContext(),
							R.layout.list_item_babysitter_list, null);
				}
				TextView babysitterName = (TextView) view
						.findViewById(R.id.babysitter_name);
				TextView babysitterAddress = (TextView) view
						.findViewById(R.id.babysitter_address);

				ImageView babysitterImage = (ImageView) view
						.findViewById(R.id.babysitter_avator);

				RatingBar babysitterRating = (RatingBar) view
						.findViewById(R.id.babysitter_rating);
				
				TextView totalComment = (TextView) view.findViewById(R.id.totalComment);

				babysitterName.setText(post.getText());
				babysitterAddress.setText(post.getAddress());
				//babysitterImage.setBackgroundResource(R.drawable.ic_launcher);
				
				imageLoader
				.displayImage(
						"http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg",
						babysitterImage, options, null);

				
				int totalRatingValue = post.getTotalRating();
				int totalComementValue = post.getTotalComment(); 
				
				totalComment.setText("共有：" + String.valueOf(totalComementValue) + "筆評論");
				float rating = getRatingValue(totalRatingValue, totalComementValue);
				babysitterRating.setRating(rating);
				
				return view;
			}

			private float getRatingValue(int totalRating, int totalComment) {
				float avgRating = 0.0f; 
				
				if (totalComment != 0) {
					avgRating = totalRating / totalComment;
				}
				return avgRating;
			}

		};
		return adapter;
	}

	@Override
	public BabysitterOutline getOutline(int position) {
		return mAdapter.getItem(position);
	}

}
