package tw.tasker.babysitter.presenter.adapter;

import java.util.HashMap;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyInfoWindowAdapter implements InfoWindowAdapter {

	private final View myContentsView;
	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	HashMap<String, Babysitter> mMapModel;

	public MyInfoWindowAdapter(LayoutInflater layoutinflater, HashMap<String, Babysitter> map_model) {
		myContentsView = layoutinflater
				.inflate(R.layout.map_info_content, null);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(1)).build();

		mMapModel = map_model;
	}

	@Override
	public View getInfoContents(Marker marker) {

		ImageView avator = (ImageView) myContentsView.findViewById(R.id.babysitter_avstor);
		TextView name = (TextView) myContentsView.findViewById(R.id.babysitter_name);
		RatingBar rating = (RatingBar) myContentsView.findViewById(R.id.babysitter_rating);
		TextView totalComemnt = (TextView) myContentsView.findViewById(R.id.babysitter_totalComment);
		
		name.setText(mMapModel.get(marker.getId()).getName());
		rating.setRating(getRatingValue( mMapModel.get(marker.getId()).getTotalRating(), mMapModel.get(marker.getId()).getTotalComment()));
		
		int comment = mMapModel.get(marker.getId()).getTotalComment();
		totalComemnt.setText("共有"+ String.valueOf(comment)+"則評論");
		
		imageLoader
		.displayImage(
				"http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg",
				avator, options, null);

		
		return myContentsView;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
	
	private float getRatingValue(int totalRating, int totalComment) {
		float avgRating = 0.0f; 
		
		if (totalComment != 0) {
			avgRating = totalRating / totalComment;
		}
		return avgRating;
	}

}
