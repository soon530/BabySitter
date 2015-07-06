package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.layer.LayerCallbacks;
import tw.tasker.babysitter.layer.LayerImpl;
import tw.tasker.babysitter.parse.ParseImpl;

import com.layer.sdk.exceptions.LayerException;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends ActionBarActivity implements LayerCallbacks {
	protected ProgressDialog mDialog;
	
	public BaseActivity() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDialog = new ProgressDialog(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.primary_dark);
		
        //Initializes and connects the LayerClient if it hasn't been created already
        LayerImpl.initialize(getApplicationContext());

        //Registers the activity so callbacks are executed on the correct class
        LayerImpl.setContext(this);
        
	}
	
    //This can be called when the app moves from the foreground to the background, and when the
    // Activity is created
    protected void onResume(){
        super.onResume();

        //Registers the activity so callbacks are executed on the correct class
        LayerImpl.setContext(this);

        //Runs a Parse Query to return all users registered with the app
        ParseImpl.cacheAllUsers();
    }

	@TargetApi(19)
	protected void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	protected void showDialog(String title, String message) {
		// Set up a progress dialog
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.show();
	}
	
	protected void hideDialog() {
		mDialog.dismiss();
	}

	@Override
	public void onLayerConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayerDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayerConnectionError(LayerException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserAuthenticated(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserAuthenticatedError(LayerException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserDeauthenticated() {
		// TODO Auto-generated method stub
		
	}

}