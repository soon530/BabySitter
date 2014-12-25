package tw.tasker.babysitter.view.fragment;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.view.card.BabysitterCard;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SitterFragment extends Fragment {
	private LinearLayout mDemo;
	private CardView mBabysitterCard;
	private CardView mTelCard;
	private CardView mEmailCard;
	private CardView mAddressCard;
	private CardView mDateCard;
	private CardView mBabycareCard;
	private Babysitter mBabysitter;

	public static Fragment newInstance(int position) {
		SitterFragment fragment = new SitterFragment();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.demo_fragment_card, container, false);
		mDemo = (LinearLayout) rootView.findViewById(R.id.demo);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Babysitter babysitter = new Babysitter();
		babysitter.setTotalRating(0.0f);
		babysitter.setImageUrl("../img/photo_mother_no.jpg");
		babysitter.setTel("");
		
		initCards(babysitter);
	}
	
	private void initCards(Babysitter babysitter) {
		mBabysitterCard = initBasicCard(babysitter);
		mTelCard = initCard("電話 (可點選)", babysitter.getTel().replace(": ", ":").replace(" ", "\n"), R.id.tel_card);
		
		mEmailCard = initCard("郵件 (可點選)", "soon530@gmail.com", R.id.email_card);
		mEmailCard.setVisibility(View.GONE);
		
		mAddressCard = initCard("地址 (可點選)", babysitter.getAddress(), R.id.address_card);
		
		mDateCard = initCard("時段", babysitter.getBabycareTime(), R.id.date_card);
		
		mBabycareCard = initCard("托育 (可點選)", babysitter.getBabycareCount() , R.id.babycare_card);

		//mDemo.setVisibility(View.VISIBLE);
		
		mBabysitter = babysitter;
		
		//hideProgress();
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
		//cardView.setOnClickListener(this);
		
		return cardView;
	}


}
