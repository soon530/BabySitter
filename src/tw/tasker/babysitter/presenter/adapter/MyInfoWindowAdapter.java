package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.view.CardView;

import java.util.HashMap;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements InfoWindowAdapter {

	HashMap<String, Babysitter> mMapModel;
	private Context mContext;
	private LayoutInflater mLayoutinflater;

	public MyInfoWindowAdapter(Context context, HashMap<String, Babysitter> map_model) {
		mLayoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		mContext = context;
		mMapModel = map_model;
	}

	@Override
	public View getInfoContents(Marker marker) {

		View view = mLayoutinflater.inflate(R.layout.list_item_map, null);

		Babysitter babysitter = mMapModel.get(marker.getId());
		BabysitterGridCard mCard = new BabysitterGridCard(mContext);
		mCard.setBabysitter(babysitter);
		mCard.init();

		CardView mCardView;
		mCardView = (CardView) view.findViewById(R.id.list_cardId);
		if (mCardView != null) {
			mCardView.setCard(mCard);
		}
		return view;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
}
