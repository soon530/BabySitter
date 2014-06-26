package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyFavorite;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.presenter.adapter.RecordParseQueryAdapter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.PictureHelper;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterDetailView;
import tw.tasker.babysitter.view.fragment.BaseFragment;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BabyRecordFragment extends BaseFragment implements
		OnQueryLoadListener<BabyRecord> {

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

		final BabyRecord babyRecord = new BabyRecord();
		babyRecord.setBaby(babyDiary);
		babyRecord.setTitle("不解釋...");
		String name = "";// ParseUser.getCurrentUser().getUsername() + "說: ";
		babyRecord.setDescription(name + "不解釋...");
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
				
				updateBabyDiary(babyRecord);
				
				//getActivity().finish();
			}

		});
	}
	
	
	private void updateBabyDiary(final BabyRecord babyRecord) {
		
		ParseQuery<BabyDiary> query = BabyDiary.getQuery();
		
/*		if (!DisplayUtils.hasNetwork(getActivity())) {
			query.fromLocalDatastore();
		}
*/		
		query.getInBackground(mBabyObjectId, new GetCallback<BabyDiary>() {

			@Override
			public void done(BabyDiary babyDiary, ParseException e) {
				babyDiary.setTotalRecord(++mTotalRecord);
				babyDiary.setBabyRecord(babyRecord);
				babyDiary.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						mRingProgressDialog.dismiss();
						mAdapter.loadObjects();
					}
				});
			}
		});
		
		//ParseQuery<ParseObject> query = ParseQuery.getQuery("BabyDiary");

/*		query.getInBackground(mBabyObjectId,
				new GetCallback<ParseObject>() {

					public void done(ParseObject babysitter,
							ParseException e) {
						if (e == null) {
							int r = (int) mBabysitterRating.getRating();
							babysitter.put("totalRating", mTotalRating + r);
							babysitter.put("totalComment",
									mTotalComment + 1);
							babysitter.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException e) {
									if (e == null) {
										mRingProgressDialog.dismiss();
										getActivity().finish();

									}
								}
							});

						}
					}
				});
*/	
	}

	
	

	private ProgressDialog mRingProgressDialog;
	
	private String mBabyObjectId;

	private RecordParseQueryAdapter mAdapter;
	
	private PictureHelper mPictureHelper;

	private int mTotalRecord;

	private boolean mIsChecked;
	private MenuItem mFavoriteItem;
	private BabyFavorite mBabyFavorite;

	public BabyRecordFragment() {
		mPictureHelper = new PictureHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Config.BABY_OBJECT_ID)) {
			mBabyObjectId = getArguments().getString(Config.BABY_OBJECT_ID);
			mTotalRecord = getArguments().getInt(Config.TOTAL_RECORD);
		}

	}
	
	@Override
	protected Boolean loadGridView() {
		return false;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_favorite, menu);
		mFavoriteItem = menu.findItem(R.id.action_favorite);
		getFavorite();
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.action_add:
			
			//if (DisplayUtils.hasNetwork(getActivity())) {
				Intent intent_camera = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 0);
			//}else {
				//Toast.makeText(getActivity(), "新增[成長記錄]需開啟網路...", Toast.LENGTH_SHORT).show();
			//}
			
			break;

		case R.id.action_favorite:
			if (mIsChecked) {
				item.setTitle("未收藏");
				deleteFavorite();
			} else {
				item.setTitle("已收藏");
				addFavorite();
			}
			mIsChecked = !mIsChecked;
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		doListQuery();
	}

	private void doListQuery() {

		mAdapter = new RecordParseQueryAdapter(getActivity(), mBabyObjectId, this);
		mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
		mAdapter.addOnQueryLoadListener(this);
		mListView.setAdapter(mAdapter);
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
		}

		// 覆蓋原來的Activity
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getFavorite() {
		BabyDiary babyDiary = ParseObject.createWithoutData(BabyDiary.class, mBabyObjectId);
		ParseQuery<BabyFavorite> favorite_query = BabyFavorite.getQuery();

		favorite_query.whereEqualTo("BabyDiary", babyDiary);
		favorite_query.whereEqualTo("user", ParseUser.getCurrentUser());
		
/*		if (!DisplayUtils.hasNetwork(getActivity())) {
			favorite_query.fromLocalDatastore();
		}
*/		
		favorite_query.getFirstInBackground(new GetCallback<BabyFavorite>() {
			@Override
			public void done(BabyFavorite babyFavorite, ParseException e) {
				
				if (e != null) {
					LogUtils.LOGD("vic", e.getMessage());
				}
				
				if (babyFavorite == null) {
					mIsChecked = false;
					mFavoriteItem.setTitle("未收藏");
				} else {
					mIsChecked = true;
					mFavoriteItem.setTitle("已收藏");
					mBabyFavorite = babyFavorite;
				}
			}
		});
	}


	@Override
	public void onLoading() {
		showLoading();
	}

	@Override
	public void onLoaded(List<BabyRecord> babyRecords,
			Exception e) {
		
/*		if (DisplayUtils.hasNetwork(getActivity())) {
			ParseObject.pinAllInBackground(babyRecords);
		}
*/
		
		hideLoading();
	}

	@Override
	public void onRefreshStarted(View arg0) {
		mAdapter.loadObjects();
	}

	private void addFavorite() {
		BabyDiary babyDiary = ParseObject.createWithoutData(BabyDiary.class, mBabyObjectId);
		BabyFavorite babyFavorite = new BabyFavorite();
		mBabyFavorite = babyFavorite;
		babyFavorite.setBabyDiary(babyDiary);
		/* TODO 
		 * WorkAround 因為在[我的收藏]沒辦法有效的透過BabyDiary裡的BabyRecord取得資料，
		 * 所以在存[寶寶收藏]的時候，也把BabyRecord存到BabyFavorite裡去
		 */		
		babyFavorite.setBabyRecord(mAdapter.getItem(0));
		babyFavorite.setUser(ParseUser.getCurrentUser());
		
		babyFavorite.saveInBackground(new SaveCallback() {
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

	private void deleteFavorite() {
		mBabyFavorite.deleteInBackground(new DeleteCallback() {

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
}