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
import tw.tasker.babysitter.presenter.adapter.BabysittersParseQueryAdapter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;

/**
 * List base example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class NewHomeFragment extends Fragment implements OnClickListener, OnQueryLoadListener<Babysitter>, OnRefreshListener, OnFocusChangeListener, OnEditorActionListener {

	protected ScrollView mScrollView;
	private LinearLayout mFilterPanel;
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
	private LinearLayout mAddressPanel;
	private TextView mAddressText;
	private EditText mAddressEdit;
	private ImageView mLocation;
	private ImageView mArrow;
	private MenuItem mItem;
	private SubMenu mSubMenu;
	private MenuItem mLogoutItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
		
		mFilterPanel = (LinearLayout) rootView.findViewById(R.id.filter_pannel);
		//mFilterPanel.setAlpha(0.0f);
		//mFilterPanel.setVisibility(View.INVISIBLE);
		mAddressPanel = (LinearLayout) rootView.findViewById(R.id.address_panel);
		
		mFilter = (TextView) rootView.findViewById(R.id.filter);
		mFilterExpand = (LinearLayout) rootView.findViewById(R.id.filter_expand);
		mFilterExpand.setOnClickListener(this);
		
		mAddressText = (TextView) rootView.findViewById(R.id.address_text);
		mAddressText.setOnClickListener(this);
		
		mAddressEdit = (EditText) rootView.findViewById(R.id.address_edit);
		mAddressEdit.setVisibility(View.GONE);
		mAddressEdit.setOnFocusChangeListener(this);
		mAddressEdit.setOnEditorActionListener(this);
		
		mLocation = (ImageView) rootView.findViewById(R.id.location);
		
		// arraw
		mArrow = (ImageView) rootView.findViewById(R.id.arrow);
		
		
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
	
	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		final ViewTreeObserver observer = view.getViewTreeObserver();
	    observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        public void onGlobalLayout() {
	            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	        		mFilterPanel.setY(-mFilterPanel.getHeight());

	        		view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
	            } else {
	            	view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	            }

	            // get width and height of the view
	        }});

		//mFilterPanel.setTranslationY(mFilterPanel.getHeight());

	    
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
			if (mListView.getVisibility() == View.GONE) { //hide
				
				hideFilterPanel();
				
				
			} else if (mListView.getVisibility() == View.VISIBLE) { // show
				showFilterPanel();
			}
			//mArrow.animate().rotationBy(180).start();

			
			break;

		case R.id.save:
			saveAllCheckbox();
			doListQuery();

			break;
		case R.id.address_text:
			mAddressText.setVisibility(View.GONE);
			mAddressEdit.setVisibility(View.VISIBLE);
			mLocation.setVisibility(View.GONE);
			//mAddressEdit.setFocusable(true);
			//mAddressEdit.setFocusableInTouchMode(true);
			mAddressEdit.requestFocus();
			//getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			showKeyboard();
			break;
		default:
			break;
		}
		
	}
	
	private void showFilterPanel() {
		mListView.setVisibility(View.GONE);
        mFilterPanel.setVisibility(View.VISIBLE);
		mAddressPanel.setVisibility(View.GONE);
		
		mFilterPanel.animate()
        .translationY(0.0f)
        .alpha(1.0f)
        .setDuration(1000)
        .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //mFilterPanel.setVisibility(View.VISIBLE);
				mFilter.setText("隱藏更多過濾條件");
            }
        });
		mArrow.animate().rotation(180).start();

		//mFilterPanel.setVisibility(View.VISIBLE);
	}

	private void hideFilterPanel() {
		mFilterPanel.animate()
        .translationY(-mFilterPanel.getHeight())
        .alpha(0.0f)
        .setDuration(1000)
        .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFilterPanel.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mAddressPanel.setVisibility(View.VISIBLE);
				mFilter.setText("顯示更多過濾條件");
            }
        });
		
		mArrow.animate().rotation(360).start();

		//mFilterPanel.setVisibility(View.GONE);
		
	}

	// performance issue
	private void showKeyboard() {
		InputMethodManager inputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    inputMethodManager.toggleSoftInputFromWindow(mAddressEdit.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);		
	}

	private void hideKeyboard() {
		InputMethodManager inputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    inputMethodManager.toggleSoftInputFromWindow(mAddressEdit.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS, 0);		
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

		hideFilterPanel();
		
		Toast.makeText(getActivity(), "過慮條件，已儲存!",
				Toast.LENGTH_LONG).show();
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
		
		mItem = menu.findItem(R.id.action_settings);
		//LogUtils.LOGD("vic", "sub menu :" + mItem.hasSubMenu());
		mSubMenu = mItem.getSubMenu();
		//LogUtils.LOGD("vic", "getSubMenu() :" + mSubMenu.hasVisibleItems());
		mLogoutItem = mSubMenu.findItem(R.id.action_logout);
		//LogUtils.LOGD("vic", "loginout :" + mLogoutItem );
		//mLogoutItem.setTitle("這是測試");
		
	    if (ParseUser.getCurrentUser()==null) {
	    	mLogoutItem.setTitle("登入");
	    	
	    } else {
	    	mLogoutItem.setTitle("登出");
	    }

		
		//mLogoutItem = menu.findItem(R.id.action_settings).getSubMenu().getItem(0);
		
		//mItem = menu.findItem(R.id.action_settings);
		//mSubMenu = mItem.getSubMenu().getItem(R.id.);
		//mLogoutItem = mSubMenu.findItem(R.id.action_logout);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		String uri = "";
		Intent intent;

		switch (id) {
//		case R.id.action_google_play:
//			uri = "market://details?id=tw.tasker.babysitter";
//			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//			startActivity(intent);
//			break;
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

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			mAddressText.setVisibility(View.VISIBLE);
			mAddressEdit.setVisibility(View.GONE);
			mLocation.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		String addr = mAddressEdit.getText().toString();
		mAddressText.setText(addr);
		Config.keyWord= addr;
		
		mAddressText.setVisibility(View.VISIBLE);
		mAddressEdit.setVisibility(View.GONE);
		mLocation.setVisibility(View.VISIBLE);
		doListQuery();
		hideKeyboard();
		
		return true;
	}


}
