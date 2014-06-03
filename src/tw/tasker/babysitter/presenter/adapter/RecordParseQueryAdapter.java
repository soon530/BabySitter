package tw.tasker.babysitter.presenter.adapter;

import java.text.SimpleDateFormat;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.utils.DateTimeUtils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class RecordParseQueryAdapter extends ParseQueryAdapter<BabyRecord> {
	ImageView mUserAvator;
	TextView mTitle;
	TextView mDescription;
	private TextView mCreateDate;

	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public RecordParseQueryAdapter(Context context, String babyObejctId) {
		super(context, getFactory(babyObejctId));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

	}

	public static ParseQueryAdapter.QueryFactory<BabyRecord> getFactory(
			final String babyObjectId) {
		ParseQueryAdapter.QueryFactory<BabyRecord> factory = new ParseQueryAdapter.QueryFactory<BabyRecord>() {
			public ParseQuery<BabyRecord> create() {
				ParseQuery<BabyRecord> query = BabyRecord.getQuery();
				query.orderByDescending("createdAt");
				Baby baby = ParseObject.createWithoutData(Baby.class, babyObjectId);
				query.whereEqualTo("baby", baby);
				query.setLimit(20);
				return query;
			}
		};
		return factory;
	}

	public View getItemView(BabyRecord comment, View view, ViewGroup parent) {

		if (view == null) {
			view = View.inflate(getContext(), R.layout.list_item_baby_record,
					null);
		}

		initUI(view);
		fillDataToUI(comment);

		return view;
	};

	private void initUI(View view) {
		mUserAvator = (ImageView) view.findViewById(R.id.user_avator);
		mTitle = (TextView) view.findViewById(R.id.baby_record_title);
		mCreateDate = (TextView) view.findViewById(R.id.create_date);
		mDescription = (TextView) view.findViewById(R.id.baby_record);
	}

	private void fillDataToUI(BabyRecord babyrecord) {
		mTitle.setText(babyrecord.getTitle());
		mDescription.setText(babyrecord.getDescription());

		String now = DateTimeUtils.show(babyrecord.getCreatedAt());
		mCreateDate.setText(now);

		String url;
		if (babyrecord.getPhotoFile() != null) {
			url = babyrecord.getPhotoFile().getUrl();
		} else {
			url = "https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-ash2/t1.0-9/377076_10150391287099790_1866039278_n.jpg";
		}
		imageLoader.displayImage(url, mUserAvator, options, null);
	}
}
