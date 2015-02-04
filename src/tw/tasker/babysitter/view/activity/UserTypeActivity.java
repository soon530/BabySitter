package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UserTypeActivity extends ActionBarActivity implements OnClickListener {
	
	private Button mParents;
	private Button mSitters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usertype);
		
		mParents = (Button) findViewById(R.id.parents);
		mSitters = (Button) findViewById(R.id.sitters);

		mSitters.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(this, SitterActivity.class);
		startActivity(intent);
	}
}
