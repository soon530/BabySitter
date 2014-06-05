package tw.tasker.babysitter.presenter.adapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardView;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.Favorite;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class FavoriteBabyParseQueryAdapter extends ParseQueryAdapter<Favorite> {
	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public FavoriteBabyParseQueryAdapter(Context context) {
		super(context, getQueryFactory());
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	@Override
	public View getItemView(Favorite favorite, View view, ViewGroup parent) {
        
		boolean recycle = false;
		if (view == null) {
			recycle = false;
			view = View.inflate(getContext(), R.layout.list_item_baby, null);
		} else {
			recycle = true;
		}
		
		Baby baby = favorite.getBaby();

        GplayGridCard mCard = new GplayGridCard(getContext());
        
        mCard.headerTitle = baby.getName();
        mCard.secondaryTitle = baby.getNote();
        mCard.rating = (float) (Math.random() * (5.0));
        //mCard.resourceIdThumbnail = R.drawable.ic_launcher;
        mCard.init();
		
        
		String url;
		if (baby.getPhotoFile() != null) {
			url = baby.getPhotoFile().getUrl();
		} else {
			url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
		}
		
		mCard.mUrl=url;

        
        
        CardView mCardView;

        //Setup card
        mCardView = (CardView) view.findViewById(R.id.list_cardId);
        if (mCardView != null) {
            //It is important to set recycle value for inner layout elements
            mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(mCardView.getCard(),mCard));

            //It is important to set recycle value for performance issue
            mCardView.setRecycle(recycle);

            //Save original swipeable to prevent cardSwipeListener (listView requires another cardSwipeListener)
//            boolean origianlSwipeable = mCard.isSwipeable();
//            mCard.setSwipeable(false);

            mCardView.setCard(mCard);

            //Set originalValue
//            mCard.setSwipeable(origianlSwipeable);

        }

		
/*		ImageView babyAvator = (ImageView) view.findViewById(R.id.baby_avator);
		TextView babyName = (TextView) view.findViewById(R.id.baby_name);
		TextView babyNote = (TextView) view.findViewById(R.id.baby_note);
		TextView totalFavorite = (TextView) view
				.findViewById(R.id.total_favorite);
		TextView totalRecord = (TextView) view.findViewById(R.id.total_record);

		Baby baby = favorite.getBaby();
		String url;
		if (baby.getPhotoFile() != null) {
			url = baby.getPhotoFile().getUrl();
		} else {
			url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
		}
		imageLoader.displayImage(url, babyAvator, options, null);

		String tag = "";
		if (baby.getIsPublic()) {
			tag = "公開";
		} else {
			tag = "私藏";
		}

		babyName.setText(baby.getName() + " (" + tag + ")");
		babyNote.setText(baby.getNote());
		totalFavorite.setText("最愛：+" + baby.getFavorite());
		totalRecord.setText("記錄：+5");
*/
		return view;
	}

	private static ParseQueryAdapter.QueryFactory<Favorite> getQueryFactory() {
		ParseQueryAdapter.QueryFactory<Favorite> factory = new ParseQueryAdapter.QueryFactory<Favorite>() {
			public ParseQuery<Favorite> create() {
				ParseQuery<Favorite> query = Favorite.getQuery();
				query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("user", ParseUser.getCurrentUser());
				//query.setLimit(20);
				query.include("baby");
				return query;
			}
		};
		return factory;
	}

	@Override
	public View getNextPageView(View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_next_page, null);
		}

		return v;
	}

	
    public class GplayGridCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail = -1;
        protected int count;

        protected String headerTitle;
        protected String secondaryTitle;
        protected float rating;
        
        protected String mUrl;

        public GplayGridCard(Context context) {
            super(context, R.layout.carddemo_gplay_inner_content);
        }

        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {
            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            addCardHeader(header);

            GplayGridThumb thumbnail = new GplayGridThumb(getContext());
            
/*            if (resourceIdThumbnail > -1)
                thumbnail.setDrawableResource(resourceIdThumbnail);
            else
                thumbnail.setDrawableResource(R.drawable.ic_launcher);
*/            
            
            addCardThumbnail(thumbnail);

            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getContext(), "你點是的：" + card.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
            title.setText("FREE");

            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText(secondaryTitle);

            RatingBar mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

            mRatingBar.setNumStars(5);
            mRatingBar.setMax(5);
            mRatingBar.setStepSize(0.5f);
            mRatingBar.setRating(rating);
        }

        class GplayGridThumb extends CardThumbnail {

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            	
        		imageLoader.displayImage(mUrl, (ImageView) viewImage, options, null);

            	
                //viewImage.getLayoutParams().width = 196;
                //viewImage.getLayoutParams().height = 196;

            }
        }

    }

}
