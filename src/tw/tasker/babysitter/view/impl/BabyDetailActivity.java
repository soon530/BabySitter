package tw.tasker.babysitter.view.impl;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.data.Favorite;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenter;
import tw.tasker.babysitter.presenter.adapter.RecordParseQueryAdapter;
import tw.tasker.babysitter.view.BabysitterDetailView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BabyDetailActivity extends ActionBarActivity implements
		BabysitterDetailView {

	private BabysitterDetailPresenter mPresenter;

	private String mObjectId;

	//private String mObjectId;

	private static final String[] mStrings = new String[] { "一", "二", "三", "四",
			"五", "六", "七", "八", "九" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_detail);

		Bundle bundle = getIntent().getExtras();
		mObjectId = bundle.getString("objectId");

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(mObjectId)).commit();
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baby_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.post_words) {
			Bundle bundle = new Bundle();
			bundle.putString("objectId", mObjectId);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(this, BabyCommentActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { // 拍照後顯示圖片 //ImageView iv = (ImageView)
	 * findViewById(R.id.imagecaptured); if (resultCode == RESULT_OK) { //
	 * 取出拍照後回傳資料 Bundle extras = data.getExtras(); // 將資料轉換為圖像格式 //Bitmap bmp =
	 * (Bitmap) extras.get("data"); // 載入ImageView //iv.setImageBitmap(bmp); }
	 * 
	 * // 覆蓋原來的Activity super.onActivityResult(requestCode, resultCode, data); }
	 */
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnQueryLoadListener<BabysitterComment> {

		private ListView mListView;
		private ImageView mBabyIcon;
		DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();
		private Button mBabysitterIcon;
		ParseQueryAdapter<BabysitterComment> mCommentAdapter;

		private View mHeaderView;
		private Button mHeart;
		private int count;
		private TextView mName;
		private TextView mNote;
		private ToggleButton mStar;
		
		private Baby mBaby;
		private Favorite mFavorite;
		private boolean isInitData;
		private String mObjectId;


		public PlaceholderFragment(String objectId) {

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true)
					.displayer(new RoundedBitmapDisplayer(20)).build();

			mObjectId = objectId;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			mHeaderView = inflater.inflate(
					R.layout.fragment_baby_detail_header, null);

			initHeadUI();

			View rootView = inflater.inflate(R.layout.fragment_baby_detail,
					container, false);
			mListView = (ListView) rootView
					.findViewById(R.id.baby_comment_list);

			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Intent refresh =new Intent(getActivity(),
					// BabyDetailActivity.class);

					// startActivity(refresh);

					// getActivity().finish();

				}
			});

			return rootView;
		}

		private void initHeadUI() {
			mBabyIcon = (ImageView) mHeaderView.findViewById(R.id.baby_avator);

			mBabyIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getActivity().getApplicationContext(),
							BabysitterDetailActivity.class);
					startActivity(intent);

				}
			});

			mBabysitterIcon = (Button) mHeaderView
					.findViewById(R.id.babysitter_icon);

			mName = (TextView) mHeaderView.findViewById(R.id.name);
			mNote = (TextView) mHeaderView.findViewById(R.id.desciption);

			mHeart = (Button) mHeaderView.findViewById(R.id.heart);

			mHeart.setOnClickListener(

			new OnClickListener() {
				@Override
				public void onClick(View v) {
					// mHeart.setText("♥ +" + ++count);
					saveHeard();
				}

				private void saveHeard() {
					ParseQuery<Baby> query = Baby.getQuery();

					query.getInBackground(mObjectId,
							new GetCallback<Baby>() {

								@Override
								public void done(Baby baby, ParseException e) {
									baby.put("favorite", ++count);
									baby.saveInBackground(new SaveCallback() {

										@Override
										public void done(ParseException e) {
											if (e == null) {
												// setResult(RESULT_OK);
												// finish();
												Toast.makeText(
														getActivity()
																.getApplicationContext(),
														"saving doen!",
														Toast.LENGTH_SHORT)
														.show();
												mHeart.setText("♥ +" + count);
											} else {
												LOGD("vic", e.getMessage());
												Toast.makeText(
														getActivity()
																.getApplicationContext(),
														"Error saving: "
																+ e.getMessage(),
														Toast.LENGTH_SHORT)
														.show();
											}

										}
									});

								}
							});
				}

			});
			
			
			mStar = (ToggleButton) mHeaderView.findViewById(R.id.star);
			
			mStar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
