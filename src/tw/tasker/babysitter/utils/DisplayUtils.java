package tw.tasker.babysitter.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseUser;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.view.fragment.ListDialogFragment;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class DisplayUtils {

	public static String getDateTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		String now = formatter.format(date);
		return now;
	}

	public static float getRatingValue(float totalRating, int totalComment) {
		float avgRating = 0.0f;

		if (totalComment != 0) {
			avgRating = totalRating / totalComment;
		}
		return avgRating;
	}

	public static Boolean hasNetwork(Context contenxt) {
		final ConnectivityManager conMag = (ConnectivityManager)
				 contenxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo wifi =
				conMag.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobile=
				conMag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				 
		if( wifi.isAvailable() || mobile.isAvailable()){
			return true;
		} else {
			return false;
		}
	}
	
//	public static void showKeypad(Activity context) {
//		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//		if(imm != null){
//	        imm.showSoftInputFromInputMethod(context.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
//	    }
//	}
	
	public static void hideKeypad(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm != null && activity.getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}
	
	
	public static void toggleKeypad(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		if(imm != null){
	        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
	    }
	}
	
	public static String getChangeText(String babycareTime) {
		String changeText = "";
		changeText = babycareTime
				.replace("白天", "日托")
				.replace("夜間", "夜托")
				.replace("全天(24小時)", "全日")
				.replace("半天", "半日")
				.replace("到宅服務", "到府服務");
		return changeText;
	}

	public static int getBabyCount(String babycareCount) {
		
		int count;
		if (babycareCount.isEmpty()) {
			count = 0;
		} else {
			String[] babies = babycareCount.split(" ");
			count = babies.length;
		}
		
		return count;
	}

	public static List<String> getPhoneList(String allPhone) {
		LogUtils.LOGD("vic", "all phone: " + allPhone);
		String[] phones = allPhone.replace("(日):", "").replace("手機: ", "").split(" ");
		for (String phone : phones) {
			LogUtils.LOGD("vic", "phone" + phone);
		}
		return Arrays.asList(phones);
	}
	
	public static void showBabysitterPhone(final Context context, final String[] phones) {
		DialogFragment newFragment = new ListDialogFragment(phones,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String phone = phones[which];
						makePhoneCall(context, phone);
					}
				});

		newFragment.show(
				((FragmentActivity) context).getSupportFragmentManager(), "dialog");
	}
	
	private static void makePhoneCall(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		context.startActivity(intent);
	}

	public static JSONObject getJSONDataMessageForIntent() {
		try
	    {
	        JSONObject data = new JSONObject();
	        data.put("alert", "家長["+ParseUser.getCurrentUser().getUsername()+"]，想找你帶小孩唷~");
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
	
}
