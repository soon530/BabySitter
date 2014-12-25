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

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.activity.BabyDiaryActivity;
import tw.tasker.babysitter.view.activity.BabysittersActivity;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import tw.tasker.babysitter.view.activity.MyFavoriteActivity;
import tw.tasker.babysitter.view.activity.SitterActivity;
import tw.tasker.babysitter.view.card.ColorCard;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.parse.ParseUser;

/**
 * List base example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class HomeFragment extends Fragment implements OnCardClickListener {

	protected ScrollView mScrollView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initCards();
	}

	private void initCards() {

		// Init an array of Cards
		ArrayList<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < 5; i++) {
			ColorCard card = new ColorCard(this.getActivity());
			// card.setTitle("A simple colored card " + i);
			card.setCount(i);
			card.setOnClickListener(this);
			switch (i) {
			case 0:
				card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
				card.setTitle("附近保母");
				card.setDescription("更有效率，定位搜尋快速找出您附近的保母");
				break;
			case 1:
				card.setBackgroundResourceId(R.drawable.demo_card_selector_color4);
				card.setTitle("寶寶日記");
				card.setDescription("更有樂趣，記錄寶寶成長分享您生命中的愛");
				break;
			case 2:
				card.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
				card.setTitle("我的收藏");
				card.setDescription("更有品味，收集您感興趣的保母及寶寶日記");
				break;
			case 3:
				card.setBackgroundResourceId(R.drawable.demo_card_selector_color2);
				
			    if (ParseUser.getCurrentUser() == null) { //沒有登入的
			    	card.setTitle("登入帳號");			    	
					card.setDescription("更多功能，登入後可收藏保母及幫保母評分");
			    } else {
			    	card.setTitle("登出帳號");
					card.setDescription("更有保障，登出後任何人都無法使用您的功能");
			    }
				
				break;
			case 4:
				card.setBackgroundResourceId(R.drawable.demo_card_selector_color1);
				card.setTitle("我是保母");
				card.setDescription("保母註冊頁面prototype");
				break;
			}

			cards.add(card);
		}

		CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(
				getActivity(), cards);

		CardListView listView = (CardListView) getActivity().findViewById(
				R.id.carddemo_list_colors);
		if (listView != null) {
			listView.setAdapter(mCardArrayAdapter);
		}
	}

	@Override
	public void onClick(Card card, View view) {
		Intent intent;
		int count = ((ColorCard) card).getCount();
		switch (count) {
		case 0:
			intent = new Intent();
			intent.setClass(getActivity(), BabysittersActivity.class);
			startActivity(intent);
			break;

		case 1:
		    
//			if (ParseUser.getCurrentUser() == null) { //沒有登入
//				intent = new Intent();
//				// Start and intent for the dispatch activity
//				intent.setClass(getActivity(), DispatchActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(intent);
//
//		    } else { // 有登入
				intent = new Intent();
				intent.setClass(getActivity(), BabyDiaryActivity.class);
				startActivity(intent);
//		    }			
			
			break;

		case 2:
		    if (ParseUser.getCurrentUser() == null) { //沒有登入
				intent = new Intent();
				// Start and intent for the dispatch activity
				intent.setClass(getActivity(), DispatchActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

		    } else { // 有登入

				intent = new Intent();
				intent.setClass(getActivity(), MyFavoriteActivity.class);
				startActivity(intent);
		    }
			break;

		case 3:
		    if (ParseUser.getCurrentUser() == null) { //沒有登入
		    } else { // 有登入
		    	// Call the Parse log out method
		    	ParseUser.logOut();		    	
		    }
			
			intent = new Intent();
			// Start and intent for the dispatch activity
			intent.setClass(getActivity(), DispatchActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			break;
		case 4:
			intent = new Intent();
			intent.setClass(getActivity(), SitterActivity.class);
			startActivity(intent);

		default:
			break;
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		String uri = "";
		Intent intent;

		switch (id) {
		case R.id.action_google_play:
			uri = "market://details?id=tw.tasker.babysitter";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			startActivity(intent);
			break;
		case R.id.action_fb:
			uri = "fb://page/765766966779332";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			startActivity(intent);
			break;

		case R.id.action_gmail:
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "soon530@gmail.com" });
			intent.putExtra(Intent.EXTRA_SUBJECT, "Search保母意見回饋");
			// intent.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(intent, "Search保母意見回饋"));
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
