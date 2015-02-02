package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

public class SplashActivity extends ActionBarActivity {
	private static final long SPLASH_DISPLAY_LENGTH = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initFullScreen();
		setContentView(R.layout.activity_splash);
		goSingUpOrLogin();
	}

	private void initFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	private void goSingUpOrLogin() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startSignUpOrLogin();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

	protected void startSignUpOrLogin() {
		Intent intent = new Intent();
		intent.setClass(this, DispatchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
