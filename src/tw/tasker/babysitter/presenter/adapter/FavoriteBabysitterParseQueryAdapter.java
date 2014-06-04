package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.Favorite;
import tw.tasker.babysitter.model.data.FavoriteBabysitter;
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
import com.parse.ParseUser;

public class FavoriteBabysitterParseQueryAdapter extends ParseQueryAdapter<FavoriteBabysitter> {
	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public FavoriteBabysitterParseQueryAdapter(Context context) {
		super(context, getQueryFactory());
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	@Override
	public View getItemView(FavoriteBabysitter favorite, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.list_item_babysitter_list, null);
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

		Babysitter babysitter = favorite.getBabysitter();
		
		babysitterName.setText(babysitter.getText());
		babysitterAddress.setText(babysitter.getAddress());

		imageLoader
				.displayImage(
						"http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg",
						babysitterImage, options, null);

		int totalRatingValue = babysitter.getTotalRating();
		int totalComementValue = babysitter.getTotalComment();

		totalComment
				.setText("共有：" + String.valueOf(totalComementValue) + "筆評論");
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

	private static ParseQueryAdapter.QueryFactory<FavoriteBabysitter> getQueryFactory() {
		ParseQueryAdapter.QueryFactory<FavoriteBabysitter> factory = new ParseQueryAdapter.QueryFactory<FavoriteBabysitter>() {
			public ParseQuery<FavoriteBabysitter> create() {
				ParseQuery<FavoriteBabysitter> query = FavoriteBabysitter.getQuery();
				query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				query.setLimit(20);
				query.include("babysitter");
				return query;
			}
		};
		return factory;
	}

	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}

		return v;
	}
}
