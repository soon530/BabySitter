package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyFavorite;
import tw.tasker.babysitter.view.card.BabyFavoriteGridCard;
import tw.tasker.babysitter.view.fragment.BabyFavoriteFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class BabyFavoriteParseQueryAdapter extends
		ParseQueryAdapter<BabyFavorite> {

	private Fragment mFragment;

	public BabyFavoriteParseQueryAdapter(Context context, Fragment fragment) {
		super(context, getQueryFactory(context));
		mFragment = fragment;
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

		//BabyDiary baby = favorite.getBabyDiary();

		BabyFavoriteGridCard mCard = new BabyFavoriteGridCard(getContext());
		mCard.setBabyFavorite(favorite);
		mCard.setFragment(mFragment);
		mCard.setAdapter(this);
		mCard.init();

		CardView mCardView;
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

	private static ParseQueryAdapter.QueryFactory<BabyFavorite> getQueryFactory(final Context context) {
		ParseQueryAdapter.QueryFactory<BabyFavorite> factory = new ParseQueryAdapter.QueryFactory<BabyFavorite>() {
			public ParseQuery<BabyFavorite> create() {
				ParseQuery<BabyFavorite> query = BabyFavorite.getQuery();
				query.include("user");
				query.include("BabyDiary");
				query.include("BabyDiary.BabyRecord");
				//query.include("BabyRecord");
				query.orderByDescending("createdAt");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				
/*				if (!DisplayUtils.hasNetwork(context)) {
					query.fromLocalDatastore();
				}
*/				return query;
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
