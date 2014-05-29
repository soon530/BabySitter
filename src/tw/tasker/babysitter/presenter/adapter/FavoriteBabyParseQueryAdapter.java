package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.Favorite;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class FavoriteBabyParseQueryAdapter extends ParseQueryAdapter<Favorite> {
	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public FavoriteBabyParseQueryAdapter(Context context) {
		super(context, getQueryFactory());
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	@Override
	public View getItemView(Favorite post, View view, ViewGroup parent) {
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

		Baby baby = post.getBaby();

		babysitterName.setText(baby.getName() + baby.getObjectId());
		babysitterAddress.setText(baby.getNote());

		String url;
		if (baby.getPhotoFile() != null) {
			url = baby.getPhotoFile().getUrl();
		} else {
			url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
		}

		imageLoader.displayImage(url, babysitterImage, options, null);

		return view;
	}

	private static ParseQueryAdapter.QueryFactory<Favorite> getQueryFactory() {
		// Set up a customized query
		ParseQueryAdapter.QueryFactory<Favorite> factory = new ParseQueryAdapter.QueryFactory<Favorite>() {
			public ParseQuery<Favorite> create() {
				// Location myLoc = (currentLocation == null) ? lastLocation
				// :
				// currentLocation;
				ParseQuery<Favorite> query = Favorite.getQuery();
				query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				// query.whereWithinKilometers("location",
				// geoPointFromLocation(myLoc), radius * METERS_PER_FEET /
				// METERS_PER_KILOMETER);
				query.setLimit(20);
				query.include("baby");
				return query;
			}
		};
		return factory;
	}

}
