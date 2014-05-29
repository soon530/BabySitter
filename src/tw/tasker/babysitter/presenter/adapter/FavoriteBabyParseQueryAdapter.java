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
	public View getItemView(Favorite favorite, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		}
		ImageView babyAvator = (ImageView) view.findViewById(R.id.baby_avator);
		TextView babyName = (TextView) view.findViewById(R.id.baby_name);
		TextView babyNote = (TextView) view.findViewById(R.id.baby_note);
		TextView totalFavorite = (TextView) view
				.findViewById(R.id.total_favorite);
		TextView totalRecord = (TextView) view.findViewById(R.id.total_record);

		Baby baby = favorite.getBaby();
		String url;
		if (baby.getPhotoFile() != null) {
			url = baby.getPhotoFile().getUrl();
		} else {
			url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
		}
		imageLoader.displayImage(url, babyAvator, options, null);

		String tag = "";
		if (baby.getIsPublic()) {
			tag = "公開";
		} else {
			tag = "私藏";
		}

		babyName.setText(baby.getName() + " (" + tag + ")");
		babyNote.setText(baby.getNote());
		totalFavorite.setText("最愛：+" + baby.getFavorite());
		totalRecord.setText("記錄：+5");

		return view;
	}

	private static ParseQueryAdapter.QueryFactory<Favorite> getQueryFactory() {
		ParseQueryAdapter.QueryFactory<Favorite> factory = new ParseQueryAdapter.QueryFactory<Favorite>() {
			public ParseQuery<Favorite> create() {
				ParseQuery<Favorite> query = Favorite.getQuery();
				query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				query.setLimit(20);
				query.include("baby");
				return query;
			}
		};
		return factory;
	}
}
