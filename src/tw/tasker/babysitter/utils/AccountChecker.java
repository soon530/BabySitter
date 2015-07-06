package tw.tasker.babysitter.utils;

import tw.tasker.babysitter.UserType;

import com.parse.ParseUser;

import android.widget.EditText;

public class AccountChecker {

	public static boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isMatching(EditText etText1, EditText etText2) {
		if (etText1.getText().toString().equals(etText2.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static UserType getUserType() {
		UserType userType = UserType.LATER;
		if (isLogin()) {
			if (isSitter()) {
				userType = UserType.SITTER;
			} else {
				userType = UserType.PARENT;
			}
		}
		return userType;
	}
	
	public static boolean isLogin() {
		if (isNull(ParseUser.getCurrentUser())) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNull(Object object) {
		if (object == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSitter() {
		String userType = ParseUser.getCurrentUser().getString("userType"); 
		LogUtils.LOGD("userType", userType);
		if (userType.equals("sitter")) {
			return true;
		} else {
			return false;
		}
	}
}
