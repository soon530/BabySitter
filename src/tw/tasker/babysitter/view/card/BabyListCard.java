package tw.tasker.babysitter.view.card;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.presenter.adapter.BabyRecordParseQueryAdapter;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.fragment.EditDialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class BabyListCard extends Card {

	public int resourceIdThumbnail = -1;
	private BabyRecord mBabyRecord;
	private Fragment mFragment;
	private BabyRecordParseQueryAdapter mAdapter;
	private ProgressDialog mRingProgressDialog;

	public BabyListCard(Context context) {
		super(context, R.layout.baby_list_card_inner_content);
	}

	public BabyListCard(Context context, int innerLayout) {
		super(context, innerLayout);
	}

	public void init() {
		String currenUser = ParseUser.getCurrentUser().getUsername(); 
		String recordUser = mBabyRecord.getUser().getUsername();
		
		CardHeader header = new CardHeader(getContext());
		header.setButtonOverflowVisible(true);
		header.setTitle(recordUser + "說:" + mBabyRecord.getTitle());
				
		if (currenUser.equals(recordUser)) {
		header.setPopupMenu(R.menu.popupmain,
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
							updateBabyDiary();
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

/*		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				Intent intent = new Intent(getContext(),
						BabyRecordActivity.class);
				intent.putExtra(Config.BABY_OBJECT_ID, mBabyRecord.getObjectId());
				getContext().startActivity(intent);
			}
		});
*/		
	}
	private void editCard() {
		EditDialogFragment newFragment = new EditDialogFragment(mBabyRecord, mAdapter);
		newFragment.show(mFragment.getFragmentManager(), "edit_dialog");
	}

	
	private void deleteCard() {
		mRingProgressDialog = ProgressDialog.show(mFragment.getActivity(),
		"請稍等 ...", "資料刪除中...", true);

		mBabyRecord.deleteInBackground(new DeleteCallback() {
			
			@Override
			public void done(ParseException e) {
				mAdapter.loadObjects();
				mRingProgressDialog.dismiss();
			}
		});
	}
	
	private void updateBabyDiary() {
		ParseQuery<BabyDiary> query = BabyDiary.getQuery();
		String babyDiaryObjectId = mBabyRecord.getBaby().getObjectId(); 
		query.getInBackground(babyDiaryObjectId, new GetCallback<BabyDiary>() {
			
			@Override
			public void done(BabyDiary babyDiary, ParseException e) {
				
				if ( mAdapter.getCount() >=2 && mAdapter.getItem(0).equals(mBabyRecord)) {
					LogUtils.LOGD("vic", "update 2nd card to BabyDiary!");
					
					babyDiary.setBabyRecord(mAdapter.getItem(1));
				}
				
				babyDiary.increment("totalRecord", -1);
				babyDiary.saveInBackground();
			}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		TextView title = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_title);
		title.setText(DisplayUtils.getDateTime(mBabyRecord.getCreatedAt()));

		TextView subtitle = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
		
		subtitle.setText(mBabyRecord.getDescription());

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
			// 暫時先用程式控制，有空在回頭過來看..
			if (viewImage != null) {

				if (parent != null && parent.getResources() != null) {
					DisplayMetrics metrics = parent.getResources()
							.getDisplayMetrics();

					int base = 300;

					if (metrics != null) {
						//viewImage.getLayoutParams().width = (int) (base * metrics.density);
						viewImage.getLayoutParams().height = (int) (base * metrics.density);
					} else {
						//viewImage.getLayoutParams().width = 450;
						viewImage.getLayoutParams().height = base;
					}
				}
			
				String url;
				if (mBabyRecord.getPhotoFile() != null) {
					url = mBabyRecord.getPhotoFile().getUrl();
				} else {
					url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
				}

				
				imageLoader
						.displayImage(url, (ImageView) viewImage, Config.OPTIONS, null);
			}
			
			//viewImage.getLayoutParams().width = 450;
			//viewImage.getLayoutParams().height = 450;

		}
	}

	public void setBabyRecord(BabyRecord babyRecord) {
		mBabyRecord = babyRecord;
	}

	public void setFragment(Fragment fragment) {
		mFragment = fragment;
	}

	public void setAdapter(BabyRecordParseQueryAdapter adapter) {
		mAdapter = adapter;
	}	
	
}
