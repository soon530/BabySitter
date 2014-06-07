package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysittersParseQueryAdapter extends
		ParseQueryAdapter<Babysitter> {

	public BabysittersParseQueryAdapter(Context context) {
		super(context, getQueryFactory());
	}

	@Override
	public View getItemView(Babysitter babysitter, View view, ViewGroup parent) {
		
		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}

		//Babysitter babysitter = favorite.getBabysitter();

		BabysitterGridCard mCard = new BabysitterGridCard(getContext());
		mCard.setBabysitter(babysitter);
		mCard.init();

		CardView mCardView;

		// Setup card
		mCardView = (CardView) view.findViewById(R.id.list_cardId);
		if (mCardView != null) {
			// It is important to set recycle value for inner layout elements
			mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(
					mCardView.getCard(), mCard));

			// It is important to set recycle value for performance issue
			mCardView.setRecycle(recycle);

			// Save original swipeable to prevent cardSwipeListener (listView
			// requires another cardSwipeListener)
			// boolean origianlSwipeable = mCard.isSwipeable();
			// mCard.setSwipeable(false);

			mCardView.setCard(mCard);

			// Set originalValue
			// mCard.setSwipeable(origianlSwipeable);

		}

		
/*		if (view == null) {
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

		imageLoader
				.displayImage(
						"http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg",
						babysitterImage, options, null);

		int totalRatingValue = post.getTotalRating();
		int totalComementValue = post.getTotalComment();

		totalComment
				.setText("共有：" + String.valueOf(totalComementValue) + "筆評論");
		float rating = getRatingValue(totalRatingValue, totalComementValue);
		babysitterRating.setRating(rating);
*/
		return view;
	}

	private float getRatingValue(int totalRating, int totalComment) {
		float avgRating = 0.0f;

		if (totalComment != 0) {
			avgRating = totalRating / totalComment;
		}
		return avgRating;
	}

	private static ParseQueryAdapter.QueryFactory<Babysitter> getQueryFactory() {
		ParseQueryAdapter.QueryFactory<Babysitter> factory = new ParseQueryAdapter.QueryFactory<Babysitter>() {
			public ParseQuery<Babysitter> create() {
				ParseQuery<Babysitter> query = Babysitter.getQuery();
				query.orderByDescending("createdAt");
				// query.setLimit(20);
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
