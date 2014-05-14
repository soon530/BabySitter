package tw.tasker.babysitter.view;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.dummy.DummyContent;
import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.model.BabysitterOutline;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link BabysitterListActivity} in two-pane mode (on tablets) or a
 * {@link BabysitterDetailActivity} on handsets.
 */
public class BabysitterDetailFragment extends Fragment {
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

	private ImageView mBabyIcon;
	private static final String[] mStrings = new String[] {"一","二","三","四","五","六","七","八","九"};

	private String mAddress;
	private String mName;

	private TextView mBabysitterName;

	private TextView mBabysitterAddress;
	private RatingBar mBabysitterRating;

	private String objectId;
	
	
	public int mTotalRating;
	public int mTotalComment;

	
	private ParseQueryAdapter<BabysitterComment> mOutlines;

	private ImageView mCallIcon;

	private TextView mPhone;

	private Button mDirection;

	private String mSlat;

	private String mSlng;

	private String mDlat;

	private String mDlng;

	private TextView mDistance;

	private ImageView mBabysitterImage;
	// Maximum results returned from a Parse query
	private static final int MAX_POST_SEARCH_RESULTS = 20;

	
	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	
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
		
		Bundle bundle = getActivity().getIntent().getExtras();
		objectId = bundle.getString("objectId");
		
		mSlat = bundle.getString("slat");
		mSlng= bundle.getString("slng");
		mDlat= bundle.getString("dlat");
		mDlng= bundle.getString("dlng");
		
		
		// Set up a customized query
		ParseQueryAdapter.QueryFactory<BabysitterComment> factory = new ParseQueryAdapter.QueryFactory<BabysitterComment>() {
			public ParseQuery<BabysitterComment> create() {
				//Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
				ParseQuery<BabysitterComment> query = BabysitterComment
						.getQuery();
				// query.include("user");
				query.orderByDescending("createdAt");
				query.whereEqualTo("babysitterId", objectId);
				// query.whereWithinKilometers("location",
				// geoPointFromLocation(myLoc), radius * METERS_PER_FEET /
				// METERS_PER_KILOMETER);
				query.setLimit(MAX_POST_SEARCH_RESULTS);
				return query;
			}
		};

		// Set up the query adapter
		mOutlines = new ParseQueryAdapter<BabysitterComment>(getActivity().getApplicationContext(), factory) {
			@Override
			public View getItemView(BabysitterComment post, View view, ViewGroup parent) {
				if (view == null) {
					view = View.inflate(getContext(), R.layout.adapter, null);
				}
				TextView contentView = (TextView) view
						.findViewById(R.id.babysitter_name);
				TextView usernameView = (TextView) view
						.findViewById(R.id.babysitter_address);
				RatingBar ratingBar = (RatingBar) view.findViewById(R.id.babysitter_rating);
				
				ImageView babysitterImage = (ImageView) view
				.findViewById(R.id.MyAdapter_ImageView_icon);
				
				contentView.setText(post.getTitle());
				usernameView.setText(post.getComment());
				ratingBar.setRating(post.getRating());
				
				babysitterImage.setBackgroundResource(R.drawable.ic_launcher);
				
				return view;
			}
		};

	    // Disable automatic loading when the adapter is attached to a view.
		mOutlines.setAutoload(false);

