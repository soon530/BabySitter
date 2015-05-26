package tw.tasker.babysitter.view.activity;

import org.json.JSONException;
import org.json.JSONObject;

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
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.presenter.adapter.BabysittersParseQueryAdapter;
import tw.tasker.babysitter.presenter.adapter.ParentMessageParseQueryAdapter;
import tw.tasker.babysitter.presenter.adapter.SitterMessageParseQueryAdapter;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.LogUtils;
import android.support.v7.app.ActionBarActivity;
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

public class MessageActivity extends ActionBarActivity {
	public static final String PARSE_DATA_KEY = "com.parse.Data";
	private TextView mInfo;
	private Button mOk;
	private UserInfo mUserInfo;
	private ListView mListView;
	private SitterMessageParseQueryAdapter mAdapter;
	private ParentMessageParseQueryAdapter mParentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		mListView = (ListView) findViewById(R.id.list);
		
		if (AccountChecker.isSitter()) {
			mParentAdapter = new ParentMessageParseQueryAdapter(this);
			mParentAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
			mListView.setAdapter(mParentAdapter);
			
		} else { // 爸媽看保母列表 
			mAdapter = new SitterMessageParseQueryAdapter(this);
			mAdapter.setObjectsPerPage(Config.OBJECTS_PER_PAGE);
			mListView.setAdapter(mAdapter);
		}
		

		
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
					Toast.makeText(MessageActivity.this, "查不到你的資料!", Toast.LENGTH_SHORT).show();

				} else {
					//Config.userInfo = userInfo;
					//fillDataToUI(userInfo);
					mInfo.setText("姓名：" + userInfo.getName() + ", 聯絡電話：" + userInfo.getPhone());
					mUserInfo = userInfo;
				}
			}
		});
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
