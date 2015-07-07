package tw.tasker.babysitter.presenter.adapter;

import java.util.List;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.UserType;
import tw.tasker.babysitter.layer.LayerImpl;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.parse.ParseImpl;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.LogUtils;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.layer.sdk.LayerClient;
import com.layer.sdk.LayerClient.DeletionMode;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;
import com.layer.sdk.query.Query;
import com.layer.sdk.query.SortDescriptor;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/*
 * ConversationQueryAdapter.java
 * Drives the RecyclerView in the ConversationsActivity class. Shows a list of conversations sorted
 *  by the last message received. For each Conversation, it shows the participants (not including the
 *  locally authenticated user), the time of the last message received, and the contents of the last
 *  message.
 *
 *  This is just one possible implementation. You can edit the conversation_item.xml view to change
 *   what is shown for each Conversation, including adding icons for individual or group messages,
 *   indicating whether the latest message in a Conversation is unread, etc.
 */


public class ConversationQueryAdapter extends QueryAdapter<Conversation, ConversationQueryAdapter.ViewHolder> {

    //Inflates the view associated with each Conversation object returned by the Query
    private final LayoutInflater mInflater;

    //Handle the callbacks when the Conversation item is actually clicked. In this case, the
    // ConversationsActivity class implements the ConversationClickHandler
    private final ConversationClickHandler mConversationClickHandler;

    public static interface ConversationClickHandler {
        public void onConversationClick(Conversation conversation);

        public boolean onConversationLongClick(Conversation conversation);
        
        public void onConfirmClick(Conversation conversation);
    }

    //The fields in the ViewHolder reflect the conversation_item view
    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        //For each Conversation item in the RecyclerView list, we show the participants, time,
        // contents of the last message, and have a reference to the conversation so when it is
        // clicked we can start the ConversationActivity
        public TextView participants;
        public TextView time;
        public TextView lastMsgContent;
        public Conversation conversation;
        public final ConversationClickHandler conversationClickHandler;
		public Button match;
		public Button cancel;

        //Registers the click listener callback handler
        public ViewHolder(View itemView, ConversationClickHandler conversationClickHandler) {
            super(itemView);
            this.conversationClickHandler = conversationClickHandler;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        //Execute the callback when the conversation is clicked
        public void onClick(View v) {
        	conversationClickHandler.onConversationClick(conversation);
        }

        //Execute the callback when the conversation is long-clicked
        public boolean onLongClick(View v) {
            return conversationClickHandler.onConversationLongClick(conversation);
        }
        
        public void onConfirmClick() {
        	conversationClickHandler.onConfirmClick(conversation);
        }
    }

    //Constructor for the ConversationQueryAdapter
    //Sorts all conversations by last message received (ie, downloaded to the device)
    public ConversationQueryAdapter(Context context, LayerClient client, ConversationClickHandler conversationClickHandler, Callback callback) {
        super(client, Query.builder(Conversation.class)
                .sortDescriptor(new SortDescriptor(Conversation.Property.LAST_MESSAGE_RECEIVED_AT, SortDescriptor.Order.DESCENDING))
                .build(), callback);

        //Sets the LayoutInflator and Click callback handler
        mInflater = LayoutInflater.from(context);
        mConversationClickHandler = conversationClickHandler;
    }

    //When a new Conversation is created (ie, either locally, or by another user), a new ViewHolder is created
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //The conversation_item is just an example view you can use to display each Conversation in a list
        View itemView = mInflater.inflate(R.layout.conversation_item, null);

        //Tie the view elements to the fields in the actual view after it has been created
        ViewHolder holder = new ViewHolder(itemView, mConversationClickHandler);
        holder.participants = (TextView) itemView.findViewById(R.id.participants);
        holder.time = (TextView) itemView.findViewById(R.id.time);
        holder.lastMsgContent = (TextView) itemView.findViewById(R.id.message);

        holder.match = (Button) itemView.findViewById(R.id.match);
        holder.cancel = (Button) itemView.findViewById(R.id.cancel);

        return holder;
    }

    //After the ViewHolder is created, we need to populate the fields with information from the Conversation
    public void onBindViewHolder(final ViewHolder viewHolder, Conversation conversation) {
        if (conversation == null) {
            // If the item no longer exists, the ID probably migrated.
            refresh();
            return;
        }

        Log.d("Activity", "binding conversation: " + conversation.getId() + " with participants: " + conversation.getParticipants().toString());

        //Set the Conversation (so when this item is clicked, we can start a ConversationActivity and
        // show all the messages associated with it)
        viewHolder.conversation = conversation;

        //Go through all the User IDs in the Conversation and find the matching human readable
        // handles from Parse
        String participants = "";
        List<String> users = conversation.getParticipants();
        for(int i = 0; i < users.size(); i++){
            if(!users.get(i).equals(ParseUser.getCurrentUser().getObjectId())){
                //Format the String so there is a comma after every username
                if(participants.length() > 0)
                    participants += ", ";

                //Add the human readable username to the String
                participants += ParseImpl.getUsername(users.get(i));
            }
        }
        
        String participatsTitle = "";
        UserType userType = AccountChecker.getUserType();
        if (userType == UserType.PARENT) {
        	participatsTitle = "保母:";
        } else if (userType == UserType.SITTER) {
        	participatsTitle = "爸媽:";
        }
        
        viewHolder.participants.setText(participatsTitle + participants);

        //Grab the last message in the conversation and show it in the format "sender: last message content"
        Message message = conversation.getLastMessage();
        if (message != null) {
            viewHolder.lastMsgContent.setText(ParseImpl.getUsername(message.getSender().getUserId()) + ": " + LayerImpl.getMessageText(message));
        } else {
            viewHolder.lastMsgContent.setText("");
        }

        //Draw the date the last message was received (downloaded from the server)
        viewHolder.time.setText(LayerImpl.getReceivedAtTime(message));
        
        viewHolder.match.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	            MessagePart part = LayerImpl.getLayerClient().newMessagePart("接受托育，開始聊天吧~");
	            Message msg = LayerImpl.getLayerClient().newMessage(part);
	            viewHolder.conversation.send(msg);
	            
	            updateSitterConfirm(viewHolder.conversation.getId().toString(), viewHolder);

	            viewHolder.onConfirmClick();
	            //viewHolder.isConfirm = true;

			}
		});
        
        viewHolder.cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewHolder.conversation.delete(DeletionMode.ALL_PARTICIPANTS);
			}
		});

        
