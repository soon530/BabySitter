package tw.tasker.babysitter.view.impl;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.data.Favorite;
import tw.tasker.babysitter.presenter.adapter.RecordParseQueryAdapter;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterDetailView;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class BabyDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);
		setContentView(R.layout.activity_baby_detail);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(Config.BABY_OBJECT_ID, getIntent()
					.getStringExtra(Config.BABY_OBJECT_ID));
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	public static class PlaceholderFragment extends Fragment implements
			OnQueryLoadListener<BabysitterComment>, BabysitterDetailView,
			OnClickListener {

		DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();
		private Baby mBaby;
		private Favorite mFavorite;
		private String mBabyObjectId;

		private ListView mListView;
		private ImageView mBabyIcon;
		private Button mBabysitterIcon;
		private ParseQueryAdapter<BabysitterComment> mCommentAdapter;
		private View mHeaderView;
		private TextView mName;
		private TextView mNote;
		private CheckBox mFavoriteBaby;

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

			if (getArguments().containsKey(Config.BABY_OBJECT_ID)) {
				mBabyObjectId = getArguments().getString(Config.BABY_OBJECT_ID);
			}

			setHasOptionsMenu(true);
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

			return rootView;
		}

		private void initHeadUI() {
			mBabyIcon = (ImageView) mHeaderView.findViewById(R.id.baby_avator);

			mFavoriteBaby = (CheckBox) mHeaderView
					.findViewById(R.id.favorite_baby);

			mBabysitterIcon = (Button) mHeaderView
					.findViewById(R.id.babysitter_icon);

			mName = (TextView) mHeaderView.findViewById(R.id.name);
			mNote = (TextView) mHeaderView.findViewById(R.id.desciption);

			mFavoriteBaby.setOnClickListener(this);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			mListView.addHeaderView(mHeaderView);
			doDetailQuery(mBabyObjectId);
			// getFavorite();

		}

		private void doDetailQuery(String objectId) {
			ParseQuery<Baby> detailQuery = Baby.getQuery();
			detailQuery.getInBackground(objectId, new GetCallback<Baby>() {

				@Override
				public void done(Baby baby, ParseException arg1) {

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
					getFavorite();
					doCommentQuery(mBabyObjectId);
				}
			});
		}

		private void getFavorite() {
			ParseQuery<Favorite> favorite_query = Favorite.getQuery();

			favorite_query.whereEqualTo("baby", mBaby);
			favorite_query.whereEqualTo("user", ParseUser.getCurrentUser());

			favorite_query.getFirstInBackground(new GetCallback<Favorite>() {

				@Override
				public void done(Favorite favorite, ParseException e) {
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

		public void doCommentQuery(String babyObjectId) {
			mCommentAdapter = new RecordParseQueryAdapter(getActivity(),
					babyObjectId);
			mCommentAdapter.setAutoload(false);
			mCommentAdapter.setPaginationEnabled(false);
			mCommentAdapter.addOnQueryLoadListener(this);
			mCommentAdapter.loadObjects();
		}

		@Override
		public void onLoading() {
			showProgress();
		}

		@Override
		public void onLoaded(List<BabysitterComment> babysitterComment,
				Exception e) {
			mListView.setAdapter(mCommentAdapter);
			hideProgress();
		}

		@Override
		public void showProgress() {
			ProgressBarUtils.show(getActivity());
		}

		@Override
		public void hideProgress() {
			ProgressBarUtils.hide(getActivity());
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.baby_detail, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if (id == R.id.post_words) {
				Bundle bundle = new Bundle();
				bundle.putString(Config.BABY_OBJECT_ID, mBabyObjectId);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(getActivity(), BabyCommentActivity.class);
				startActivity(intent);
				return true;
			}

			if (id == R.id.refresh) {
				mCommentAdapter.loadObjects();
			}

			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.babysitter_icon:
				Intent intent = new Intent();
				intent.setClass(getActivity().getApplicationContext(),
						BabysitterDetailActivity.class);
				startActivity(intent);

				break;
			case R.id.favorite_baby:
				if (mFavoriteBaby.isChecked()) {
					mFavoriteBaby.setText("已收藏");
					addFavorite();
				} else {
					mFavoriteBaby.setText("已取消");
					deleteFavorite();
				}
				break;
			default:
				break;
			}
		}

		private void addFavorite() {
			Favorite favorite = new Favorite();
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

		private void deleteFavorite() {
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

	}
}
