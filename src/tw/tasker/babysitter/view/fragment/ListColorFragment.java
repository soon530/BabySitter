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

package tw.tasker.babysitter.view.fragment;

//import it.gmariotti.cardslib.demo.R;
//import it.gmariotti.cardslib.demo.cards.ColorCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import tw.tasker.babysitter.FavoriteBabysitterActivity;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.activity.BabyDiaryActivity;
import tw.tasker.babysitter.view.activity.BabysitterListActivity;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import tw.tasker.babysitter.view.activity.FavoriteBabyActivity;
import tw.tasker.babysitter.view.card.ColorCard;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

import com.parse.ParseUser;

/**
 * List base example
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ListColorFragment extends Fragment implements OnCardClickListener {

    protected ScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_fragment_colors, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCards();
    }


    private void initCards() {

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 4; i++) {
            ColorCard card = new ColorCard(this.getActivity());
            //card.setTitle("A simple colored card " + i);
            card.setCount(i);
            card.setOnClickListener(this);
            switch (i) {
                case 0:
                    card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
                    card.setTitle("附近保母");
                    card.setDescription("更有效率，快速定位找出你附近的保母");
                    break;
                case 1:
                    card.setBackgroundResourceId(R.drawable.demo_card_selector_color4);
                    card.setTitle("寶寶日記");
                    card.setDescription("更有樂趣，看看人家的寶寶怎麼成長的");
                    break;
                case 2:
                    card.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
                    card.setTitle("我的收藏");
                    card.setDescription("更有品味，收集有興趣的保母及寶寶日記");
                    break;
                case 3:
                    card.setBackgroundResourceId(R.drawable.demo_card_selector_color2);
                    card.setTitle("登出帳號");
                    card.setDescription("更多安全，登出後只可搜尋無法留言");
                    break;
                case 4:
                    card.setBackgroundResourceId(R.drawable.demo_card_selector_color1);
                    break;
            }

            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.carddemo_list_colors);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

	@Override
	public void onClick(Card card, View view) {
		Intent intent;
		int count =  ((ColorCard) card).getCount();
		switch (count) {
		case 0:
			intent = new Intent();
			intent.setClass(getActivity(),
					BabysitterListActivity.class);
			startActivity(intent);
			break;
			
		case 1:
			intent = new Intent();
			intent.setClass(getActivity(), BabyDiaryActivity.class);
			startActivity(intent);
			break;
			
		case 2:
			intent = new Intent();
			intent.setClass(getActivity(), FavoriteBabyActivity.class);
			startActivity(intent);

/*			intent = new Intent();
			intent.setClass(getActivity(), FavoriteBabysitterActivity.class);
			startActivity(intent);
*/
			break;

		case 3:
	        // Call the Parse log out method
	        ParseUser.logOut();
	        intent = new Intent();
	        // Start and intent for the dispatch activity
	        intent.setClass(getActivity(), DispatchActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
			
			break;
		default:
			break;
		}
		
	}

}