//		if (isUserSendRequest(viewHolder.conversation.getId().toString())) {
//			LogUtils.LOGD("vic", "1.confirm button hide!");
//			viewHolder.match.setVisibility(View.GONE);
//			viewHolder.cancel.setVisibility(View.GONE);
//		} else {
//			LogUtils.LOGD("vic", "1.confirm button show!");
//			viewHolder.match.setVisibility(View.VISIBLE);
//			viewHolder.cancel.setVisibility(View.VISIBLE);
//		}

		loadSitterFavoriteData(Config.sitterInfo, viewHolder);

    }
    
    private boolean isConfirmBothParentAndSitter(String conversationId) {
		for (BabysitterFavorite favorite : Config.favorites) {
//			/String favoriteUserId = favorite.getUser().getObjectId();
			String favoriteConversationId = favorite.getConversationId();
			
			if (favoriteConversationId.equals(conversationId) && 
					favorite.getIsParentConfirm() && favorite.getIsSitterConfirm()) {
				return true;
			}
		}
		return false;
		
	}

	protected void updateSitterConfirm(String conversationId, final ViewHolder viewHolder) {
		ParseQuery<BabysitterFavorite> query = BabysitterFavorite.getQuery();
		query.whereEqualTo("conversationId", conversationId);
		query.getFirstInBackground(new GetCallback<BabysitterFavorite>() {
			
			@Override
			public void done(BabysitterFavorite favorite, ParseException exception) {
				if (favorite != null) {
					favorite.setIsSitterConfirm(true);
					favorite.saveInBackground();
				}
			}
		});
	}
	
	private void loadSitterFavoriteData(Babysitter sitter, final ViewHolder viewHolder) {
		ParseQuery<BabysitterFavorite> query = BabysitterFavorite.getQuery();
		
		if (AccountChecker.getUserType() == UserType.PARENT) {
			query.whereEqualTo("UserInfo", Config.userInfo);
		} else if (AccountChecker.getUserType() == UserType.SITTER) {
			query.whereEqualTo("Babysitter", Config.sitterInfo);
		}
		
		query.findInBackground(new FindCallback<BabysitterFavorite>() {
			
			@Override
			public void done(List<BabysitterFavorite> favorites, ParseException e) {
				if (AccountChecker.isNull(favorites)) {
					//Toast.makeText(getActivity(), "查不到你的資料!", Toast.LENGTH_SHORT).show();

				} else {
					Config.favorites = favorites;
					
					for (BabysitterFavorite favorite : favorites) {
						LogUtils.LOGD("vic", "sitter favorite isParentConfirm():" + favorite.getIsParentConfirm());
						LogUtils.LOGD("vic", "sitter favorite isSitterConfirm():" + favorite.getIsSitterConfirm());
					}
					
					//LogUtils.LOGD("vic", "isConfirm()" + viewHolder.isConfirm);
					
					String conversationId = viewHolder.conversation.getId().toString();
					
					if (isConfirmBothParentAndSitter(conversationId) || isUserSendRequest(conversationId) ) {
						LogUtils.LOGD("vic", "2.confirm button hide!");
						viewHolder.match.setVisibility(View.GONE);
						viewHolder.cancel.setVisibility(View.GONE);
					} else {
						LogUtils.LOGD("vic", "2.confirm button show!");
						viewHolder.match.setVisibility(View.VISIBLE);
						viewHolder.cancel.setVisibility(View.VISIBLE);
					}
					
				}
			}
		});
	}

	private boolean isUserSendRequest(String conversationId) {
		for (BabysitterFavorite favorite : Config.favorites) {
			String favoriteUserId = favorite.getUser().getObjectId();
			String favoriteConversationId = favorite.getConversationId();
			
			if (favoriteConversationId.equals(conversationId) && 
					favoriteUserId.equals(ParseUser.getCurrentUser().getObjectId())) {
				return true;
			}
		}
		return false;
	}

	//This example app only has one kind of view type, but you could support different TYPES of
    // Conversations if you were so inclined
    public int getItemViewType(int i) {
        return 1;
    }
}