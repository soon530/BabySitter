package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.view.card.BabyGridCard;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class BabyDiaryParseQueryAdapter extends ParseQueryAdapter<BabyDiary> {

	private Fragment mFragment;

	public BabyDiaryParseQueryAdapter(Context context, String babysitterObjectId, Fragment fragment, int position) {
		super(context, getQueryFactory(babysitterObjectId, context, position));
		mFragment = fragment;
	}

	@Override
	public View getItemView(BabyDiary babyDiary, View view, ViewGroup parent) {
		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}
		
		BabyGridCard mCard = new BabyGridCard(getContext());
		mCard.setBaby(babyDiary);
		mCard.setFragment(mFragment);
		mCard.setAdapter(this);
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
		return view;
	}

	private static ParseQueryAdapter.QueryFactory<BabyDiary> getQueryFactory(final String babysitterObjectId, final Context context,final int position) {
		ParseQueryAdapter.QueryFactory<BabyDiary> factory = new ParseQueryAdapter.QueryFactory<BabyDiary>() {
			public ParseQuery<BabyDiary> create() {
				ParseQuery<BabyDiary> query = BabyDiary.getQuery();
				query.include("BabyRecord");
				query.include("user");
				
				if (position == 0) {
					query.whereEqualTo("isPublic", true);
				} else {
					query.whereEqualTo("isPublic", false);
					query.whereEqualTo("user", ParseUser.getCurrentUser());
				}
				
				// 如果是從保母點過來的[寶寶日記]
				if (babysitterObjectId != null) {
					Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class, babysitterObjectId);
					query.whereEqualTo("Babysitter", babysitter);
				}
				query.orderByDescending("totalRecord");
				//query.addDescendingOrder("createdAt");
				
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
