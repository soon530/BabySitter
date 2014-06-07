package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.BabyFavorite;
import tw.tasker.babysitter.view.card.BabyGridCard;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class BabyFavoriteParseQueryAdapter extends
		ParseQueryAdapter<BabyFavorite> {

	public BabyFavoriteParseQueryAdapter(Context context) {
		super(context, getQueryFactory());

	}

	@Override
	public View getItemView(BabyFavorite favorite, View view, ViewGroup parent) {

		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}

		Baby baby = favorite.getBaby();

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

		/*
		 * ImageView babyAvator = (ImageView)
		 * view.findViewById(R.id.baby_avator); TextView babyName = (TextView)
		 * view.findViewById(R.id.baby_name); TextView babyNote = (TextView)
		 * view.findViewById(R.id.baby_note); TextView totalFavorite =
		 * (TextView) view .findViewById(R.id.total_favorite); TextView
		 * totalRecord = (TextView) view.findViewById(R.id.total_record);
		 * 
		 * Baby baby = favorite.getBaby(); String url; if (baby.getPhotoFile()
		 * != null) { url = baby.getPhotoFile().getUrl(); } else { url =
		 * "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg"
		 * ; } imageLoader.displayImage(url, babyAvator, options, null);
		 * 
		 * String tag = ""; if (baby.getIsPublic()) { tag = "公開"; } else { tag =
		 * "私藏"; }
		 * 
		 * babyName.setText(baby.getName() + " (" + tag + ")");
		 * babyNote.setText(baby.getNote()); totalFavorite.setText("最愛：+" +
		 * baby.getFavorite()); totalRecord.setText("記錄：+5");
		 */
		return view;
	}

	private static ParseQueryAdapter.QueryFactory<BabyFavorite> getQueryFactory() {
		ParseQueryAdapter.QueryFactory<BabyFavorite> factory = new ParseQueryAdapter.QueryFactory<BabyFavorite>() {
			public ParseQuery<BabyFavorite> create() {
				ParseQuery<BabyFavorite> query = BabyFavorite.getQuery();
				query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				// query.setLimit(20);
				query.include("baby");
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
