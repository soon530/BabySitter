package tw.tasker.babysitter;

import tw.tasker.babysitter.view.activity.ConversationActivity;
import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

public class Receiver extends ParsePushBroadcastReceiver {
	
	
	 @Override
	    public void onPushOpen(Context context, Intent intent) {
		 
	        Intent i = new Intent(context, ConversationActivity.class);
	        i.putExtras(intent.getExtras());
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(i);
	 }
	 
	 
}
