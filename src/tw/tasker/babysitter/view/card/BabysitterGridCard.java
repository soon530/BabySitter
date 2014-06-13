package tw.tasker.babysitter.view.card;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.DisplayUtils;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class BabysitterGridCard extends Card {

	private int resourceIdThumbnail = -1;
	private Babysitter mBabysitter;

	public BabysitterGridCard(Context context) {
		super(context, R.layout.carddemo_gplay_inner_content);
	}

	public BabysitterGridCard(Context context, int innerLayout) {
		super(context, innerLayout);
	}

	public void init() {
		CardHeader header = new CardHeader(getContext());
		header.setButtonOverflowVisible(false);
		header.setTitle(mBabysitter.getName());
/*		header.setPopupMenu(R.menu.popupmain,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						Toast.makeText(getContext(), "Item " + item.getTitle(),
								Toast.LENGTH_SHORT).show();
					}
				});
*/
		addCardHeader(header);

		GplayGridThumb thumbnail = new GplayGridThumb(getContext());
		thumbnail.setExternalUsage(true);
		/*
		 * if (resourceIdThumbnail > -1)
		 * thumbnail.setDrawableResource(resourceIdThumbnail); else
		 * thumbnail.setDrawableResource(R.drawable.ic_launcher);
		 */
		addCardThumbnail(thumbnail);

		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				Intent detailIntent = new Intent(getContext(),
						BabysitterActivity.class);
				detailIntent.putExtra(Config.BABYSITTER_OBJECT_ID,
						mBabysitter.getObjectId());
				getContext().startActivity(detailIntent);
			}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		float totalRatingValue = mBabysitter.getTotalRating();
		int totalComementValue = mBabysitter.getTotalComment();

		TextView title = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_title);

		title.setText("保母評論:" + String.valueOf(totalComementValue));

		TextView subtitle = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
		subtitle.setText(mBabysitter.getAddress());

		RatingBar mRatingBar = (RatingBar) parent
				.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

		mRatingBar.setNumStars(5);
		mRatingBar.setMax(5);
		mRatingBar.setStepSize(0.5f);

		float rating = DisplayUtils.getRatingValue(totalRatingValue, totalComementValue);
		mRatingBar.setRating(rating);
	}

	class GplayGridThumb extends CardThumbnail {
		private DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();

		public GplayGridThumb(Context context) {
			super(context);

			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.displayer(new SimpleBitmapDisplayer())
					.showImageOnFail(R.drawable.ic_launcher).build();

		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View viewImage) {
			String url;
			//if (mBabysitter.getImageUrl().equals("../img/photo_mother_no.jpg")) {
			 //url = "";
			 //} else {
			//url = "http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg";
			url = "http://cwisweb.sfaa.gov.tw/" + mBabysitter.getImageUrl();
			// }

			imageLoader.displayImage(url, (ImageView) viewImage, options, null);

			// viewImage.getLayoutParams().width = 196;
			// viewImage.getLayoutParams().height = 196;

		}
	}

	public void setBabysitter(Babysitter babysitter) {
		mBabysitter = babysitter;
	}


}
