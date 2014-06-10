package tw.tasker.babysitter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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

}
