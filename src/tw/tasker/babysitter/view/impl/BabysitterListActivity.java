package tw.tasker.babysitter.view.impl;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.R.menu;
import tw.tasker.babysitter.view.BabysitterListFragment;
import tw.tasker.babysitter.view.BabysitterMapActivity;
import tw.tasker.babysitter.view.BabysitterListFragment.Callbacks;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link BabysitterDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BabysitterListFragment} and the item details (if present) is a
 * {@link BabysitterDetailFragment}.
 * <p>
 * This activity also implements the required {@link BabysitterListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class BabysitterListActivity extends ActionBarActivity implements
		BabysitterListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_babysitter_list);

		if (findViewById(R.id.babysitter_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((BabysitterListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link BabysitterListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(BabysitterDetailFragment.ARG_ITEM_ID, id);
			BabysitterDetailFragment fragment = new BabysitterDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.babysitter_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, BabysitterDetailActivity.class);
			detailIntent.putExtra(BabysitterDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.babysitter_list, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_map) {
			Intent intent = new Intent();
			intent.setClass(this, BabysitterMapActivity.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
