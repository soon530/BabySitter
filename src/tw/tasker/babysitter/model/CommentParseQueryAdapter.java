package tw.tasker.babysitter.model;

import tw.tasker.babysitter.R;
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

public class CommentParseQueryAdapter extends
		ParseQueryAdapter<BabysitterComment> {

	ImageView babysitterImage;
	TextView babysitterCommentTitle;
	TextView babysitterComment;
	RatingBar babysitterRating;

	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public CommentParseQueryAdapter(Context context,
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
			view = View.inflate(getContext(),
					R.layout.list_item_babysitter_comment, null);
		}

		initUI(view);
		fillDataToUI(comment);

		return view;
	};

	private void initUI(View view) {
		babysitterImage = (ImageView) view.findViewById(R.id.user_avator);

		babysitterCommentTitle = (TextView) view
				.findViewById(R.id.babysitter_comment_title);

		babysitterComment = (TextView) view
				.findViewById(R.id.babysitter_comment);

		babysitterRating = (RatingBar) view
				.findViewById(R.id.babysitter_rating);
	}

	private void fillDataToUI(BabysitterComment comment) {
		//babysitterImage.setBackgroundResource(R.drawable.ic_launcher);
		babysitterCommentTitle.setText(comment.getTitle());
		babysitterComment.setText(comment.getComment());
		babysitterRating.setRating(comment.getRating());
		
		imageLoader
		.displayImage(
				"https://lh3.googleusercontent.com/-3ett5vaAVZc/AAAAAAAAAAI/AAAAAAAACSQ/BUns79OwRrI/s120-c/photo.jpg",
				babysitterImage, options, null);

	}

}
