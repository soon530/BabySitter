package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.view.card.BabyGridCard;
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

public class BabyDiaryParseQueryAdapter extends ParseQueryAdapter<BabyDiary> {

	public BabyDiaryParseQueryAdapter(Context context, String babysitterObjectId) {
		super(context, getQueryFactory(babysitterObjectId));
	}

	@Override
	public View getItemView(BabyDiary baby, View view, ViewGroup parent) {
		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}
		
		BabyGridCard mCard = new BabyGridCard(getContext());
		mCard.setBaby(baby);
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

		
		
/*		ImageView babyAvator = (ImageView) view.findViewById(R.id.baby_avator);
		TextView babyName = (TextView) view.findViewById(R.id.baby_name);
		TextView babyNote = (TextView) view.findViewById(R.id.baby_note);
		TextView totalFavorite = (TextView) view
				.findViewById(R.id.total_favorite);
		TextView totalRecord = (TextView) view.findViewById(R.id.total_record);

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
*/		return view;
	}

	private static ParseQueryAdapter.QueryFactory<BabyDiary> getQueryFactory(final String babysitterObjectId) {
		ParseQueryAdapter.QueryFactory<BabyDiary> factory = new ParseQueryAdapter.QueryFactory<BabyDiary>() {
			public ParseQuery<BabyDiary> create() {
				ParseQuery<BabyDiary> query = BabyDiary.getQuery();
				query.orderByDescending("createdAt");
				//query.setLimit(20);
				//query.include("baby");
				
				if (babysitterObjectId != null) {
					query.whereEqualTo("babysitterId", babysitterObjectId);
				}
				
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
