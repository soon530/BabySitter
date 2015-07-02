package tw.tasker.babysitter.view.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.messaging.Conversation;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.R.menu;
import tw.tasker.babysitter.layer.LayerCallbacks;
import tw.tasker.babysitter.layer.LayerImpl;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.parse.ParseImpl;
import tw.tasker.babysitter.presenter.adapter.BabysittersParseQueryAdapter;
import tw.tasker.babysitter.presenter.adapter.ConversationQueryAdapter;
import tw.tasker.babysitter.presenter.adapter.ParentMessageParseQueryAdapter;
import tw.tasker.babysitter.presenter.adapter.QueryAdapter;
import tw.tasker.babysitter.presenter.adapter.SitterMessageParseQueryAdapter;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.LogUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationActivity extends ActionBarActivity implements LayerCallbacks, ConversationQueryAdapter.ConversationClickHandler {
	public static final String PARSE_DATA_KEY = "com.parse.Data";
	private TextView mInfo;
	private Button mOk;
	private UserInfo mUserInfo;
	private ListView mListView;
	private SitterMessageParseQueryAdapter mAdapter;
	private ParentMessageParseQueryAdapter mParentAdapter;
    //The Query Adapter that grabs all Conversations and displays them based on the last lastMsgContent
    private ConversationQueryAdapter mConversationsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
//		mListView = (ListView) findViewById(R.id.list);
//		
//		if (AccountChecker.isSitter()) {
//			mParentAdapter = new ParentMessageParseQueryAdapter(this);
//			mParentAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
//			mListView.setAdapter(mParentAdapter);
//			
//		} else { // 爸媽看保母列表 
//			mAdapter = new SitterMessageParseQueryAdapter(this);
//			mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
//			mListView.setAdapter(mAdapter);
//		}
		
		initLayer();
		
//        JSONObject data = getDataFromIntent(getIntent());
//        LogUtils.LOGD("Push", "Clicked data:" + data);
        
//        mInfo = (TextView) findViewById(R.id.info);
//        mOk = (Button) findViewById(R.id.yes);
//        mOk.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				pushTextToParent();
//			}
//		});
        
//        String user = data.optString("user");
//        loadProfileData(user);
        //info.setText("user" + user);

	}
	
	private void initLayer() {
        //Initializes and connects the LayerClient if it hasn't been created already
        LayerImpl.initialize(getApplicationContext());

        //Registers the activity so callbacks are executed on the correct class
        LayerImpl.setContext(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
        //Registers the activity so callbacks are executed on the correct class
        LayerImpl.setContext(this);

        //Runs a Parse Query to return all users registered with the app
        ParseImpl.cacheAllUsers();

		
        //If the user is not authenticated, make sure they are logged in, and if they are, re-authenticate
        if (!LayerImpl.isAuthenticated()) {

            if (ParseImpl.getRegisteredUser() == null) {

                LogUtils.LOGD("Activity", "User is not authenticated or logged in - returning to login screen");
                Intent intent = new Intent(ConversationActivity.this, LogInActivity.class);
                startActivity(intent);

            } else {

            	LogUtils.LOGD("Activity", "User is not authenticated, but is logged in - re-authenticating user");
                LayerImpl.authenticateUser();

            }

            //Everything is set up, so start populating the Conversation list
        } else {

        	LogUtils.LOGD("Activity", "Starting conversation view");
            setupConversationView();
        }
        
	}
	
    //Set up the Query Adapter that will drive the RecyclerView on the conversations_screen
    private void setupConversationView() {

        LogUtils.LOGD("Activity", "Setting conversation view");

        //Grab the Recycler View and list all conversation objects in a vertical list
        RecyclerView conversationsView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        conversationsView.setLayoutManager(layoutManager);

        //The Query Adapter drives the recycler view, and calls back to this activity when the user
        // taps on a Conversation
        mConversationsAdapter = new ConversationQueryAdapter(getApplicationContext(), LayerImpl.getLayerClient(), this, new QueryAdapter.Callback() {
            @Override
            public void onItemInserted() {
                //Log.d("Activity", "Conversation Adapter, new conversation inserted");
            }
        });

        //Attach the Query Adapter to the Recycler View
        conversationsView.setAdapter(mConversationsAdapter);

        //Execute the Query
        mConversationsAdapter.refresh();
    }
    
	private void pushTextToParent() {
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		LogUtils.LOGD("vic", "push obj:" + mUserInfo.getUser().getObjectId());
		//ParseObject obj = ParseObject.createWithoutData("user", "KMyQfnc5k3");
		pushQuery.whereEqualTo("user", mUserInfo.getUser());
		
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery); // Set our Installation query
		//push.setMessage("有爸媽，想找你帶小孩唷~");
		JSONObject data = getJSONDataMessageForIntent();
		push.setData(data);
		push.sendInBackground(new SendCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e != null)
					LogUtils.LOGD("vic", "erroe" + e.getMessage());
			}
		});
		
	}

	private JSONObject getJSONDataMessageForIntent() {
		try
	    {
	        JSONObject data = new JSONObject();
	        data.put("alert", "保母["+ParseUser.getCurrentUser().getUsername()+"]，可以幫你帶小孩~");
	        //instead action is used
	        //data.put("customdata", "custom data value");
	        data.put("user", ParseUser.getCurrentUser().getObjectId());
	        return data;
	    }
	    catch(JSONException x)
	    {
	        throw new RuntimeException("Something wrong with JSON", x);
	    }
	}
	
	private JSONObject getDataFromIntent(Intent intent) {
		JSONObject data = null;
		try {
			
			data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
		} catch (JSONException e) {
			// Json was not readable...
		}
		return data;
	}
	
	private void loadProfileData(String userObjectId) {
        LogUtils.LOGD("vic", "userObjectId:" + userObjectId);

		ParseUser user = new ParseUser();
		user.setObjectId(userObjectId);

		ParseQuery<UserInfo> query = UserInfo.getQuery();
		query.whereEqualTo("user", user);
		query.getFirstInBackground(new GetCallback<UserInfo>() {
			

			@Override
			public void done(UserInfo userInfo, ParseException exception) {
				if (userInfo == null) {
					Toast.makeText(ConversationActivity.this, "查不到你的資料!", Toast.LENGTH_SHORT).show();

				} else {
					//Config.userInfo = userInfo;
					//fillDataToUI(userInfo);
					mInfo.setText("姓名：" + userInfo.getName() + ", 聯絡電話：" + userInfo.getPhone());
					mUserInfo = userInfo;
				}
			}
		});
	}

	//Layer events
	@Override
	public void onLayerConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayerDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayerConnectionError(LayerException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserAuthenticated(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserAuthenticatedError(LayerException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserDeauthenticated() {
		// TODO Auto-generated method stub
		
	}

	// Conversation
	@Override
	public void onConversationClick(Conversation conversation) {
        //If the Conversation is valid, start the MessageActivity and pass in the Conversation ID
        if (conversation != null && conversation.getId() != null && !conversation.isDeleted()) {
            Intent intent = new Intent(ConversationActivity.this, MessageActivity.class);
            intent.putExtra("conversation-id", conversation.getId());
            startActivity(intent);
        }
		
	}

	@Override
	public boolean onConversationLongClick(Conversation conversation) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.push_info, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
