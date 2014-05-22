package tw.tasker.babysitter.view;

import tw.tasker.babysitter.BabyCommentActivity;
import tw.tasker.babysitter.R;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BabyDetailActivity extends ActionBarActivity {
	private static final String[] mStrings = new String[] { "一", "二", "三", "四",
			"五", "六", "七", "八", "九" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_detail);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
			Intent intent = new Intent();
			intent.setClass(this, BabyCommentActivity.class);
			startActivity(intent);	
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 拍照後顯示圖片
		//ImageView iv = (ImageView) findViewById(R.id.imagecaptured);
		if (resultCode == RESULT_OK) {
			// 取出拍照後回傳資料
			Bundle extras = data.getExtras();
			// 將資料轉換為圖像格式
			//Bitmap bmp = (Bitmap) extras.get("data");
			// 載入ImageView
			//iv.setImageBitmap(bmp);
		}

		// 覆蓋原來的Activity
		super.onActivityResult(requestCode, resultCode, data);
	}
*/
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ListView mListView;
		private ImageView mBabyIcon;
		DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();
		private ImageButton mBabysitterIcon;

		public PlaceholderFragment() {

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true)
					.displayer(new RoundedBitmapDisplayer(20)).build();

		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setHasOptionsMenu(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baby_detail,
					container, false);
			mListView = (ListView) rootView
					.findViewById(R.id.baby_comment_list);
			mListView.setAdapter(new ArrayAdapter<String>(getActivity()
					.getApplicationContext(),
					android.R.layout.simple_list_item_1, mStrings));

			mBabyIcon = (ImageView) rootView.findViewById(R.id.baby_avator);

			imageLoader
					.displayImage(
							"https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/q77/s720x720/1966891_782022338479354_124097698_n.jpg",
							mBabyIcon, options, null);

			mBabyIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getActivity().getApplicationContext(),
							BabysitterDetailActivity.class);
					startActivity(intent);

				}
			});
			
			
			mBabysitterIcon = (ImageButton) rootView.findViewById(R.id.babysitter_icon);

			return rootView;
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
			//ImageView iv = (ImageView) findViewById(R.id.imagecaptured);
			if (resultCode == RESULT_OK) {
				// 取出拍照後回傳資料
				Bundle extras = data.getExtras();
				// 將資料轉換為圖像格式
				Bitmap bmp = (Bitmap) extras.get("data");
				// 載入ImageView
				mBabysitterIcon.setImageBitmap(bmp);
			}

			// 覆蓋原來的Activity
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	

}
