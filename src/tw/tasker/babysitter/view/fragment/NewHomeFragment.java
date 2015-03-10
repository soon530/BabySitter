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
import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.presenter.adapter.BabyRecordParseQueryAdapter;
import tw.tasker.babysitter.presenter.adapter.BabysittersParseQueryAdapter;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;

/**
 * List base example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class NewHomeFragment extends Fragment implements OnClickListener, OnQueryLoadListener<Babysitter>, OnRefreshListener {

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
	private BabysittersParseQueryAdapter mAdapter;

	protected PullToRefreshLayout mPullToRefreshLayout;
	protected ExpandableLayoutListView mListView;
	private LinearLayout mFilterExpand;
	
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
		mFilterExpand = (LinearLayout) rootView.findViewById(R.id.filter_expand);
		mFilterExpand.setOnClickListener(this);
		
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
		
		mListView = (ExpandableLayoutListView ) rootView.findViewById(R.id.list);
		//mListView.setOnItemClickListener(this);

		// Retrieve the PullToRefreshLayout from the content view
		mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.carddemo_extra_ptr_layout);

		// Now setup the PullToRefreshLayout
		 ActionBarPullToRefresh.from(getActivity())
		 	// Mark All Children as pullable
		 	.allChildrenArePullable()
		 	// Set the OnRefreshListener
			.listener(this)
			// Finally commit the setup to our PullToRefreshLayout
			.setup(mPullToRefreshLayout);
		
		loadSavedPreferences();
		doListQuery();

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
		case R.id.filter_expand :
			if (mListView.getVisibility() == View.GONE) {
				mLayout.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mFilter.setText("顯示更多過濾條件");
				
			} else if (mListView.getVisibility() == View.VISIBLE) { 
				mLayout.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				mFilter.setText("隱藏更多過濾條件");
			}
			
			break;

		case R.id.save:
			
			saveAllCheckbox();
			doListQuery();

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
		mListView.setVisibility(View.VISIBLE);
		mFilter.setText("顯示更多過濾條件");
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
		//doListQuery();
	}
	
	private void doListQuery() {

		mAdapter = new BabysittersParseQueryAdapter(getActivity(), 3);
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		mAdapter.addOnQueryLoadListener(this);
		mListView.setAdapter(mAdapter);
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
			
		case R.id.action_logout:
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
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLoaded(List<Babysitter> arg0, Exception arg1) {
		hideLoading();
	}

	@Override
	public void onLoading() {
		showLoading();
	}

	@Override
	public void onRefreshStarted(View view) {
		
	}
	
	protected void showLoading() {
		ProgressBarUtils.show(getActivity());
	}
	
	protected void hideLoading() {
		ProgressBarUtils.hide(getActivity());
		mPullToRefreshLayout.setRefreshComplete();
	}


}
