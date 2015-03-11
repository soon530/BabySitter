package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabysittersParseQueryAdapter extends ParseQueryAdapter<Babysitter> {

	public BabysittersParseQueryAdapter(Context context, int position) {
		super(context, getQueryFactory(context, position));
	}

	@Override
	public View getItemView(Babysitter babysitter, View view, ViewGroup parent) {
		//boolean recycle = false;
		View rootView;
		if (view == null) {
			//recycle = false;
			//rootView = View.inflate(getContext(), R.layout.list_item_sitter, null);
			LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView = mInflater.inflate(R.layout.list_item_sitter, parent, false);
			} else {
				rootView = view;
			//recycle = true;
		}
		
		TextView name = (TextView) rootView.findViewById(R.id.name);
		TextView address = (TextView) rootView.findViewById(R.id.address);
		TextView babycareTime = (TextView) rootView.findViewById(R.id.babycare_time);

		name.setText(babysitter.getName());
		address.setText(babysitter.getAddress());
		babycareTime.setText(babysitter.getBabycareTime());
        
		
		//float distance = (float) babysitter.getLocation().distanceInKilometersTo(Config.MY_LOCATION);
        //babysitter.setDistance(distance);
        
		//BabysitterGridCard mCard = new BabysitterGridCard(getContext());
		//mCard.setBabysitter(babysitter);
		//mCard.init();

		//CardView mCardView;
		//mCardView = (CardView) view.findViewById(R.id.list_cardId);
		//if (mCardView != null) {
			// It is important to set recycle value for inner layout elements
			//mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(
					//mCardView.getCard(), mCard));

			// It is important to set recycle value for performance issue
			//mCardView.setRecycle(recycle);
			//mCardView.setCard(mCard);
		//}
		return rootView;
	}
	
/*	@Override
	public int getViewTypeCount() {
		return 2;
	}
*/
	private static ParseQueryAdapter.QueryFactory<Babysitter> getQueryFactory(final Context context, final int position) {
		ParseQueryAdapter.QueryFactory<Babysitter> factory = new ParseQueryAdapter.QueryFactory<Babysitter>() {
			public ParseQuery<Babysitter> create() {

				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

				
				ParseQuery<Babysitter> query = Babysitter.getQuery();
				
				boolean mDay = sharedPreferences.getBoolean("mDay", false);
				if (mDay) {
					query.whereContains("babycareTime", "白天");
				}

				boolean mNight = sharedPreferences.getBoolean("mNight", false);
				if (mNight) {
					query.whereContains("babycareTime", "半天");
				}

				boolean mTemp = sharedPreferences.getBoolean("mTemp", false);
				if (mTemp) {
					query.whereContains("babycareTime", "臨時");
				}

				if (!Config.keyWord.equals("")) {
					String keyword = Config.keyWord;
					keyword = keyword.replace("台", "臺");
					query.whereContains("address", keyword);
				} else {
					query.whereNear("location", Config.MY_LOCATION);
				}
				

//				switch (position) {
//				case 0:
//					query.whereNear("location", Config.MY_LOCATION);
//					break;
//				case 1: //臨時保母
//					query.whereContains("babycareTime", "臨時");
//					query.whereNear("location", Config.MY_LOCATION);
//					break;
//				case 2:
//					String keyword = Config.keyWord;
//					keyword = keyword.replace("台", "臺");
//					query.whereContains("address", keyword);
//					break;
//				case 3:
//					query.orderByDescending("updatedAt");
//				default:
//					break;
//				}
				
				
/*				if (!DisplayUtils.hasNetwork(context)) {
					LogUtils.LOGD("vic", "babysitters fromLocalDatastore()");
					query.fromLocalDatastore();
				}
*/				return query;
			}
		};
		return factory;
	}

/*	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}

		return v;
	}
*/
}
