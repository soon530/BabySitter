package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	private static final long SPLASH_DISPLAY_LENGTH = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		goSingUpOrLogin();
		//goSingUpOrLoginWithClick();
	}

	private void goSingUpOrLoginWithClick() {
		
		ImageView splashLogo = (ImageView) findViewById(R.id.splash_logo);
		
		splashLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startSignUpOrLogin();
			}
		});
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
