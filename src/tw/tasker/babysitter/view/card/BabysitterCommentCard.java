/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package tw.tasker.babysitter.view.card;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.presenter.adapter.BabysitterCommentParseQueryAdapter;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.view.fragment.BabysitterCommentEditDialogFragment;
import tw.tasker.babysitter.view.fragment.EditDialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * This class provides a simple example as Google Play card.
 * The Google maps icon this time is loaded from package manager.
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com), Ronald Ammann (ramdroid.fn@gmail.com)
 */
public class BabysitterCommentCard extends Card {

    private BabysitterComment mBabysitterComment;
	private BabysitterCommentParseQueryAdapter mAdapter;
	private Fragment mFragment;
	private ProgressDialog mRingProgressDialog;

	public BabysitterCommentCard(Context context) {
        super(context, R.layout.babysitter_list_card_inner_content);
        //init();
    }

    public BabysitterCommentCard(Context context, int innerLayout) {
        super(context, innerLayout);
        //init();
    }

    public void init() {
		String currenUser = ParseUser.getCurrentUser().getUsername(); 
		String commentUser = mBabysitterComment.getUser().getUsername();

        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(commentUser + "說：" +mBabysitterComment.getTitle());
        
		if (currenUser.equals(commentUser)) {

        header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
				int id =item.getItemId();
				switch (id) {
				case R.id.card_edit:
					editCard();
					break;
				case R.id.card_del:
					deleteCard();
				default:
					break;
				}
            }
        });
		}
        addCardHeader(header);

        CardThumbnail thumbnail = new CardThumbnail(getContext());
        thumbnail.setCustomSource(new CardThumbnail.CustomSource() {
            @Override
            public String getTag() {
                return "tw.tasker.babysitter";
            }

            @Override
            public Bitmap getBitmap() {
                PackageManager pm = mContext.getPackageManager();
                Bitmap bitmap = null;
                try {
                    bitmap = drawableToBitmap(pm.getApplicationIcon(getTag()));
                } catch (PackageManager.NameNotFoundException e) {
                }
                return bitmap;
            }

            private Bitmap drawableToBitmap(Drawable drawable) {
                if (drawable instanceof BitmapDrawable) {
                    return ((BitmapDrawable) drawable).getBitmap();
                }

                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                return bitmap;
            }
        });
        addCardThumbnail(thumbnail);
    }
    
	private void editCard() {
		BabysitterCommentEditDialogFragment newFragment = new BabysitterCommentEditDialogFragment(mBabysitterComment, mAdapter);
		newFragment.show(mFragment.getFragmentManager(), "edit_dialog");
	}

	private void deleteCard() {
		mRingProgressDialog = ProgressDialog.show(mFragment.getActivity(),
		"請稍等 ...", "資料刪除中...", true);

		mBabysitterComment.deleteInBackground(new DeleteCallback() {
			
			@Override
			public void done(ParseException e) {
				mAdapter.loadObjects();
				mRingProgressDialog.dismiss();
			}
		});
	}


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
        if (mBabysitterComment.getCreatedAt() != null) {
        	title.setText(DisplayUtils.getDateTime(mBabysitterComment.getCreatedAt()));
        }
        
        TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
        subtitle.setText(mBabysitterComment.getDescription());

        RatingBar mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

        mRatingBar.setNumStars(5);
        mRatingBar.setMax(5);
        mRatingBar.setStepSize(0.5f);
        mRatingBar.setRating(mBabysitterComment.getRating());
    }

	public void setBabysitterComment(BabysitterComment comment) {
		mBabysitterComment = comment;
	}

	public void setFragment(Fragment fragment) {
		mFragment = fragment;
	}

	public void setAdapter(
			BabysitterCommentParseQueryAdapter adapter) {
		mAdapter = adapter;
	}


}
