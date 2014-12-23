package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysittersParseQueryAdapter extends ParseQueryAdapter<Babysitter> {

	public BabysittersParseQueryAdapter(Context context, int position) {
		super(context, getQueryFactory(context, position));
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
		
        float distance = (float) babysitter.getLocation().distanceInKilometersTo(Config.MY_LOCATION);
        babysitter.setDistance(distance);
        
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

	private static ParseQueryAdapter.QueryFactory<Babysitter> getQueryFactory(final Context context, final int position) {
		ParseQueryAdapter.QueryFactory<Babysitter> factory = new ParseQueryAdapter.QueryFactory<Babysitter>() {
			public ParseQuery<Babysitter> create() {
				ParseQuery<Babysitter> query = Babysitter.getQuery();

				switch (position) {
				case 0:
					query.whereNear("location", Config.MY_LOCATION);
					break;
				case 1: //臨時保母
					query.whereContains("babycareTime", "臨時");
					query.whereNear("location", Config.MY_LOCATION);
					break;
				case 2:
					String keyword = Config.keyWord;
					keyword = keyword.replace("台", "臺");
					query.whereContains("address", keyword);
					break;
				case 3:
					query.orderByDescending("updatedAt");
				default:
					break;
				}
				
				
/*				if (!DisplayUtils.hasNetwork(context)) {
					LogUtils.LOGD("vic", "babysitters fromLocalDatastore()");
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
