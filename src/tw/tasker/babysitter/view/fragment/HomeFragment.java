package tw.tasker.babysitter.view.fragment;

//import it.gmariotti.cardslib.demo.R;
//import it.gmariotti.cardslib.demo.cards.ColorCard;
import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.util.ArrayList;
import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.presenter.adapter.BabysittersParseQueryAdapter;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.GetLocation;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.MyLocation;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import tw.tasker.babysitter.view.activity.MessageActivity;
import tw.tasker.babysitter.view.activity.ProfileActivity;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tjerkw.slideexpandable.library.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

/**
 * List base example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class HomeFragment extends Fragment implements OnClickListener,
		OnQueryLoadListener<Babysitter>, OnRefreshListener,
		OnFocusChangeListener, OnEditorActionListener {

	protected ScrollView mScrollView;
	private LinearLayout mFilterPanel;
	private TextView mFilter;
	private Button mSave;
	private BabysittersParseQueryAdapter mAdapter;

	protected ListView mListView;
	private LinearLayout mFilterExpand;
	private LinearLayout mAddressPanel;
	private TextView mAddressText;
	private EditText mAddressEdit;
	private ImageView mLocation;
	private ImageView mArrow;
	private MenuItem mItem;
	private SubMenu mSubMenu;
	private MenuItem mLogoutItem;
	private MenuItem mProfileItem;
	private Button mCancel;
	
	private CheckBox mDayTime;
	private CheckBox mNightTime;
	private CheckBox mHalfDay;
	private CheckBox mFullDay;
	private CheckBox mPartTime;
	private CheckBox mInHouse;

	private CheckBox mKids0;
	private CheckBox mKids1;
	private CheckBox mKids2;
	private CheckBox mKids3;
	
	private CheckBox mOld40;
	private CheckBox mOld40_50;
	private CheckBox mOld50;
	
	private ArrayList<CheckBox> mTimeCheckBoxs = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> mKidsCheckBoxs = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> mAgeCheckBoxs = new ArrayList<CheckBox>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	private void initLocation() {
		// 初始化現在的位置
		// if (Config.MY_LOCATION == null) {
		MyLocation myLocation = new MyLocation(getActivity(), new GetLocation() {

			@Override
			public void done(ParseGeoPoint parseGeoPoint) {
				Config.MY_LOCATION = parseGeoPoint;
				//updateMyLocaton();
				doListQuery();
				// Config.MY_LOCATION = Config.MY_TEST_LOCATION;
				// LogUtils.LOGD("vic",
				// "get my location at ("+parseGeoPoint.getLatitude()+","+parseGeoPoint.getLongitude()+")");
			}

		});
		// }
	}

	private void updateMyLocaton() {
		if (ParseUser.getCurrentUser() != null) {
			hasUserInfo();
		}
	}
	
	private void hasUserInfo() {
		ParseQuery<UserInfo> userInfoQuery = UserInfo.getQuery();
		userInfoQuery.whereEqualTo("user", ParseUser.getCurrentUser());
		userInfoQuery.getFirstInBackground(new GetCallback<UserInfo>() {
			
			@Override
			public void done(UserInfo userInfo, ParseException e) {
				if (userInfo == null ) {
					addUserInfo();
				} else {
					updateUserInfo(userInfo);
				}
			}
		});
	}
	
	private void addUserInfo() {
		LogUtils.LOGD("vic", "addUserInfo");

		UserInfo userInfo = new UserInfo();
		userInfo.setLocation(Config.MY_LOCATION);
		userInfo.setUser(ParseUser.getCurrentUser());

		userInfo.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
				} else {
					LOGD("vic", e.getMessage());
				}
			}
		});
	}
	
	private void updateUserInfo(UserInfo userInfo) {
		LogUtils.LOGD("vic", "updateUserInfo");

		userInfo.setLocation(Config.MY_LOCATION);
		userInfo.saveInBackground();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		mFilterPanel = (LinearLayout) rootView.findViewById(R.id.filter_pannel);
		// mFilterPanel.setAlpha(0.0f);
		// mFilterPanel.setVisibility(View.INVISIBLE);
		mAddressPanel = (LinearLayout) rootView
				.findViewById(R.id.address_panel);

		mFilter = (TextView) rootView.findViewById(R.id.filter);
		mFilterExpand = (LinearLayout) rootView
				.findViewById(R.id.filter_expand);
		mFilterExpand.setOnClickListener(this);

		mAddressText = (TextView) rootView.findViewById(R.id.address_text);
		mAddressText.setOnClickListener(this);

		// Address
		mAddressEdit = (EditText) rootView.findViewById(R.id.address_edit);
		mAddressEdit.setOnFocusChangeListener(this);
		mAddressEdit.setOnEditorActionListener(this);

		mCancel = (Button) rootView.findViewById(R.id.cancel);
		mCancel.setOnClickListener(this);

		mLocation = (ImageView) rootView.findViewById(R.id.location);

		// arraw
		mArrow = (ImageView) rootView.findViewById(R.id.arrow);

		// check box
		mDayTime = (CheckBox) rootView.findViewById(R.id.day_time);
		mNightTime = (CheckBox) rootView.findViewById(R.id.night_time);

		mHalfDay = (CheckBox) rootView.findViewById(R.id.half_day);
		mFullDay = (CheckBox) rootView.findViewById(R.id.full_day);
		
		mPartTime = (CheckBox) rootView.findViewById(R.id.part_time);
		mInHouse = (CheckBox) rootView.findViewById(R.id.in_house);
		

		mKids0 = (CheckBox) rootView.findViewById(R.id.kids_0);
		mKids1 = (CheckBox) rootView.findViewById(R.id.kids_1);
		mKids2 = (CheckBox) rootView.findViewById(R.id.kids_2);
		mKids3 = (CheckBox) rootView.findViewById(R.id.kids_3);

		mOld40 = (CheckBox) rootView.findViewById(R.id.old_40);
		mOld40_50 = (CheckBox) rootView.findViewById(R.id.old_40_50);
		mOld50 = (CheckBox) rootView.findViewById(R.id.old_50);

		mSave = (Button) rootView.findViewById(R.id.save);
		mSave.setOnClickListener(this);

		mListView = (ListView) rootView.findViewById(R.id.list);
		// mListView.setOnItemClickListener(this);

		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int threshold = 1;
				int count = mListView.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if (mListView.getLastVisiblePosition() >= count
							- threshold) {
						mAdapter.loadNextPage();
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
		initPanel();
		loadSavedPreferences();
		
		initCheckboxs();
		setCheckBoxsListener();

		return rootView;
	}
	
	private void initCheckboxs() {
		mTimeCheckBoxs.add(mDayTime);
		mTimeCheckBoxs.add(mNightTime);
		mTimeCheckBoxs.add(mHalfDay);
		mTimeCheckBoxs.add(mFullDay);
		mTimeCheckBoxs.add(mPartTime);
		mTimeCheckBoxs.add(mInHouse);
		
		mKidsCheckBoxs.add(mKids0);
		mKidsCheckBoxs.add(mKids1);
		mKidsCheckBoxs.add(mKids2);
		mKidsCheckBoxs.add(mKids3);
		
		mAgeCheckBoxs.add(mOld40);
		mAgeCheckBoxs.add(mOld40_50);
		mAgeCheckBoxs.add(mOld50);
		
	}

	private void setCheckBoxsListener() {
		for (CheckBox item : mTimeCheckBoxs) {
			item.setOnClickListener(this);
		}
		for (CheckBox item : mKidsCheckBoxs) {
			item.setOnClickListener(this);
		}
		for (CheckBox item : mAgeCheckBoxs) {
			item.setOnClickListener(this);
		}
	}

	private void initPanel() {
		mFilterPanel.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		mCancel.setVisibility(View.GONE);
		mAddressEdit.setVisibility(View.GONE);
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

					view.getViewTreeObserver().removeOnGlobalLayoutListener(
							this);
				} else {
					view.getViewTreeObserver().removeGlobalOnLayoutListener(
							this);
				}

				// get width and height of the view
			}
		});

		// mFilterPanel.setTranslationY(mFilterPanel.getHeight());
	}

	private void loadSavedPreferences() {
		setCheckBox(mDayTime, "mDayTime");
		setCheckBox(mNightTime, "mNightTime");
		setCheckBox(mHalfDay, "mHalfDay");
		setCheckBox(mFullDay, "mFullDay");
		setCheckBox(mPartTime, "mPartTime");
		setCheckBox(mInHouse, "mInHouse");

		setCheckBox(mKids0, "mKids0");
		setCheckBox(mKids1, "mKids1");
		setCheckBox(mKids2, "mKids2");
		setCheckBox(mKids3, "mKids3");

		setCheckBox(mOld40, "mOld40");
		setCheckBox(mOld40_50, "mOld40_50");
		setCheckBox(mOld50, "mOld50");
	}

	private void setCheckBox(CheckBox checkBox, String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		boolean checkBoxValue = sharedPreferences.getBoolean(key, false);
		if (checkBoxValue) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}

	}

	@Override
	public void onClick(View v) {
		boolean checkBoxStatus = false;
		int id = v.getId();
		switch (id) {
		case R.id.filter_expand:
			if (mListView.getVisibility() == View.GONE) { // hide

				hideFilterPanel();

			} else if (mListView.getVisibility() == View.VISIBLE) { // show
				showFilterPanel();
			}
			// mArrow.animate().rotationBy(180).start();

			break;

		case R.id.save:
			saveAllCheckbox();
			doListQuery();

			break;

		case R.id.address_text:
			changeToAddressEditMode();
			break;

		case R.id.cancel:
			changeToAndressTextMode();
			break;
		
		// time
		case R.id.day_time:
			clearTimeCheckboxs(R.id.day_time);
			break;
		case R.id.night_time:
			clearTimeCheckboxs(R.id.night_time);
			break;
		case R.id.half_day:
			clearTimeCheckboxs(R.id.half_day);
			break;
		case R.id.full_day:
			clearTimeCheckboxs(R.id.full_day);
			break;
		case R.id.part_time:
			clearTimeCheckboxs(R.id.part_time);
			break;
		case R.id.in_house:
			clearTimeCheckboxs(R.id.in_house);
			break;
		
		// kids
		case R.id.kids_0:
			clearKidsCheckBoxs(R.id.kids_0);
			break;
		case R.id.kids_1:
			clearKidsCheckBoxs(R.id.kids_1);
			break;
		case R.id.kids_2:
			clearKidsCheckBoxs(R.id.kids_2);
			break;
		case R.id.kids_3:
			clearKidsCheckBoxs(R.id.kids_3);
			break;
		
		// age
		case R.id.old_40:
			clearAgeCheckBoxs(R.id.old_40);
			break;
		case R.id.old_40_50:
			clearAgeCheckBoxs(R.id.old_40_50);
			break;
		case R.id.old_50:
			clearAgeCheckBoxs(R.id.old_50);
			break;

		default:
			break;
		}

	}
	
	private void clearTimeCheckboxs(int r) {
		for (CheckBox item : mTimeCheckBoxs) {
			if (item.getId() == r) {
			} else {
				item.setChecked(false);
			}
		}
	}
	
	private void clearKidsCheckBoxs(int r) {
		for (CheckBox item : mKidsCheckBoxs) {
			if (item.getId() == r) {
			} else {
				item.setChecked(false);
			}
		}
	}
	
	private void clearAgeCheckBoxs(int r) {
		for (CheckBox item : mAgeCheckBoxs) {
			if (item.getId() == r) {
			} else {
				item.setChecked(false);
			}
		}
	}

	private void changeToAndressTextMode() {
		mAddressText.setVisibility(View.VISIBLE);
		mAddressEdit.setVisibility(View.GONE);
		mCancel.setVisibility(View.GONE);
		mLocation.setVisibility(View.VISIBLE);
		DisplayUtils.toggleKeypad(getActivity());
	}

	private void changeToAddressEditMode() {
		mAddressText.setVisibility(View.GONE);
		mAddressEdit.setVisibility(View.VISIBLE);
		mLocation.setVisibility(View.GONE);
		// mAddressEdit.setFocusable(true);
		// mAddressEdit.setFocusableInTouchMode(true);
		mAddressEdit.requestFocus();
		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		mCancel.setVisibility(View.VISIBLE);
		DisplayUtils.toggleKeypad(getActivity());
	}

	private void showFilterPanel() {
		mListView.setVisibility(View.GONE);
		mFilterPanel.setVisibility(View.VISIBLE);
		mAddressPanel.setVisibility(View.GONE);

		mFilterPanel.animate().translationY(0.0f).alpha(1.0f).setDuration(250)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						// mFilterPanel.setVisibility(View.VISIBLE);
						mFilter.setText("隱藏更多過濾條件");
					}
				});
		mArrow.animate().rotation(180).start();

		// mFilterPanel.setVisibility(View.VISIBLE);
	}

	private void hideFilterPanel() {
		mFilterPanel.animate().translationY(-mFilterPanel.getHeight())
				.alpha(0.0f).setDuration(250)
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

		// mFilterPanel.setVisibility(View.GONE);

	}

	private void saveAllCheckbox() {

		savePreferences("mDayTime", mDayTime.isChecked());
		savePreferences("mNightTime", mNightTime.isChecked());
		savePreferences("mHalfDay", mHalfDay.isChecked());
		savePreferences("mFullDay", mFullDay.isChecked());
		savePreferences("mPartTime", mPartTime.isChecked());
		savePreferences("mInHouse", mInHouse.isChecked());

		savePreferences("mKids0", mKids0.isChecked());
		savePreferences("mKids1", mKids1.isChecked());
		savePreferences("mKids2", mKids2.isChecked());
		savePreferences("mKids3", mKids3.isChecked());

		savePreferences("mOld40", mOld40.isChecked());
		savePreferences("mOld40_50", mOld40_50.isChecked());
		savePreferences("mOld50", mOld50.isChecked());

		hideFilterPanel();

		Toast.makeText(getActivity(), "過慮條件，已儲存!", Toast.LENGTH_LONG).show();
	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initLocation();
		//doListQuery();
	}

	private void doListQuery() {

		mAdapter = new BabysittersParseQueryAdapter(getActivity(), 3);
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		//mAdapter.setPaginationEnabled(false);
		mAdapter.addOnQueryLoadListener(this);
		
		SlideExpandableListAdapter slideAdapter = new SlideExpandableListAdapter(
				mAdapter,
                R.id.expandable_toggle_button,
                R.id.expandable);
		slideAdapter.setItemExpandCollapseListener(new OnItemExpandCollapseListener() {
			
			@Override
			public void onExpand(View itemView, int position) {
				
				View view = getViewByPosition(position, mListView);
				
				ImageView arrow = (ImageView) view.findViewById(R.id.arrow);
				arrow.animate().rotation(180).start();
				mAdapter.setExpandableObjectID(mAdapter.getItem(position).getObjectId());
			}
			
			@Override
			public void onCollapse(View itemView, int position) {
				View view = getViewByPosition(position, mListView);
				ImageView arrow = (ImageView) view.findViewById(R.id.arrow);
				arrow.animate().rotation(0).start();				
				mAdapter.setExpandableObjectID("");
			}
		});
		
		mListView.setAdapter(slideAdapter);		

	}
		
	public View getViewByPosition(int pos, ListView listView) {
	    final int firstListItemPosition = listView.getFirstVisiblePosition();
	    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

	    if (pos < firstListItemPosition || pos > lastListItemPosition ) {
	        return listView.getAdapter().getView(pos, null, listView);
	    } else {
	        final int childIndex = pos - firstListItemPosition;
	        return listView.getChildAt(childIndex);
	    }
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home, menu);

		mItem = menu.findItem(R.id.action_settings);
		// LogUtils.LOGD("vic", "sub menu :" + mItem.hasSubMenu());
		mSubMenu = mItem.getSubMenu();
		// LogUtils.LOGD("vic", "getSubMenu() :" + mSubMenu.hasVisibleItems());
		mLogoutItem = mSubMenu.findItem(R.id.action_logout);
		mProfileItem = mSubMenu.findItem(R.id.action_profile);
		// LogUtils.LOGD("vic", "loginout :" + mLogoutItem );
		// mLogoutItem.setTitle("這是測試");

		if (ParseUser.getCurrentUser() == null) {
			mLogoutItem.setTitle("登入");
			mProfileItem.setVisible(false);

		} else {
			mLogoutItem.setTitle("登出");
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		String uri = "";
		Intent intent;

		switch (id) {

		case R.id.message:
			intent = new Intent();
			intent.setClass(getActivity(), MessageActivity.class);
			startActivity(intent);
			
			break;
		
		case R.id.action_profile:
			intent = new Intent();
			intent.setClass(getActivity(), ProfileActivity.class);
			startActivity(intent);

			break;

		// case R.id.action_google_play:
		// uri = "market://details?id=tw.tasker.babysitter";
		// intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		// startActivity(intent);
		// break;
		case R.id.action_fb:
			uri = "fb://page/765766966779332";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			startActivity(intent);
			break;

		case R.id.action_gmail:
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "service@babytone.cc" });
			intent.putExtra(Intent.EXTRA_SUBJECT, "BabyCare意見回饋");
			// intent.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(intent, "BabyCare意見回饋"));
			break;

		case R.id.action_logout:
			if (ParseUser.getCurrentUser() == null) { // 沒有登入
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
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			changeToAndressTextMode();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		String addr = mAddressEdit.getText().toString();
		
		if (addr.isEmpty()) { 
			mAddressText.setText("範例：高雄市鳳山區");
		} else {
			mAddressText.setText(addr);
		}
		Config.keyWord = addr;

		mAddressText.setVisibility(View.VISIBLE);
		mAddressEdit.setVisibility(View.GONE);
		mLocation.setVisibility(View.VISIBLE);
		DisplayUtils.toggleKeypad(getActivity());
		doListQuery();

		return true;
	}

}
