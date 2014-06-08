package tw.tasker.babysitter.view.fragment;

//import it.gmariotti.cardslib.demo.R;
//import it.gmariotti.cardslib.demo.cards.SuggestedCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterDetailPresenterImpl;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterDetailView;
import tw.tasker.babysitter.view.activity.BabysitterCommentActivity;
import tw.tasker.babysitter.view.card.BabysitterCard;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class BabysitterFragment extends Fragment implements
		BabysitterDetailView {
	protected ScrollView mScrollView;
	protected TextView mTextViewSwipe;

	protected ActionMode mActionMode;
	protected Card mCardCab;
	protected CardView cardViewCab;

	private String mBabysitterObjectId;
	private BabysitterDetailPresenter mPresenter;

	private int mTotalRating;
	private int mTotalComment;

	public static Fragment newInstance(int position) {
		BabysitterFragment fragment =  new BabysitterFragment();
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
		return inflater.inflate(R.layout.demo_fragment_card, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mScrollView = (ScrollView) getActivity().findViewById(
				R.id.card_scrollview);
		// mTextViewSwipe = (TextView)
		// getActivity().findViewById(R.id.carddemo_card3_text);

		mPresenter.doDetailQuery(mBabysitterObjectId);

	}

	@Override
	public void onPause() {
		super.onPause();
		if (mActionMode != null)
			mActionMode.finish();
	}

	// call back
	public void fillHeaderUI(Babysitter outline) {
		initCards(outline);
	}

	private void initCards(Babysitter babysitter) {
		// init_simple_card();
		// init_card_inner_layout("保母姓名", "張女士", R.id.carddemo_card_inner1);
		// init_card_inner_layout("技術證號", "1234567890",
		// R.id.carddemo_card_inner2);
		// init_card_inner_layout("性別", "女", R.id.carddemo_card_inner3);
		// init_card_inner_layout("年齡", "40", R.id.carddemo_card_inner4);
		// init_card_inner_layout("教育程度", "大學", R.id.carddemo_card_inner5);

		initCardSuggested(babysitter.getName());
		init_card_inner_layout("電話",
				babysitter.getName() + babysitter.getTel(),
				R.id.carddemo_card_inner1);
		init_card_inner_layout("郵件", "soon530@gmail.com",
				R.id.carddemo_card_inner2);
		init_card_inner_layout("地址", babysitter.getAddress(),
				R.id.carddemo_card_inner3);

		init_card_inner_layout("時段", "全天24hr 臨時保母", R.id.carddemo_card_inner4);
		init_card_inner_layout("托育", "共" + babysitter.getBabycareCount() + "人",
				R.id.carddemo_card_inner5);

		// init_custom_card();
		// init_custom_card_swipe();
		// init_custom_card_clickable();
		// init_custom_card_partial_listener();
		// init_cab();
	}

	/**
	 * This method builds a suggested card example
	 * 
	 * @param babysitter
	 */
	private void initCardSuggested(String name) {

		BabysitterCard card = new BabysitterCard(getActivity());
		// card.mName = name;
		CardView cardView = (CardView) getActivity().findViewById(
				R.id.carddemo_suggested);
		cardView.setCard(card);
	}

	/**
	 * This method builds a simple card with a custom inner layout
	 * 
	 * @param layou_id
	 * @param descript
	 * @param title
	 */
	private void init_card_inner_layout(String title, String descript,
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
	}

	@Override
	public void showProgress() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void hideProgress() {
		ProgressBarUtils.hide(getActivity());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.babysitter_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {

		case R.id.action_comment:

			Intent intent = new Intent();

			Bundle bundle = new Bundle();
			bundle.putString(Config.BABYSITTER_OBJECT_ID, mBabysitterObjectId);
			bundle.putInt(Config.TOTAL_RATING, mTotalRating);
			bundle.putInt(Config.TOTAL_COMMENT, mTotalComment);
			intent.putExtras(bundle);

			intent.setClass(getActivity(), BabysitterCommentActivity.class);
			startActivity(intent);
			break;
		case R.id.refresh:
			mPresenter.refresh();
			break;
		case R.id.baby_diary:
			mPresenter.seeBabyDetail(mBabysitterObjectId);
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
