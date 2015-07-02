package tw.tasker.babysitter.presenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.layer.sdk.LayerClient;
import com.layer.sdk.query.Query;
import com.layer.sdk.query.Queryable;
import com.layer.sdk.query.RecyclerViewController;

/*
 * QueryAdapter.java
 * The QueryAdapter class hooks directly into a RecyclerView and updates the view based on a
 *  pre-defined Layer query. The query will automatically refresh the view when the Layer SDK syncs
 *  and receives new Conversations or Messages. This is a very powerful tool when it comes to driving
 *  the UI, since all changes are automatically captured. By overloading onCreateViewHolder and
 *  onBindViewHolder, you can decide how you display the updated Conversation or Message.
 *
 *  The ConversationQueryAdapter extends this class and drives the Conversation List. When a specific
 *   Conversation in that list selected, the MessageQueryAdapter is used to display all the Messages
 *   in that Conversation.
 */

//The Query Adapter defines all the callbacks required when the query updates
public abstract class QueryAdapter<Tquery extends Queryable, Tview extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<Tview>
        implements RecyclerViewController.Callback {

    //References to the query and the callbacks
    private final RecyclerViewController<Tquery> mQueryController;
    private final Callback mCallback;

    //When a new Conversation/Message is sent/received, a new Item will be inserted into the RecyclerView
    public static interface Callback {
        public void onItemInserted();
    }

    //Binds the query and callback class
    public QueryAdapter(LayerClient client, Query<Tquery> query, Callback callback) {
        mQueryController = client.newRecyclerViewController(query, null, this);
        mCallback = callback;
        setHasStableIds(false);

        Log.d("Activity", "Query adapter created");
    }

    //After a Query Adapter is initialized, the refresh method executes the query, which will then
    // execute any necessary callbacks
    public void refresh() {

        Log.d("Activity", "Query Adapter refreshed");
        mQueryController.execute();
    }

    //Overloaded in subclasses. The ViewHolder defines the view group that will actually be added to
    // the RecyclerView
    public abstract Tview onCreateViewHolder(ViewGroup viewGroup, int viewType);

    //The Conversation or Message object can be unpacked into the ViewHolder
    public void onBindViewHolder(Tview viewHolder, int position) {
        Log.d("Activity", "onBindViewHolder called: " + mQueryController.getItem(position).getId());
        onBindViewHolder(viewHolder, mQueryController.getItem(position));
    }

    //Overloaded in subclasses. The target Conversation or Message can be deconstructed and the
    // ViewHolder can be populated
    public abstract void onBindViewHolder(Tview viewHolder, Tquery queryable);

    //Returns the total number of items found by the query
    public int getItemCount() {
        Log.d("Activity", "Query Adapter getItemCount: " + mQueryController.getItemCount());
        return mQueryController.getItemCount();
    }

    //If the data set changes, check to see if the views in the RecyclerView need to be updated
    public void onQueryDataSetChanged(RecyclerViewController controller) {

        Log.d("Activity", "Query Adapter onQueryDataSetChanged");
        notifyDataSetChanged();
    }

    //If a specific item has changed, check to see if the views in the RecyclerView need to be updated
    public void onQueryItemChanged(RecyclerViewController controller, int position) {
        Log.d("Activity", "Query Adapter onQueryItemChanged");
        notifyItemChanged(position);
    }

    //If several items have changed, checked to see if the views in the RecyclerView need to be updated
    public void onQueryItemRangeChanged(RecyclerViewController controller, int positionStart, int itemCount) {
        Log.d("Activity", "Query Adapter onQueryItemRangeChanged");
        notifyItemRangeChanged(positionStart, itemCount);
    }

    //If the query returns a new item, execute the callback
    public void onQueryItemInserted(RecyclerViewController controller, int position) {
        Log.d("Activity", "Query Adapter onQueryItemInserted");
        notifyItemInserted(position);
        if ((position + 1) == getItemCount()) {
            mCallback.onItemInserted();
        }
    }

    //If the query returns several new items, execute the callback
    public void onQueryItemRangeInserted(RecyclerViewController controller, int positionStart, int itemCount) {
        Log.d("Activity", "Query Adapter onQueryItemRangeInserted");
        notifyItemRangeInserted(positionStart, itemCount);
        if ((positionStart + itemCount + 1) == getItemCount()) {
            mCallback.onItemInserted();
        }
    }

    //If an item has been delete,d check to see if the views need to be updated
    public void onQueryItemRemoved(RecyclerViewController controller, int position) {
        Log.d("Activity", "Query Adapter onQueryItemRemoved");
        notifyItemRemoved(position);
    }

    //If several items have been deleted, check to see if the views need to be updated
    public void onQueryItemRangeRemoved(RecyclerViewController controller, int positionStart, int itemCount) {
        Log.d("Activity", "Query Adapter onQueryItemRangeRemoved");
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    //If an item has moved, check to see if the views need to be updated
    public void onQueryItemMoved(RecyclerViewController controller, int fromPosition, int toPosition) {
        Log.d("Activity", "Query Adapter onQueryItemMoved");
        notifyItemMoved(fromPosition, toPosition);
    }
}