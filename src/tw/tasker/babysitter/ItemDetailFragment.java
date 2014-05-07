package tw.tasker.babysitter;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import tw.tasker.babysitter.dummy.DummyContent;
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
import android.widget.TextView;
import static tw.tasker.babysitter.LogUtils.LOGD;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link BabysitterListActivity} in two-pane mode (on tablets) or a
 * {@link BabysitterDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
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

	private String objectId;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
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
				}
			}
		});
	
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doDetailQuery(objectId);
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

		
		mListView = (ListView) rootView.findViewById(R.id.listView1);
		mListView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mStrings ));
		
		mBabyIcon = (ImageView) rootView.findViewById(R.id.baby_icon);
		mBabyIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
