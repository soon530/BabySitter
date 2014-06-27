package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class BabysitterFavoriteParseQueryAdapter extends
		ParseQueryAdapter<BabysitterFavorite> {

	public BabysitterFavoriteParseQueryAdapter(Context context) {
		super(context, getQueryFactory(context));
	}

	@Override
	public View getItemView(BabysitterFavorite favorite, View view,
			ViewGroup parent) {

		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}

		Babysitter babysitter = favorite.getBabysitter();

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
			mCardView.setCard(mCard);
		}

		return view;
	}


	private static ParseQueryAdapter.QueryFactory<BabysitterFavorite> getQueryFactory(final Context context) {
		ParseQueryAdapter.QueryFactory<BabysitterFavorite> factory = new ParseQueryAdapter.QueryFactory<BabysitterFavorite>() {
			public ParseQuery<BabysitterFavorite> create() {
				ParseQuery<BabysitterFavorite> query = BabysitterFavorite
						.getQuery();
				query.include("user");
				query.include("Babysitter");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				query.orderByDescending("createdAt");
				
/*				if (!DisplayUtils.hasNetwork(context)) {
					query.fromLocalDatastore();
				}
*/
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
