package tw.tasker.babysitter.utils;

import android.app.Activity;
import android.view.Window;

public class ProgressBarUtils {

	public static void init(Activity activity) {
		if (activity == null) {
			return;
		}
		/** Enabling Progress bar for this activity */
		activity.getWindow().requestFeature(
				Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	public static void show(Activity activity) {
		if (activity == null) {
			return;
		}
		activity.setProgressBarIndeterminateVisibility(true);
	}

	public static void hide(Activity activity) {
		if (activity == null) {
			return;
		}
		activity.setProgressBarIndeterminateVisibility(false);
	}
}
