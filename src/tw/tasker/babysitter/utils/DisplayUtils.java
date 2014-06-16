package tw.tasker.babysitter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
}
