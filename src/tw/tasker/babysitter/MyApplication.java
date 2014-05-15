package tw.tasker.babysitter;

import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.model.BabysitterOutline;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseObject;

public class MyApplication extends Application {
	private static final String APPLICATION_ID = "NJFvH3uzP9EHAKydw7iSIICBBU4AfAHvhJzuTawu";
	private static final String CLIENT_KEY = "FOwFRZ8hqGZ4NdZflfeLINvBQehNXOlihdEKnwTU";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
		ParseObject.registerSubclass(BabysitterOutline.class);
		ParseObject.registerSubclass(BabysitterComment.class);

		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

		initImageLoader(getApplicationContext());

	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
