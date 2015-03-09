package tw.tasker.babysitter.view.activity;

import com.viewpagerindicator.CirclePageIndicator;

import tw.tasker.babysitter.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Activity which displays a registration screen to the user.
 */
public class SignUpOrLogInActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_or_log_in);

		// Log in button click handler
		((Button) findViewById(R.id.logInButton))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// Starts an intent of the log in activity
						startActivity(new Intent(SignUpOrLogInActivity.this,
								LoginActivity.class));
					}
				});

		// Sign up button click handler
		((Button) findViewById(R.id.signUpButton))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// Starts an intent for the sign up activity
						startActivity(new Intent(SignUpOrLogInActivity.this,
								SignUpActivity.class));
					}
				});

		// Sign up button click handler
		((Button) findViewById(R.id.go))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// Starts an intent for the sign up activity
						startActivity(new Intent(SignUpOrLogInActivity.this,
								HomeActivity.class));
					}
				});

//		MyPagerAdapter adapter = new MyPagerAdapter();
//		ViewPager myPager = (ViewPager) findViewById(R.id.pager);
//		myPager.setAdapter(adapter);
//		myPager.setCurrentItem(0);
		
//		CirclePageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        mIndicator.setViewPager(myPager);	
     }

	private class MyPagerAdapter extends PagerAdapter {

		public int getCount() {
			return 3;
		}

		public Object instantiateItem(View collection, int position) {

			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.first_page;
				break;
			case 1:
				resId = R.layout.second_page;
				break;
			case 2:
				resId = R.layout.third_page;
				break;
			}

			View view = inflater.inflate(resId, null);

			((ViewPager) collection).addView(view, 0);

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);

		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
