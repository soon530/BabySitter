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

//import it.gmariotti.cardslib.demo.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class BabysitterCard extends Card {

	// public String mName;

	private Babysitter mBabysitter;

	public BabysitterCard(Context context) {
		this(context, R.layout.carddemo_suggested_inner_content);
	}

	public BabysitterCard(Context context, int innerLayout) {
		super(context, innerLayout);
		//init();
	}

	public void init() {

		// Add a header
		SuggestedCardHeader header = new SuggestedCardHeader(getContext());
		addCardHeader(header);

		// Set click listener
/*		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				Toast.makeText(getContext(), "Click listener",
						Toast.LENGTH_LONG).show();
			}
		});
*/
		// Add thumbnail
		CardThumbnail thumb = new SuggestedCardThumb(getContext());
		thumb.setExternalUsage(true);
		addCardThumbnail(thumb);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		if (view != null) {
			TextView title = (TextView) view
					.findViewById(R.id.carddemo_suggested_title);
			TextView member = (TextView) view
					.findViewById(R.id.carddemo_suggested_memeber);
			TextView subtitle = (TextView) view
					.findViewById(R.id.carddemo_suggested_subtitle);
			TextView community = (TextView) view
					.findViewById(R.id.carddemo_suggested_community);

			if (title != null)
				title.setText("姓名:" + mBabysitter.getName());

			if (member != null)
				member.setText("性別:女");

			if (subtitle != null)
				subtitle.setText("年齡:40");

			if (community != null)
				community.setText("教育:研究所");
		}
	}

	public void setBabysitter(Babysitter babysitter) {
		mBabysitter = babysitter;
	}
}

class SuggestedCardHeader extends CardHeader {

	public SuggestedCardHeader(Context context) {
		this(context, R.layout.carddemo_suggested_header_inner);
	}

	public SuggestedCardHeader(Context context, int innerLayout) {
		super(context, innerLayout);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		if (view != null) {
			TextView textView = (TextView) view
					.findViewById(R.id.text_suggested_card1);

			if (textView != null) {
				textView.setText("保母");
			}
		}
	}
}

class SuggestedCardThumb extends CardThumbnail {
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public SuggestedCardThumb(Context context) {
		super(context);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.displayer(new SimpleBitmapDisplayer())
				.showImageOnFail(R.drawable.ic_launcher).build();
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View viewImage) {

		// viewImage.getLayoutParams().width = 196;
		// viewImage.getLayoutParams().height = 196;
		// 暫時先用程式控制，有空在回頭過來看..
		if (viewImage != null) {

			if (parent != null && parent.getResources() != null) {
				DisplayMetrics metrics = parent.getResources()
						.getDisplayMetrics();

				int base = 150;

				if (metrics != null) {
					viewImage.getLayoutParams().width = (int) (base * metrics.density);
					viewImage.getLayoutParams().height = (int) (base * metrics.density);
				} else {
					viewImage.getLayoutParams().width = 200;
					viewImage.getLayoutParams().height = 200;
				}
			}
		
			String url;
			// if (babysitter.getPhotoFile() != null) {
			// url = babysitter.getPhotoFile().getUrl();
			// } else {
			url = "http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg";
			// }

			imageLoader.displayImage(url, (ImageView) viewImage, options, null);
		}
	}
}