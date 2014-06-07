package tw.tasker.babysitter.view.card;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.view.activity.BabyDetailActivity;
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

public class BabyGridCard extends Card {

	public int resourceIdThumbnail = -1;
	private Baby mBaby;

	public BabyGridCard(Context context) {
		super(context, R.layout.carddemo_gplay_inner_content);
	}

	public BabyGridCard(Context context, int innerLayout) {
		super(context, innerLayout);
	}

	public void init() {
		CardHeader header = new CardHeader(getContext());
		header.setButtonOverflowVisible(true);
		header.setTitle(mBaby.getName());
		header.setPopupMenu(R.menu.popupmain,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						Toast.makeText(getContext(), "Item " + item.getTitle(),
								Toast.LENGTH_SHORT).show();
					}
				});

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
				Intent intent = new Intent(getContext(),
						BabyDetailActivity.class);
				intent.putExtra(Config.BABY_OBJECT_ID, mBaby.getObjectId());
				getContext().startActivity(intent);
			}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		TextView title = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_title);
		title.setText("成長記錄:5");

		TextView subtitle = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
		subtitle.setText(mBaby.getNote());

		RatingBar mRatingBar = (RatingBar) parent
				.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

		mRatingBar.setNumStars(5);
		mRatingBar.setMax(5);
		mRatingBar.setStepSize(0.5f);
		mRatingBar.setRating((float) (Math.random() * (5.0)));
	}

	class GplayGridThumb extends CardThumbnail {
		DisplayImageOptions options;
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
			if (mBaby.getPhotoFile() != null) {
				url = mBaby.getPhotoFile().getUrl();
			} else {
				url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
			}

			
			imageLoader
					.displayImage(url, (ImageView) viewImage, options, null);

			// viewImage.getLayoutParams().width = 196;
			// viewImage.getLayoutParams().height = 196;

		}
	}

	public void setBaby(Baby baby) {
		mBaby = baby;
	}

}
