package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.presenter.adapter.RecordParseQueryAdapter;
import tw.tasker.babysitter.utils.PictureHelper;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterDetailView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BabyDetailFragment extends Fragment implements
		OnQueryLoadListener<BabyRecord>, BabysitterDetailView,
		OnClickListener, OnRefreshListener {

	public class BabyRecordSaveCallback extends SaveCallback {

		@Override
		public void done(ParseException e) {
			if (e == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"upload doen!", Toast.LENGTH_SHORT).show();
				saveComment();
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Error saving: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		}

	}
	
	private void saveComment() {
		BabyDiary babyDiary = ParseObject
				.createWithoutData(BabyDiary.class, mBabyObjectId);

		BabyRecord babyRecord = new BabyRecord();
		babyRecord.setBaby(babyDiary);
		babyRecord.setTitle("不解釋..");
		babyRecord.setDescription("");
		babyRecord.setPhotoFile(mPictureHelper.getFile());
		babyRecord.setUser(ParseUser.getCurrentUser());

		babyRecord.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getActivity().getApplicationContext(),
							"saving doen!", Toast.LENGTH_SHORT).show();
				} else {
					LOGD("vic", e.getMessage());
					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				mRingProgressDialog.dismiss();
				
				mAdapter.loadObjects();
				//getActivity().finish();
			}

		});
	}

	private ProgressDialog mRingProgressDialog;
	
	DisplayImageOptions options;
	private String mBabyObjectId;

	private RecordParseQueryAdapter mAdapter;
	private CheckBox mFavoriteBaby;
	
	private PullToRefreshLayout mPullToRefreshLayout;
	private ListView mListView;
	private PictureHelper mPictureHelper;

	public BabyDetailFragment() {
		mPictureHelper = new PictureHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Config.BABY_OBJECT_ID)) {
			mBabyObjectId = getArguments().getString(Config.BABY_OBJECT_ID);
		}

		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add) {
			Intent intent_camera = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent_camera, 0);

/*			Bundle bundle = new Bundle();
			bundle.putString(Config.BABY_OBJECT_ID, mBabyObjectId);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(getActivity(), BabyRecordAddActivity.class);
			startActivity(intent);
*/			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		mHeaderView = inflater.inflate(
//				R.layout.fragment_baby_detail_header, null);

//		initHeadUI();

		View rootView = inflater.inflate(R.layout.fragment_list,
				container, false);
		mListView = (ListView) rootView.findViewById(R.id.list);

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
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initCards();
	}

	private void initCards() {

		mAdapter = new RecordParseQueryAdapter(getActivity(), mBabyObjectId);
		mAdapter.addOnQueryLoadListener(this);
		ListView listView = (ListView) getActivity().findViewById(R.id.list);
		if (listView != null) {
			listView.setAdapter(mAdapter);
		}
	}

/*	public void doCommentQuery(String babyObjectId) {
		mAdapter = new RecordParseQueryAdapter(getActivity(), babyObjectId);
		mAdapter.setAutoload(false);
		//mAdapter.setPaginationEnabled(false);
		mAdapter.setObjectsPerPage(5);
		mAdapter.addOnQueryLoadListener(this);
		mAdapter.loadObjects();
	}
*/
	


/*	private void initHeadUI() {
		mBabyIcon = (ImageView) mHeaderView.findViewById(R.id.baby_avator);

		mFavoriteBaby = (CheckBox) mHeaderView
				.findViewById(R.id.favorite_baby);

		mBabysitterIcon = (Button) mHeaderView
				.findViewById(R.id.babysitter_icon);

		mName = (TextView) mHeaderView.findViewById(R.id.name);
		mNote = (TextView) mHeaderView.findViewById(R.id.desciption);

		mFavoriteBaby.setOnClickListener(this);
	}
*/
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//mListView.addHeaderView(mHeaderView);
		//doDetailQuery(mBabyObjectId);
		// getFavorite();

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		
		if (resultCode == BabyRecordActivity.RESULT_OK) {
			mRingProgressDialog = ProgressDialog.show(getActivity(),
					"請稍等 ...", "資料儲存中...", true);

			// 取出拍照後回傳資料
			Bundle extras = data.getExtras();
			// 將資料轉換為圖像格式
			Bitmap bmp = (Bitmap) extras.get("data");

			mPictureHelper.setBitmap(bmp);
			mPictureHelper.setSaveCallback(new BabyRecordSaveCallback());
			mPictureHelper.savePicture();

			// 載入ImageView
			//mUserAvator.setImageBitmap(bmp);
		}

		// 覆蓋原來的Activity
		super.onActivityResult(requestCode, resultCode, data);
	}


/*	private void doDetailQuery(String objectId) {
		ParseQuery<BabyDiary> detailQuery = BabyDiary.getQuery();
		detailQuery.getInBackground(objectId, new GetCallback<BabyDiary>() {

			@Override
			public void done(BabyDiary baby, ParseException arg1) {

				String url;
				if (baby.getPhotoFile() != null) {
					url = baby.getPhotoFile().getUrl();
				} else {
					url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
				}

				imageLoader.displayImage(url, mBabyIcon, options, null);

				mName.setText(baby.getName() + baby.getObjectId());
				mNote.setText(baby.getNote());
				mBaby = baby;
				//getFavorite();
				doCommentQuery(mBabyObjectId);
			}
		});
	}
*/

/*	private void getFavorite() {
		ParseQuery<BabyFavorite> favorite_query = BabyFavorite.getQuery();

		favorite_query.whereEqualTo("baby", mBaby);
		favorite_query.whereEqualTo("user", ParseUser.getCurrentUser());

		favorite_query.getFirstInBackground(new GetCallback<BabyFavorite>() {

			@Override
			public void done(BabyFavorite favorite, ParseException e) {
				if (favorite == null) {
					mFavoriteBaby.setChecked(false);
					mFavoriteBaby.setText("已取消");
				} else {
					mFavoriteBaby.setChecked(true);
					mFavoriteBaby.setText("已收藏");
					mFavorite = favorite;
				}
			}
		});
	}
*/

	@Override
	public void onLoading() {
		showProgress();
	}

	@Override
	public void onLoaded(List<BabyRecord> babysitterComment,
			Exception e) {
		//mListView.setAdapter(mAdapter);
		hideProgress();
	}

	@Override
	public void showProgress() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void hideProgress() {
		ProgressBarUtils.hide(getActivity());
		mPullToRefreshLayout.setRefreshComplete();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.babysitter_icon:
			Intent intent = new Intent();
			intent.setClass(getActivity().getApplicationContext(),
					BabysitterActivity.class);
			startActivity(intent);

			break;
		case R.id.favorite_baby:
			if (mFavoriteBaby.isChecked()) {
				mFavoriteBaby.setText("已收藏");
				//addFavorite();
			} else {
				mFavoriteBaby.setText("已取消");
				//deleteFavorite();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefreshStarted(View arg0) {
		mAdapter.loadObjects();
	}

/*	private void addFavorite() {
		BabyFavorite favorite = new BabyFavorite();
		mFavorite = favorite;
		// favorite.put("baby", mBaby);
		favorite.setBaby(mBaby);

		favorite.put("user", ParseUser.getCurrentUser());
		favorite.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					// Toast.makeText(getActivity().getApplicationContext(),
					// "saving doen!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}
*/
/*	private void deleteFavorite() {
		mFavorite.deleteInBackground(new DeleteCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					// Toast.makeText(getActivity().getApplicationContext(),
					// "deleting doen!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
*/
}