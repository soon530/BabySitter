package tw.tasker.babysitter.model;

import tw.tasker.babysitter.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

public class CommentParseQueryAdapter extends
		ParseQueryAdapter<BabysitterComment> {

	ImageView babysitterImage;
	TextView babysitterCommentTitle;
	TextView babysitterComment;
	RatingBar babysitterRating;

	public CommentParseQueryAdapter(Context context,
			ParseQueryAdapter.QueryFactory<BabysitterComment> factory) {
		super(context, factory);
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
		babysitterImage.setBackgroundResource(R.drawable.ic_launcher);
		babysitterCommentTitle.setText(comment.getTitle());
		babysitterComment.setText(comment.getComment());
		babysitterRating.setRating(comment.getRating());
	}

}
