package tw.tasker.babysitter.view.impl;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.util.List;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.dummy.DummyContent;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.data.BabysitterOutline;
import tw.tasker.babysitter.presenter.BabysitterDetailPresenter;
import tw.tasker.babysitter.presenter.impl.BabysitterDetailPresenterImpl;
import tw.tasker.babysitter.view.BabysitterDetailView;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link BabysitterListActivity} in two-pane mode (on tablets)
 * or a {@link BabysitterDetailActivity} on handsets.
 */
public class BabysitterDetailFragment extends Fragment implements
		BabysitterDetailView, OnClickListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;
	private ListView mBabysitterCommentList;

	private Button mBabyIcon;
	private static final String[] mStrings = new String[] { "一", "二", "三", "四",
			"五", "六", "七", "八", "九" };

	private String mAddress;
	private String mName;

	private TextView mBabysitterName;

	private TextView mBabysitterAddress;
	private RatingBar mBabysitterRating;

	private String objectId;

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

	private BabysitterOutline mOutline;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public BabysitterDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}

		mPresenter = new BabysitterDetailPresenterImpl(this);

		Bundle bundle = getActivity().getIntent().getExtras();
		objectId = bundle.getString("objectId");

		mDistanceValue = "10公尺"; // getDistance(bundle);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	private String getDistance(Bundle bundle) {

		mSlat = bundle.getString("slat");
		mSlng = bundle.getString("slng");
		mDlat = bundle.getString("dlat");
		mDlng = bundle.getString("dlng");

		mTargetLat = mSlat;
		mTargetLng = mSlng;

		double distance = 0;
		Location locationA = new Location("A");
		locationA.setLatitude(Double.valueOf(mSlat).doubleValue());
		locationA.setLongitude(Double.valueOf(mSlng).doubleValue());
		Location locationB = new Location("B");
		locationB.setLatitude(Double.valueOf(mDlat).doubleValue());
		locationB.setLongitude(Double.valueOf(mDlng).doubleValue());
		distance = locationA.distanceTo(locationB);

		return Double.toString(distance);
	}

	public void fillHeaderUI(BabysitterOutline outline) {
		mAddress = outline.getAddress();
		mName = outline.getText();
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

		mOutline = outline;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mHeaderView = inflater.inflate(
				R.layout.fragment_babysitter_detail_header, null);

		initHeadUI();

		View rootView = inflater.inflate(R.layout.fragment_babysitter_detail,
				container, false);

		mBabysitterCommentList = (ListView) rootView
				.findViewById(R.id.babysitter_comment_list);

		return rootView;
	}

	private void initHeadUI() {
		mBabysitterImage = (ImageView) mHeaderView
				.findViewById(R.id.babysitter_avator);

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
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mBabysitterCommentList.addHeaderView(mHeaderView);
		mPresenter.doDetailQuery(objectId);
		mPresenter.doCommentQuery(objectId);

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

			//ParseQuery<Baby> detailQuery = Baby.getQuery();
			//detailQuery.include("babysitter");
			//detailQuery.whereEqualTo("babysitter", mOutline);
			
			//detailQuery.getFirstInBackground(new GetCallback<Baby>() {
				
				//@Override
				//public void done(Baby baby, ParseException e) {
					//mPresenter.seeBabyDetail(baby.getObjectId());
				//}
			//});
			
			mPresenter.seeBabyDetail(objectId);
			
			break;
		case R.id.call_icon:
			mPresenter.makePhoneCall(mPhone.getText().toString());
			break;
		default:
			break;
		}
	}

}
