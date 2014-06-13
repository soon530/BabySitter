package tw.tasker.babysitter.view.fragment;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterDetailPresenterImpl;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterDetailView;
import tw.tasker.babysitter.view.card.BabysitterCard;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BabysitterFragment extends Fragment implements
		BabysitterDetailView, OnRefreshListener, OnClickListener {
	protected ScrollView mScrollView;
	private PullToRefreshLayout mPullToRefreshLayout;

	private String mBabysitterObjectId;
	private BabysitterDetailPresenter mPresenter;


	private boolean mIsChecked;
	private MenuItem mFavoriteItem;
	private BabysitterFavorite mBabysitterFavorite;
	
	private LinearLayout mDemo;
	private CardView mBabysitterCard;
	private CardView mTelCard;
	private CardView mEmailCard;
	private CardView mAddressCard;
	private CardView mDateCard;
	private CardView mBabycareCard;
	private Babysitter mBabysitter;

	
	public static Fragment newInstance(int position) {
		BabysitterFragment fragment = new BabysitterFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
			mBabysitterObjectId = getArguments().getString(
					Config.BABYSITTER_OBJECT_ID);
		}

		mPresenter = new BabysitterDetailPresenterImpl(this);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.demo_fragment_card, container, false);
		
		// Retrieve the PullToRefreshLayout from the content view
		mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.carddemo_extra_ptr_layout);

		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);
		
		mDemo = (LinearLayout) rootView.findViewById(R.id.demo);
		mDemo.setVisibility(View.GONE);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPresenter.doDetailQuery(mBabysitterObjectId);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mScrollView = (ScrollView) getActivity().findViewById(
				R.id.card_scrollview);

	}

	// call back
	public void fillHeaderUI(Babysitter outline) {
		// TODO 進到[保母明細]馬上退出會掛掉，回頭在review架構
		if (getActivity() == null) {
			return;
		}
		initCards(outline);
	}

	private void initCards(Babysitter babysitter) {
		mBabysitterCard = initBasicCard(babysitter);
		mTelCard = initCard("*電話", babysitter.getTel().replace(": ", ":").replace(" ", "\n"), R.id.tel_card);
		
		mEmailCard = initCard("*郵件", "soon530@gmail.com", R.id.email_card);
		mEmailCard.setVisibility(View.GONE);
		
		mAddressCard = initCard("*地址", babysitter.getAddress(), R.id.address_card);
		
		mDateCard = initCard("時段", babysitter.getBabycareTime(), R.id.date_card);
		
		mBabycareCard = initCard("*托育", babysitter.getBabycareCount() , R.id.babycare_card);

		mDemo.setVisibility(View.VISIBLE);
		
		mBabysitter = babysitter;
		
		hideProgress();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_card:
			ParseGeoPoint location = mBabysitter.getLocation();
			mPresenter.doDirections(location.getLatitude(), location.getLongitude());
			break;
		case R.id.babycare_card:
			mPresenter.seeBabyDetail(mBabysitterObjectId);
			break;
		case R.id.tel_card:
			mPresenter.makePhoneCall(mBabysitter.getTel());
			break;
		default:
			break;
		}
	}

	
	private CardView initBasicCard(Babysitter babysitter) {

		BabysitterCard card = new BabysitterCard(getActivity());
		card.setBabysitter(babysitter);
		card.init();
		CardView cardView = (CardView) getActivity().findViewById(
				R.id.carddemo_suggested);
		cardView.setCard(card);
		return cardView;
	}

	private CardView initCard(String title, String descript,
			int layou_id) {

		// Create a Card
		Card card = new Card(getActivity(),
				R.layout.carddemo_example_inner_content);

		// Create a CardHeader
		CardHeader header = new CardHeader(getActivity(),
				R.layout.carddemo_buttonleft_inner_header);
		// Set the header title
		header.setTitle(title);

		card.addCardHeader(header);

		// Set the card inner text
		card.setTitle(descript);

		// Set card in the cardView
		CardView cardView = (CardView) getActivity().findViewById(layou_id);
		cardView.setCard(card);
		cardView.setOnClickListener(this);
		
		return cardView;
	}

	@Override
	public void showProgress() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void hideProgress() {
		ProgressBarUtils.hide(getActivity());
		mPullToRefreshLayout.setRefreshComplete();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.favorite, menu);
		mFavoriteItem = menu.findItem(R.id.action_favorite);
		getFavorite();
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_favorite:
			if (mIsChecked) {
				item.setTitle("未收藏");
				deleteFavorite();
			} else {
				item.setTitle("已收藏");
				addFavorite();
			}
			mIsChecked = !mIsChecked;

			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefreshStarted(View view) {
		showProgress();
		mPresenter.doDetailQuery(mBabysitterObjectId);
	}
	
	
	private void getFavorite() {
		Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class, mBabysitterObjectId);
		ParseQuery<BabysitterFavorite> favorite_query = BabysitterFavorite.getQuery();

		favorite_query.whereEqualTo("Babysitter", babysitter);
		favorite_query.whereEqualTo("user", ParseUser.getCurrentUser());
		favorite_query.getFirstInBackground(new GetCallback<BabysitterFavorite>() {

			@Override
			public void done(BabysitterFavorite babysitterFavorite, ParseException e) {
				if (babysitterFavorite == null) {
					mIsChecked = false;
					mFavoriteItem.setTitle("未收藏");
				} else {
					mIsChecked = true;
					mFavoriteItem.setTitle("已收藏");
					mBabysitterFavorite = babysitterFavorite;
				}
			}
		});
	}

	private void addFavorite() {
		Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class, mBabysitterObjectId);

		BabysitterFavorite babysitterfavorite = new BabysitterFavorite();
		mBabysitterFavorite = babysitterfavorite;
		// favorite.put("baby", mBaby);
		babysitterfavorite.setBabysitter(babysitter);
		babysitterfavorite.put("user", ParseUser.getCurrentUser());
		babysitterfavorite.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					// Toast.makeText(getActivity().getApplicationContext(),
					// "saving doen!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

	private void deleteFavorite() {
		mBabysitterFavorite.deleteInBackground(new DeleteCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					// Toast.makeText(getActivity().getApplicationContext(),
					// "deleting doen!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


}
