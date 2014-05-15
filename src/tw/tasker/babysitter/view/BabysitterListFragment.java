package tw.tasker.babysitter.view;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.drawable;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.dummy.DummyContent;
import tw.tasker.babysitter.model.BabysitterOutline;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link BabysitterDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class BabysitterListFragment extends ListFragment {
	private String[] mtitle = new String[] { "台北-大安區", "台中-西屯區", "高雄-鳳山區",
			"高雄-三民區", "高雄-岡山區", "高雄-前鎮區" };
	private String[] minfo = new String[] { "張媽媽", "吳媽媽", "陳媽媽", "李媽媽", "郭媽媽",
			"錢媽媽" };

	private ParseQueryAdapter<BabysitterOutline> mOutlines;
	// Maximum results returned from a Parse query
	private static final int MAX_POST_SEARCH_RESULTS = 20;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public BabysitterListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setHasOptionsMenu(true);

		// Set up a customized query
		ParseQueryAdapter.QueryFactory<BabysitterOutline> factory = new ParseQueryAdapter.QueryFactory<BabysitterOutline>() {
			public ParseQuery<BabysitterOutline> create() {
				//Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
				ParseQuery<BabysitterOutline> query = BabysitterOutline
						.getQuery();
				// query.include("user");
				query.orderByDescending("createdAt");
				// query.whereWithinKilometers("location",
				// geoPointFromLocation(myLoc), radius * METERS_PER_FEET /
				// METERS_PER_KILOMETER);
				query.setLimit(MAX_POST_SEARCH_RESULTS);
				return query;
			}
		};

		// Set up the query list_item_babysitter_comment
		mOutlines = new ParseQueryAdapter<BabysitterOutline>(getActivity().getApplicationContext(), factory) {
			@Override
			public View getItemView(BabysitterOutline post, View view, ViewGroup parent) {
				if (view == null) {
					view = View.inflate(getContext(), R.layout.list_item_babysitter_list, null);
				}
				TextView babysitterName = (TextView) view
						.findViewById(R.id.babysitter_name);
				TextView babysitterAddress = (TextView) view
						.findViewById(R.id.babysitter_address);
				
				ImageView babysitterImage = (ImageView) view
				.findViewById(R.id.babysitter_avator);
				
				babysitterName.setText(post.getText());
				babysitterAddress.setText(post.getAddress());
				babysitterImage.setBackgroundResource(R.drawable.ic_launcher);
				
				return view;
			}
		};

	    // Disable automatic loading when the list_item_babysitter_comment is attached to a view.
		mOutlines.setAutoload(false);

	    // Disable pagination, we'll manage the query limit ourselves
		mOutlines.setPaginationEnabled(false);

		
		// 標題資料
		CharSequence[] Mtitle = mtitle;
		// 內容
		CharSequence[] Minfo = minfo;
		// 載入列表中，new出MyAdapter時帶入所需"標題"."內容"資料

		setListAdapter(mOutlines);
		
		//		setListAdapter(new MyAdapter(getLayoutInflater(savedInstanceState),
//				Mtitle, Minfo));

		// TODO: replace with a real list list_item_babysitter_comment.
		// setListAdapter(new
		// ArrayAdapter<DummyContent.DummyItem>(getActivity(),
		// android.R.layout.simple_list_item_activated_1,
		// android.R.id.text1, DummyContent.ITEMS));
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
		doListQuery();
	}
	  
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
