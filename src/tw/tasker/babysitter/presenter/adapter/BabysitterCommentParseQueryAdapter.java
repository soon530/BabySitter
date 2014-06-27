package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.view.card.BabysitterCommentCard;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysitterCommentParseQueryAdapter extends
		ParseQueryAdapter<BabysitterComment> {

	private Fragment mFragment;

	public BabysitterCommentParseQueryAdapter(Context context, String babysitterObjectId, Fragment fragment) {
		super(context, getFactory(babysitterObjectId, context));
		mFragment = fragment;
	}
	
	public static ParseQueryAdapter.QueryFactory<BabysitterComment> getFactory(
			final String babysitterObjectId, final Context context ) {
		ParseQueryAdapter.QueryFactory<BabysitterComment> factory = new ParseQueryAdapter.QueryFactory<BabysitterComment>() {
			public ParseQuery<BabysitterComment> create() {
				ParseQuery<BabysitterComment> query = BabysitterComment
						.getQuery();
				query.orderByDescending("createdAt");
				query.include("user");
				Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class, babysitterObjectId);
				query.whereEqualTo("Babysitter", babysitter);
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
		
		BabysitterCommentCard mCard = new BabysitterCommentCard(getContext());
		mCard.setBabysitterComment(comment);
		mCard.setFragment(mFragment);
		mCard.setAdapter(this);
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
		return view;
	};

	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}

		return v;
	}
}
