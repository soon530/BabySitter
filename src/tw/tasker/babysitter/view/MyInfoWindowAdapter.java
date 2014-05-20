package tw.tasker.babysitter.view;

import tw.tasker.babysitter.R;
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

	MyInfoWindowAdapter(LayoutInflater layoutinflater) {
		myContentsView = layoutinflater
				.inflate(R.layout.map_info_content, null);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20)).build();

	
	}

	@Override
	public View getInfoContents(Marker marker) {

		ImageView avator = (ImageView) myContentsView.findViewById(R.id.babysitter_avstor);
		TextView name = (TextView) myContentsView.findViewById(R.id.babysitter_name);
		RatingBar rating = (RatingBar) myContentsView.findViewById(R.id.babysitter_rating);
		TextView totalComemnt = (TextView) myContentsView.findViewById(R.id.babysitter_totalComment);
		
		name.setText(marker.getTitle());
		rating.setRating((float) 4.5);
		totalComemnt.setText("共有10則評論");
		
		
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

}