	    // Disable pagination, we'll manage the query limit ourselves
		mOutlines.setPaginationEnabled(false);
		

		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
	}

	private void doDetailQuery(String objectId) {
		LOGD("vic", "objectId" + objectId);
		ParseQuery<BabysitterOutline> detailQuery = BabysitterOutline.getQuery();
		detailQuery.getInBackground(objectId, new GetCallback<BabysitterOutline>() {


			@Override
			public void done(BabysitterOutline outline, ParseException e) {
				if(e != null) {
					LOGD("vic", "done", e);
				}else {
					mAddress = outline.getAddress();
					mName = outline.getText();
					LOGD("vic", "address" + mAddress + "name" + mName);
					mBabysitterName.setText(mName);
					mBabysitterAddress.setText(mAddress);
					
					
					mTotalRating = outline.getTotalRating();
					mTotalComment = outline.getTotalComment();
					
					int rating = 0;  
					try {
						rating =  mTotalRating/mTotalComment ;
					} catch (Exception e2) {
						// TODO: handle exception
					}
					mBabysitterRating.setRating(rating);
					
					mPhone.setText(outline.getTel());
					
					mCallIcon.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
								Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
								phoneIntent.setData(Uri.parse("tel:" + mPhone.getText()));
								startActivity(phoneIntent);
						}
					});
				}
			}
		});
	
	}

	  /*
	   * Set up a query to update the list view
	   */
	  private void doListQuery() {
	    //Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	    // If location info is available, load the data
	    //if (myLoc != null) {
	      // Refreshes the list view with new data based
	      // usually on updated location data.
	      mOutlines.loadObjects();
	    //}
	  }
	  
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doDetailQuery(objectId);
		doListQuery();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_babysitter,
				container, false);

		mBabysitterImage = (ImageView) rootView.findViewById(R.id.MyAdapter_ImageView_icon);
		
		imageLoader.displayImage("http://cwisweb.sfaa.gov.tw/babysitterFiles/20140315134959_0822R167.jpg", mBabysitterImage, options, null);
		
		 mBabysitterName = (TextView) rootView.findViewById(R.id.babysitter_name);
		//mBabysitterName.setText(mName);

		 mBabysitterAddress = (TextView) rootView.findViewById(R.id.babysitter_address);
		//mBabysitterAddress.setText(mAddress);
		 
		 mBabysitterRating = (RatingBar) rootView.findViewById(R.id.babysitter_rating);

		
		mBabysitterCommentList = (ListView) rootView.findViewById(R.id.babysitter_comment_list);
		mBabysitterCommentList.setAdapter(mOutlines);
		
		//mBabysitterCommentList.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mStrings ));
		
		mBabyIcon = (ImageView) rootView.findViewById(R.id.baby_icon);
		mBabyIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity().getApplicationContext(), BabyDetailActivity.class);
				startActivity(intent);

			}
		});
		
		mCallIcon = (ImageView) rootView.findViewById(R.id.call_icon);
		mPhone = (TextView) rootView.findViewById(R.id.babysitter_phone);
		
		mDirection = (Button) rootView.findViewById(R.id.direction);

		mDirection.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
//				String currentLattitude = "22.635725";
//				String currentLongitude = "120.377175";
//				String targetLat = "22.634599";
//				String targetLang = "120.350349";
//				String url = "http://maps.google.com/maps?saddr="+currentLattitude+","+currentLongitude+"&daddr="+targetLat+","+targetLang;
				//String url = "http://maps.google.com/maps?saddr="+mSlat+","+mSlng+"&daddr="+mDlat+","+mDlng;
				String url = "geo:0,0?q="+mSlat+","+mSlng;
				
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		});
		
		mDistance = (TextView) rootView.findViewById(R.id.distance);
		
		double distance = 0;
	    Location locationA = new Location("A");
	    locationA.setLatitude(Double.valueOf(mSlat).doubleValue());
	    locationA.setLongitude(Double.valueOf(mSlng).doubleValue());
	    Location locationB = new Location("B");
	    locationB.setLatitude(Double.valueOf(mDlat).doubleValue());
	    locationB.setLongitude(Double.valueOf(mDlng).doubleValue());
	    distance = locationA.distanceTo(locationB);
		
	    mDistance.setText(Double.toString(distance));
		
		
		
		// Show the dummy content as text in a TextView.
//		if (mItem != null) {
//			((TextView) rootView.findViewById(R.id.item_detail))
//					.setText(mItem.content);
//		}

		return rootView;
	}
}
