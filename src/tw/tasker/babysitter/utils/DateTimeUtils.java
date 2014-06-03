package tw.tasker.babysitter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	public static String show(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		String now = formatter.format(date);
		return now;
	}
}
