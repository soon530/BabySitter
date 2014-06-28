package tw.tasker.babysitter.view.card;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.presenter.adapter.BabyDiaryParseQueryAdapter;
import tw.tasker.babysitter.view.activity.BabyRecordActivity;
import tw.tasker.babysitter.view.fragment.BabyDiaryEditDialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class BabyGridCard extends Card {

	public int resourceIdThumbnail = -1;
	private BabyDiary mBabyDiary;
	private BabyRecord mBabyRecord;
	private Fragment mFragment;
	private BabyDiaryParseQueryAdapter mAdapter;
	private ProgressDialog mRingProgressDialog;

	public BabyGridCard(Context context) {
		super(context, R.layout.baby_grid_card_inner_content);
	}

	public BabyGridCard(Context context, int innerLayout) {
		super(context, innerLayout);
	}

	public void init() {
		String currenUser = ParseUser.getCurrentUser().getUsername(); 
		String recordUser = mBabyDiary.getUser().getUsername();

		mBabyRecord = mBabyDiary.getBabyRecord();
		CardHeader header = new CardHeader(getContext());
		header.setButtonOverflowVisible(true);
		header.setTitle(mBabyDiary.getName());
		
		if (currenUser.equals(recordUser)) {
		header.setPopupMenu(R.menu.popup_edit,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						int id =item.getItemId();
						switch (id) {
						case R.id.card_edit:
							editCard();
							break;
						case R.id.card_del:
							deleteCard();
							//updateBabyDiary();
						default:
							break;
						}
					}
				});
		}
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
						BabyRecordActivity.class);
				intent.putExtra(Config.BABY_OBJECT_ID, mBabyDiary.getObjectId());
				intent.putExtra(Config.TOTAL_RECORD, mBabyDiary.getTotalRecord());
				getContext().startActivity(intent);
			}
		});
	}

	
	private void editCard() {
		BabyDiaryEditDialogFragment newFragment = new BabyDiaryEditDialogFragment(mBabyDiary, mAdapter);
		newFragment.show(mFragment.getFragmentManager(), "edit_dialog");
	}
	
	private void deleteCard() {
		mRingProgressDialog = ProgressDialog.show(mFragment.getActivity(),
		"請稍等 ...", "資料刪除中...", true);

		mBabyDiary.deleteInBackground(new DeleteCallback() {
			
			@Override
			public void done(ParseException e) {
				mAdapter.loadObjects();
				mRingProgressDialog.dismiss();
			}
		});
	}


	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		TextView title = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_title);
		
		String totalRecord = "記錄" + mBabyDiary.getTotalRecord();
		String totalFavorite = "收藏" + mBabyDiary.getTotalFavorite();
		Boolean isPublic = mBabyDiary.getIsPublic();
		String privateOrPublic = "  (私藏)";
		if (isPublic) {
			privateOrPublic = "  (公開)";
		}
		
		title.setText(totalRecord + " " + totalFavorite + privateOrPublic);

		TextView subtitle = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
		
		String description;
		if (mBabyRecord == null) {
			description =mBabyDiary.getNote(); 
		} else {
			description = mBabyRecord.getTitle();
		
		}
		subtitle.setText(description);

/*		RatingBar mRatingBar = (RatingBar) parent
				.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

		mRatingBar.setNumStars(5);
		mRatingBar.setMax(5);
		mRatingBar.setStepSize(0.5f);
		mRatingBar.setRating((float) (Math.random() * (5.0)));
*/	}

	class GplayGridThumb extends CardThumbnail {
		//DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();

		public GplayGridThumb(Context context) {
			super(context);

/*			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.displayer(new SimpleBitmapDisplayer())
					.showImageOnFail(R.drawable.ic_launcher).build();
*/
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View viewImage) {

			String url;
			url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
			if (mBabyRecord != null) {
				if (mBabyRecord.getPhotoFile() != null) {
					url = mBabyRecord.getPhotoFile().getUrl();
				}
			}

			imageLoader.displayImage(url, (ImageView) viewImage, Config.OPTIONS, null);

			// viewImage.getLayoutParams().width = 196;
			// viewImage.getLayoutParams().height = 196;

		}
	}

	public void setBaby(BabyDiary babyDiary) {
		mBabyDiary = babyDiary;
	}

	public void setFragment(Fragment fragment) {
		mFragment = fragment;
	}

	public void setAdapter(BabyDiaryParseQueryAdapter adapter) {
		mAdapter = adapter;
	}

}
