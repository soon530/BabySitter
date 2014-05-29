package tw.tasker.babysitter.model;

import java.text.SimpleDateFormat;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabysitterComment;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.ParseQueryAdapter;

public class RecordParseQueryAdapter extends
		ParseQueryAdapter<BabysitterComment> {
	ImageView userAvator;
	TextView babyRecordTitle;
	TextView babyRecord;
	TextView babyHeart;

	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private TextView createDate;

	public RecordParseQueryAdapter(Context context,
			ParseQueryAdapter.QueryFactory<BabysitterComment> factory) {
		super(context, factory);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

	}

	@Override
	public View getItemView(BabysitterComment comment, View view,
			ViewGroup parent) {

		if (view == null) {
			view = View.inflate(getContext(), R.layout.list_item_baby_record,
					null);
		}

		initUI(view);
		fillDataToUI(comment);

		return view;
	};

	private void initUI(View view) {
		userAvator = (ImageView) view.findViewById(R.id.user_avator);

		babyRecordTitle = (TextView) view.findViewById(R.id.baby_record_title);

		/*
		 * babyHeart = (TextView) view .findViewById(R.id.heart);
		 */
		createDate = (TextView) view.findViewById(R.id.create_date);

		babyRecord = (TextView) view.findViewById(R.id.baby_record);

	}

	private void fillDataToUI(BabysitterComment comment) {
		// userAvator.setBackgroundResource(R.drawable.ic_launcher);
		babyRecordTitle.setText(comment.getTitle());
		babyRecord.setText(comment.getComment());
		// babyHeart.setText("愛心 +" + comment.getRating());

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		String now = formatter.format(comment.getCreatedAt());

		createDate.setText(now);

		String url;
		//if (comment.getPhotoFile().isDataAvailable()) {
		if(comment.getPhotoFile() != null) {
			url = comment.getPhotoFile().getUrl();
		} else {
			url = "https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-ash2/t1.0-9/377076_10150391287099790_1866039278_n.jpg";
		}

		imageLoader.displayImage(url, userAvator, options, null);

	}

}
