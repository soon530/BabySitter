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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * List base example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class NewHomeFragment extends Fragment implements OnCardClickListener, OnClickListener {

	protected ScrollView mScrollView;
	private LinearLayout mLayout;
	private TextView mFilter;
	private CheckBox mDay;
	private CheckBox mNight;
	private CheckBox mTemp;
	private CheckBox mHome;
	private CheckBox mKids0;
	private CheckBox mKids1;
	private CheckBox mKids2;
	private CheckBox mKids3;
	private CheckBox mOld40;
	private CheckBox mOld40_50;
	private CheckBox mOld50;
	private Button mSave;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
		
		mLayout = (LinearLayout) rootView.findViewById(R.id.filter_pannel);
		mLayout.setVisibility(View.GONE);
		mFilter = (TextView) rootView.findViewById(R.id.filter);
		mFilter.setOnClickListener(this);
		
		// check box
		mDay = (CheckBox) rootView.findViewById(R.id.day);
		mNight = (CheckBox) rootView.findViewById(R.id.night);
		mTemp = (CheckBox) rootView.findViewById(R.id.temp);
		mHome = (CheckBox) rootView.findViewById(R.id.home);
		
		mKids0 = (CheckBox) rootView.findViewById(R.id.kids_0);
		mKids1 = (CheckBox) rootView.findViewById(R.id.kids_1);
		mKids2 = (CheckBox) rootView.findViewById(R.id.kids_2);
		mKids3 = (CheckBox) rootView.findViewById(R.id.kids_3);

		mOld40 = (CheckBox) rootView.findViewById(R.id.old_40);
		mOld40_50 = (CheckBox) rootView.findViewById(R.id.old_40_50);
		mOld50 = (CheckBox) rootView.findViewById(R.id.old_50);
		
		mSave = (Button) rootView.findViewById(R.id.save);
		mSave.setOnClickListener(this);
		
		loadSavedPreferences();
		
		return rootView; 
	}
	
	private void loadSavedPreferences() {
		setCheckBox(mDay, "mDay");
		setCheckBox(mNight, "mNight");
		setCheckBox(mTemp, "mTemp");
		setCheckBox(mHome, "mHome");
		
		setCheckBox(mKids0, "mKids0");
		setCheckBox(mKids1, "mKids1");
		setCheckBox(mKids2, "mKids2");
		setCheckBox(mKids3, "mKids3");
		
		setCheckBox(mOld40, "mOld40");
		setCheckBox(mOld40_50, "mOld40_50");
		setCheckBox(mOld50, "mOld50");
	}

	
	private void setCheckBox(CheckBox checkBox, String key) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		boolean checkBoxValue = sharedPreferences.getBoolean(key, false);
		if (checkBoxValue) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		
	}
	

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch (id) {
		case R.id.filter :
			if (mLayout.getVisibility() == View.VISIBLE) {
				mLayout.setVisibility(View.GONE);
				mFilter.setText("v顯示更多過濾條件");
				
			} else { 
				mLayout.setVisibility(View.VISIBLE);
				mFilter.setText("^隱藏更多過濾條件");
			}
			
			break;

		case R.id.save:
			
			saveAllCheckbox();
			
			break;
			
		default:
			break;
		}
		
	}
	
	
	private void saveAllCheckbox() {
		savePreferences("mDay", mDay.isChecked());
		savePreferences("mNight", mNight.isChecked());
		savePreferences("mTemp", mTemp.isChecked());
		savePreferences("mHome", mHome.isChecked());

		savePreferences("mKids0", mKids0.isChecked());
		savePreferences("mKids1", mKids1.isChecked());
		savePreferences("mKids2", mKids2.isChecked());
		savePreferences("mKids3", mKids3.isChecked());

		savePreferences("mOld40", mOld40.isChecked());
		savePreferences("mOld40_50", mOld40_50.isChecked());
		savePreferences("mOld50", mOld50.isChecked());

		mLayout.setVisibility(View.GONE);
		mFilter.setText("v顯示更多過濾條件");
	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//initCards();
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
