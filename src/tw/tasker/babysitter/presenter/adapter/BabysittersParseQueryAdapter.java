package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysittersParseQueryAdapter extends ParseQueryAdapter<Babysitter> {

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

		BabysitterGridCard mCard = new BabysitterGridCard(getContext());
		mCard.setBabysitter(babysitter);
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

	private static ParseQueryAdapter.QueryFactory<Babysitter> getQueryFactory() {
		ParseQueryAdapter.QueryFactory<Babysitter> factory = new ParseQueryAdapter.QueryFactory<Babysitter>() {
			public ParseQuery<Babysitter> create() {
				ParseQuery<Babysitter> query = Babysitter.getQuery();
				query.orderByDescending("createdAt");
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
