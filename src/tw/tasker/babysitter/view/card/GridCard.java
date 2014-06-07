package tw.tasker.babysitter.view.card;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.activity.BabysitterActivity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class GridCard extends Card {

	public String mBabysitterObjectId;
	public String mUrl;
	public TextView mTitle;
	public TextView mSecondaryTitle;
	public RatingBar mRatingBar;
	public int resourceIdThumbnail = -1;
	public int count;

	public String headerTitle;
	public String secondaryTitle;
	public float rating;
	public String mComment;
	
	public GridCard(Context context) {
		super(context, R.layout.carddemo_gplay_inner_content);
	}

	public GridCard(Context context, int innerLayout) {
		super(context, innerLayout);
	}

	public void init() {
		CardHeader header = new CardHeader(getContext());
		header.setButtonOverflowVisible(true);
		header.setTitle(headerTitle);
		header.setPopupMenu(R.menu.popupmain,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						Toast.makeText(getContext(),
								"Item " + item.getTitle(),
								Toast.LENGTH_SHORT).show();
					}
				});

		addCardHeader(header);

		GplayGridThumb thumbnail = new GplayGridThumb(getContext());
		thumbnail.setExternalUsage(true);
/*			if (resourceIdThumbnail > -1)
			thumbnail.setDrawableResource(resourceIdThumbnail);
		else
			thumbnail.setDrawableResource(R.drawable.ic_launcher);
*/
		addCardThumbnail(thumbnail);

		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				Intent detailIntent = new Intent(getContext(),
						BabysitterActivity.class);
				detailIntent.putExtra(Config.BABYSITTER_OBJECT_ID,
						mBabysitterObjectId);
				getContext().startActivity(detailIntent);
			}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		TextView title = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_title);
		title.setText("評論"+mComment);

		TextView subtitle = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
		subtitle.setText(secondaryTitle);

		RatingBar mRatingBar = (RatingBar) parent
				.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

		mRatingBar.setNumStars(5);
		mRatingBar.setMax(5);
		mRatingBar.setStepSize(0.5f);
		mRatingBar.setRating(rating);
	}

	class GplayGridThumb extends CardThumbnail {
		DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();

		public GplayGridThumb(Context context) {
			super(context);
			
	        options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .displayer(new SimpleBitmapDisplayer())
	        .showImageOnFail(R.drawable.ic_launcher)
	        .build();

		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View viewImage) {

    		imageLoader.displayImage(mUrl, (ImageView) viewImage, options, null);

			// viewImage.getLayoutParams().width = 196;
			// viewImage.getLayoutParams().height = 196;

		}
	}

}
