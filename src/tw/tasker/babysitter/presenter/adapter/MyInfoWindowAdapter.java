package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.view.CardView;

import java.util.HashMap;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.card.BabysitterGridCard;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MyInfoWindowAdapter implements InfoWindowAdapter {

	HashMap<String, Babysitter> mMapModel;
	private Context mContext;
	private LayoutInflater mLayoutinflater;

	public MyInfoWindowAdapter(Context context, HashMap<String, Babysitter> mapModel) {
		mLayoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		mContext = context;
		mMapModel = mapModel;
	}

	@Override
	public View getInfoContents(Marker marker) {

		final Marker markerShowingInfoWindow = marker;
		View view = mLayoutinflater.inflate(R.layout.list_item_map, null);

		Babysitter babysitter = mMapModel.get(marker.getId());
		BabysitterGridCard mCard = new BabysitterGridCard(mContext);
		mCard.setBabysitter(babysitter);
		mCard.setListener(new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				if (markerShowingInfoWindow != null && markerShowingInfoWindow.isInfoWindowShown()) {

				    markerShowingInfoWindow.hideInfoWindow();
				    markerShowingInfoWindow.showInfoWindow();
				    //LogUtils.LOGD("vic", "this pic is ok!");

				}			
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
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