/*						Toast.makeText(
								getActivity()
										.getApplicationContext(),
								"加入收藏..",
								Toast.LENGTH_SHORT)
								.show();
*/
						
						saveStar();
						
					}else{
/*						Toast.makeText(
								getActivity()
										.getApplicationContext(),
								"取消收藏..",
								Toast.LENGTH_SHORT)
								.show();
*/						
						deleteStar();

					}
				}

				private void deleteStar() {
					//如果是第一次抓data，只是要改變狀態而已，不是要收藏
					if(isInitData) {
						isInitData = false;
						return ;
					}

					
					mFavorite.deleteInBackground(new DeleteCallback() {
						
						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast.makeText(
										getActivity().getApplicationContext(),
										"deleting doen!", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										getActivity().getApplicationContext(),
										"Error saving: " + e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						}
					});					
				}

				private void saveStar() {
					
					//如果是第一次抓data，只是要改變狀態而已，不是要收藏
					if(isInitData == true) {
						isInitData = false;
						return ;
					}
					
					// ParseObject post = new ParseObject("Comment");
					Favorite favorite = new Favorite();
					mFavorite = favorite;
					//favorite.put("baby", mBaby);
					favorite.setBaby(mBaby);
					
					favorite.put("user", ParseUser.getCurrentUser());

					// Save the post and return
					favorite.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							if (e == null) {
								// setResult(RESULT_OK);
								// finish();
								Toast.makeText(
										getActivity().getApplicationContext(),
										"saving doen!", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										getActivity().getApplicationContext(),
										"Error saving: " + e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						}

					});
				}
			});
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			mListView.addHeaderView(mHeaderView);

			doCommentQuery(mObjectId);

			doDetailQuery(mObjectId);

			//getFavorite();

			/*
			 * mListView.setAdapter(new ArrayAdapter<String>(getActivity()
			 * .getApplicationContext(), android.R.layout.simple_list_item_1,
			 * mStrings));
			 */
		}
		
		private void getFavorite() {
			ParseQuery<Favorite> favorite_query = Favorite.getQuery();
			
			favorite_query.whereEqualTo("baby", mBaby);
			favorite_query.whereEqualTo("user", ParseUser.getCurrentUser());
			
			favorite_query.getFirstInBackground(new GetCallback<Favorite>() {


				@Override
				public void done(Favorite favorite, ParseException e) {
					//第一次抓data
					isInitData = true;
					
					if (favorite == null) {
						mStar.setChecked(false);
						isInitData = false;
					}else {
						mStar.setChecked(true);
						mFavorite = favorite;

						Toast.makeText(
								getActivity()
										.getApplicationContext(),
								"ObjectId = " + favorite.getObjectId(),
								Toast.LENGTH_SHORT)
								.show();
					}
					

				}
			});
			
		}


		private void doDetailQuery(String objectId) {
			ParseQuery<Baby> detailQuery = Baby.getQuery();
			detailQuery.getInBackground(objectId, new GetCallback<Baby>() {


				@Override
				public void done(Baby baby, ParseException arg1) {

					String url;
					if(baby.getPhotoFile() != null) {
						url = baby.getPhotoFile().getUrl();
					} else {
						url = "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg";
					}
					
					imageLoader
					.displayImage(
							url,
							mBabyIcon, options, null);

					mName.setText(baby.getName() + baby.getObjectId());
					mNote.setText(baby.getNote());
					mHeart.setText("♥ +" + baby.getFavorite());
					count = baby.getFavorite();
					mBaby = baby;
					getFavorite();
				}

			});

		}

		public void doCommentQuery(String objectId) {

			mCommentAdapter = new RecordParseQueryAdapter(getActivity(),
					getFactory(objectId));

			// Disable automatic loading when the list_item_babysitter_comment
			// is
			// attached to a view.
			mCommentAdapter.setAutoload(false);

			// Disable pagination, we'll manage the query limit ourselves
			mCommentAdapter.setPaginationEnabled(false);

			mCommentAdapter.addOnQueryLoadListener(this);

			mCommentAdapter.loadObjects();
		}

		public ParseQueryAdapter.QueryFactory<BabysitterComment> getFactory(
				final String objectId) {
			// Set up a customized query
			ParseQueryAdapter.QueryFactory<BabysitterComment> factory = new ParseQueryAdapter.QueryFactory<BabysitterComment>() {
				public ParseQuery<BabysitterComment> create() {
					// Location myLoc = (currentLocation == null) ? lastLocation
					// :
					// currentLocation;
					ParseQuery<BabysitterComment> query = BabysitterComment
							.getQuery();
					// query.include("user");
					query.orderByDescending("createdAt");
					query.whereEqualTo("babysitterId", objectId);
					// query.whereWithinKilometers("location",
					// geoPointFromLocation(myLoc), radius * METERS_PER_FEET /
					// METERS_PER_KILOMETER);
					query.setLimit(20);
					return query;
				}
			};
			return factory;
		}

		@Override
		public void onLoading() {
			mListView.setAdapter(mCommentAdapter);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.baby_detail, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			Intent intent_camera = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent_camera, 0);

			return false;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// 拍照後顯示圖片
			// ImageView iv = (ImageView) findViewById(R.id.imagecaptured);
			if (resultCode == RESULT_OK) {
				// 取出拍照後回傳資料
				Bundle extras = data.getExtras();
				// 將資料轉換為圖像格式
				Bitmap bmp = (Bitmap) extras.get("data");
				// 載入ImageView
				// mBabysitterIcon.setImageBitmap(bmp);
			}

			// 覆蓋原來的Activity
			super.onActivityResult(requestCode, resultCode, data);
		}

		@Override
		public void onLoaded(List<BabysitterComment> arg0, Exception arg1) {
			// TODO Auto-generated method stub

		}

	}

}
