package tw.tasker.babysitter.view;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.drawable;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.dummy.DummyContent;
import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.model.BabysitterOutline;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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
	private ListView mListView;

	private ImageView mBabyIcon;
	private static final String[] mStrings = new String[] {"一","二","三","四","五","六","七","八","九"};

	private String mAddress;
	private String mName;

	private TextView tname;

	private TextView taddress;
	private RatingBar mRatingBar;

	private String objectId;
	
	
	public int mTotalRating;
	public int mTotalComment;

	
	private ParseQueryAdapter<BabysitterComment> mOutlines;
	// Maximum results returned from a Parse query
	private static final int MAX_POST_SEARCH_RESULTS = 20;

	
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
						.findViewById(R.id.MyAdapter_TextView_title);
				TextView usernameView = (TextView) view
						.findViewById(R.id.MyAdapter_TextView_info);
				RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar1);
				
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
					tname.setText(mName);
					taddress.setText(mAddress);
					
					
					mTotalRating = outline.getTotalRating();
					mTotalComment = outline.getTotalComment();
					
					int rating = 0;  
					try {
						rating =  mTotalRating/mTotalComment ;
					} catch (Exception e2) {
						// TODO: handle exception
					}
					mRatingBar.setRating(rating);
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
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);

		 tname = (TextView) rootView.findViewById(R.id.MyAdapter_TextView_title);
		//tname.setText(mName);

		 taddress = (TextView) rootView.findViewById(R.id.MyAdapter_TextView_info);
		//taddress.setText(mAddress);
		 
		 mRatingBar = (RatingBar) rootView.findViewById(R.id.ratingBar1);

		
		mListView = (ListView) rootView.findViewById(R.id.listView1);
		mListView.setAdapter(mOutlines);
		
		//mListView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mStrings ));
		
		mBabyIcon = (ImageView) rootView.findViewById(R.id.baby_icon);
		mBabyIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity().getApplicationContext(), BabyDetailActivity.class);
				startActivity(intent);

			}
		});
		
		
		
		// Show the dummy content as text in a TextView.
//		if (mItem != null) {
//			((TextView) rootView.findViewById(R.id.item_detail))
//					.setText(mItem.content);
//		}

		return rootView;
	}
}
