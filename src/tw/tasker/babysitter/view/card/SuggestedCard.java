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

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import tw.tasker.babysitter.R;
//import it.gmariotti.cardslib.demo.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SuggestedCard extends Card {

	//public String mName;

    public SuggestedCard(Context context) {
        this(context, R.layout.carddemo_suggested_inner_content);
        //mName = name;
    }

    public SuggestedCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

        private void init() {

        //Add a header
        SuggestedCardHeader header = new SuggestedCardHeader(getContext());
        //header.mName = mName;
        addCardHeader(header);

        //Set click listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click listener", Toast.LENGTH_LONG).show();
            }
        });

        //Set swipe on
        //setSwipeable(true);

        //Add thumbnail
        CardThumbnail thumb = new SuggestedCardThumb(getContext());
        thumb.setUrlResource("http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg");
        thumb.setErrorResource(R.drawable.ic_launcher);
        addCardThumbnail(thumb);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.carddemo_suggested_title);
            TextView member = (TextView) view.findViewById(R.id.carddemo_suggested_memeber);
            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_suggested_subtitle);
            TextView community = (TextView) view.findViewById(R.id.carddemo_suggested_community);

            if (title != null)
                title.setText("姓名:張女士");

            if (member != null)
                member.setText("性別:女");

            if (subtitle != null)
                subtitle.setText("年齡:40");

            if (community != null)
                community.setText("教育:研究所");
        }
    }


}

class SuggestedCardHeader extends CardHeader {

    //public String mName ;

	public SuggestedCardHeader(Context context) {
        this(context, R.layout.carddemo_suggested_header_inner);
        //mName = name;
    }

    public SuggestedCardHeader(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.text_suggested_card1);

            if (textView != null) {
                textView.setText("保母");
            }
        }
    }
}

class SuggestedCardThumb extends CardThumbnail {

    public SuggestedCardThumb(Context context) {
        super(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
        if (viewImage != null) {

            if (parent!=null && parent.getResources()!=null){
                DisplayMetrics metrics=parent.getResources().getDisplayMetrics();

                int base = 150;

                if (metrics!=null){
                    viewImage.getLayoutParams().width = (int)(base*metrics.density);
                    viewImage.getLayoutParams().height = (int)(base*metrics.density);
                }else{
                    viewImage.getLayoutParams().width = 200;
                    viewImage.getLayoutParams().height = 200;
                }
            }
        }
    }
}