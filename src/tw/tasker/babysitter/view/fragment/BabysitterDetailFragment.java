package tw.tasker.babysitter.view.fragment;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.data.BabyFavorite;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterDetailPresenterImpl;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.BabysitterDetailView;
import tw.tasker.babysitter.view.activity.BabysitterCommentActivity;
import tw.tasker.babysitter.view.activity.BabysitterActivity;
import tw.tasker.babysitter.view.activity.BabysittersActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link BabysittersActivity} in two-pane mode (on tablets)
 * or a {@link BabysitterActivity} on handsets.
 */
public class BabysitterDetailFragment extends Fragment implements
		BabysitterDetailView, OnClickListener {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private ListView mBabysitterCommentList;

	private Button mBabyIcon;

	private String mAddress;
	private String mName;

	private TextView mBabysitterName;

	private TextView mBabysitterAddress;
	private RatingBar mBabysitterRating;

	public int mTotalRating;
	public int mTotalComment;

	private ImageView mCallIcon;

	private TextView mPhone;

	private ImageButton mDirection;

	private String mSlat;

	private String mSlng;

	private String mDlat;

	private String mDlng;

	private TextView mDistance;

	private ImageView mBabysitterImage;

	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private View mHeaderView;

	private BabysitterDetailPresenter mPresenter;

	private String mDistanceValue;

	private String mTargetLat;

	private String mTargetLng;

	private Babysitter mBabysitter;

	private String mBabysitterObjectId;

	private CheckBox mFavoriteBabysitter;
	
	private BabysitterFavorite mFavorite;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public BabysitterDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
			mBabysitterObjectId = getArguments().getString(
					Config.BABYSITTER_OBJECT_ID);
		}

		//mPresenter = new BabysitterDetailPresenterImpl(this);

		// Bundle bundle = getActivity().getIntent().getExtras();
		// mBabysitterObjectId = bundle.getString(BABYSITTER_OBJECT_ID);

		mDistanceValue = "10公尺"; // getDistance(bundle);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

		setHasOptionsMenu(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.babysitter_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(),
					BabysittersActivity.class));

			break;

		case R.id.action_comment:

			Intent intent = new Intent();

			Bundle bundle = new Bundle();
			bundle.putString(Config.BABYSITTER_OBJECT_ID, mBabysitterObjectId);
			bundle.putInt(Config.TOTAL_RATING, mTotalRating);
			bundle.putInt(Config.TOTAL_COMMENT, mTotalComment);
			intent.putExtras(bundle);

			intent.setClass(getActivity(), BabysitterCommentActivity.class);
			startActivity(intent);

		case R.id.refresh:
			mPresenter.refresh();
			
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}


	public void fillHeaderUI(Babysitter outline) {

		mAddress = outline.getAddress();
		mName = outline.getName();
		LOGD("vic", "address" + mAddress + "name" + mName);
		mBabysitterName.setText(mName);
		mBabysitterAddress.setText(mAddress);

		mTotalRating = outline.getTotalRating();
		mTotalComment = outline.getTotalComment();

		int rating = 0;
		try {
			rating = mTotalRating / mTotalComment;
		} catch (Exception e2) {
			// TODO: handle exception
		}
		mBabysitterRating.setRating(rating);

		mPhone.setText(outline.getTel());

		mCallIcon.setOnClickListener(this);
		
		mBabysitter = outline;
		getFavorite();
		mPresenter.doCommentQuery(mBabysitterObjectId);
	}
	
	
	private void getFavorite() {
		ParseQuery<BabysitterFavorite> favorite_query = BabysitterFavorite.getQuery();

		favorite_query.whereEqualTo("babysitter", mBabysitter);
		favorite_query.whereEqualTo("user", ParseUser.getCurrentUser());

		favorite_query.getFirstInBackground(new GetCallback<BabysitterFavorite>() {

			@Override
			public void done(BabysitterFavorite favorite, ParseException e) {
				if (favorite == null) {
					mFavoriteBabysitter.setChecked(false);
					mFavoriteBabysitter.setText("已取消");
				} else {
					mFavoriteBabysitter.setChecked(true);
					mFavoriteBabysitter.setText("已收藏");
					mFavorite = favorite;
				}
			}
		});
	}


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mHeaderView = inflater.inflate(R.layout.fragment_babysitter_detail_header, null);

		initHeadUI();

		View rootView = inflater.inflate(R.layout.fragment_list,
				container, false);

		mBabysitterCommentList = (ListView) rootView.findViewById(R.id.list);

		return rootView;
	}

	private void initHeadUI() {
		mBabysitterImage = (ImageView) mHeaderView
				.findViewById(R.id.babysitter_avator);

		mFavoriteBabysitter = (CheckBox) mHeaderView
				.findViewById(R.id.favorite_babysitter);

		
		mBabysitterName = (TextView) mHeaderView
				.findViewById(R.id.babysitter_name);

		mBabysitterRating = (RatingBar) mHeaderView
				.findViewById(R.id.babysitter_rating);

		mCallIcon = (ImageView) mHeaderView.findViewById(R.id.call_icon);
		mPhone = (TextView) mHeaderView.findViewById(R.id.babysitter_phone);

		mDirection = (ImageButton) mHeaderView.findViewById(R.id.direction);
		mBabysitterAddress = (TextView) mHeaderView
				.findViewById(R.id.babysitter_address);

		mDistance = (TextView) mHeaderView.findViewById(R.id.distance);

		mBabyIcon = (Button) mHeaderView.findViewById(R.id.baby_avator);

		imageLoader
				.displayImage(
						"http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg",
						mBabysitterImage, options, null);

		mDirection.setOnClickListener(this);
		mDistance.setText(mDistanceValue);
		mBabyIcon.setOnClickListener(this);
		mFavoriteBabysitter.setOnClickListener(this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mBabysitterCommentList.addHeaderView(mHeaderView);
		mPresenter.doDetailQuery(mBabysitterObjectId);
	}
	


	public void setCommentData(ParseQueryAdapter<BabysitterComment> adapter) {
		mBabysitterCommentList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.direction:
			mPresenter.doDirections(mTargetLat, mTargetLng);
			break;
		case R.id.baby_avator:

			// ParseQuery<Baby> detailQuery = Baby.getQuery();
			// detailQuery.include("babysitter");
			// detailQuery.whereEqualTo("babysitter", mBabysitter);

			// detailQuery.getFirstInBackground(new GetCallback<Baby>() {

			// @Override
			// public void done(Baby baby, ParseException e) {
			// mPresenter.seeBabyDetail(baby.getObjectId());
			// }
			// });

			mPresenter.seeBabyDetail(mBabysitterObjectId);

			break;
		case R.id.call_icon:
			mPresenter.makePhoneCall(mPhone.getText().toString());
			break;
			
		case R.id.favorite_babysitter:
			if (mFavoriteBabysitter.isChecked()) {
				mFavoriteBabysitter.setText("已收藏");
				addFavorite();
			} else {
				mFavoriteBabysitter.setText("已取消");
				deleteFavorite();
			}
			break;
		default:
			break;
		}
	}
	
	private void addFavorite() {
		BabysitterFavorite favorite = new BabysitterFavorite();
		mFavorite = favorite;
		// favorite.put("baby", mBaby);
		favorite.setBaby(mBabysitter);

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


	@Override
	public void showProgress() {
		ProgressBarUtils.show(getActivity());
	}

	@Override
	public void hideProgress() {
		ProgressBarUtils.hide(getActivity());
	}
}
