package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.fragment.BabysitterFavoriteFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FavoriteBabysitterActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);

		setContentView(R.layout.activity_favorite_babysitter);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, BabysitterFavoriteFragment.newInstance(1)).commit();
		}
	}


}
