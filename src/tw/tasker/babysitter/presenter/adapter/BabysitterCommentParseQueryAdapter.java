package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.card.GplayCardCustomSource;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysitterCommentParseQueryAdapter extends
		ParseQueryAdapter<BabysitterComment> {

	ImageView babysitterImage;
	TextView babysitterCommentTitle;
	TextView babysitterComment;
	RatingBar babysitterRating;

	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private TextView createDate;

	public BabysitterCommentParseQueryAdapter(Context context, String babysitterObjectId) {
		super(context, getFactory(babysitterObjectId, context));
/*		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
*/
	}
	
	public static ParseQueryAdapter.QueryFactory<BabysitterComment> getFactory(
			final String babysitterObjectId, final Context context ) {
		ParseQueryAdapter.QueryFactory<BabysitterComment> factory = new ParseQueryAdapter.QueryFactory<BabysitterComment>() {
			public ParseQuery<BabysitterComment> create() {
				ParseQuery<BabysitterComment> query = BabysitterComment
						.getQuery();
				query.orderByDescending("createdAt");
				Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class, babysitterObjectId);
				query.whereEqualTo("Babysitter", babysitter);
				//query.setLimit(20);
/*				if (!DisplayUtils.hasNetwork(context)) {
					query.fromLocalDatastore();
					LogUtils.LOGD("vic", "babysitter comment fromLocalDatastore()");
				}
*/				return query;
			}
		};
		return factory;
	}

	@Override
	public View getItemView(BabysitterComment comment, View view,
			ViewGroup parent) {

		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_card, null);
		} else {
			recycle = true;
		}
		
		GplayCardCustomSource mCard = new GplayCardCustomSource(getContext());
		mCard.setBabysitterComment(comment);
		mCard.init();
		CardView mCardView;
		mCardView = (CardView) view.findViewById(R.id.carddemo_thumb_customsource);
		if (mCardView != null) {
			// It is important to set recycle value for inner layout elements
			mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(
					mCardView.getCard(), mCard));

			// It is important to set recycle value for performance issue
			mCardView.setRecycle(recycle);
			mCardView.setCard(mCard);
		}

		
		
		//initUI(view);
		//fillDataToUI(comment);

		return view;
	};

/*	private void initUI(View view) {
		babysitterImage = (ImageView) view.findViewById(R.id.user_avator);

		createDate = (TextView) view.findViewById(R.id.create_date);

		babysitterCommentTitle = (TextView) view
				.findViewById(R.id.babysitter_comment_title);

		babysitterComment = (TextView) view
				.findViewById(R.id.babysitter_comment);

		babysitterRating = (RatingBar) view
				.findViewById(R.id.babysitter_rating);
	}
*/
/*	private void fillDataToUI(BabysitterComment comment) {
		babysitterCommentTitle.setText(comment.getTitle());
		babysitterComment.setText(comment.getComment());
		babysitterRating.setRating(comment.getRating());

		String now = DisplayUtils.show(comment.getCreatedAt());		
		createDate.setText(now);

		imageLoader
				.displayImage(
						"https://lh3.googleusercontent.com/-3ett5vaAVZc/AAAAAAAAAAI/AAAAAAAACSQ/BUns79OwRrI/s120-c/photo.jpg",
						babysitterImage, options, null);

	}
*/	
	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}

		return v;
	}
	
	

}
