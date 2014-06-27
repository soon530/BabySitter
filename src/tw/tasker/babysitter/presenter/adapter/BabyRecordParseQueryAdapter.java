package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.view.card.BabyListCard;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class BabyRecordParseQueryAdapter extends ParseQueryAdapter<BabyRecord> {
	ImageView mUserAvator;
	TextView mTitle;
	TextView mDescription;

	DisplayImageOptions options;
	private Fragment mFragment;

	public BabyRecordParseQueryAdapter(Context context, String babyObejctId, Fragment fragment) {
		super(context, getFactory(babyObejctId, context));
		mFragment = fragment;
	}

	public static ParseQueryAdapter.QueryFactory<BabyRecord> getFactory(
			final String babyObjectId, final Context context) {
		ParseQueryAdapter.QueryFactory<BabyRecord> factory = new ParseQueryAdapter.QueryFactory<BabyRecord>() {
			public ParseQuery<BabyRecord> create() {
				ParseQuery<BabyRecord> query = BabyRecord.getQuery();
				query.orderByDescending("createdAt");
				BabyDiary babyDiary = ParseObject.createWithoutData(BabyDiary.class, babyObjectId);
				query.whereEqualTo("BabyDiary", babyDiary);
				query.setLimit(20);
				query.include("user");
/*				if(!DisplayUtils.hasNetwork(context)) {
					query.fromLocalDatastore();
				}
*/				return query;
			}
		};
		return factory;
	}

	public View getItemView(BabyRecord babyRecord, View view, ViewGroup parent) {

		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}
		
		BabyListCard mCard = new BabyListCard(getContext());
		mCard.setBabyRecord(babyRecord);
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
	};

	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}
		//TextView t = (TextView) v.findViewById(R.id.textView1);
/*		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.LOGD("vic", "BabyRecord onclick()");
			}
		});
*/
		return v;
	}
}
