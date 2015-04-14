package tw.tasker.babysitter;

import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.BabyFavorite;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.model.data.BabysitterFavorite;
import tw.tasker.babysitter.model.data.City;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.LogUtils;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class MyApplication extends Application {
	private static final String APPLICATION_ID = "NJFvH3uzP9EHAKydw7iSIICBBU4AfAHvhJzuTawu";
	private static final String CLIENT_KEY = "FOwFRZ8hqGZ4NdZflfeLINvBQehNXOlihdEKnwTU";

	@Override
	public void onCreate() {
		super.onCreate();		
		ParseObject.registerSubclass(Babysitter.class);
		ParseObject.registerSubclass(BabysitterComment.class);
		ParseObject.registerSubclass(BabyDiary.class);
		ParseObject.registerSubclass(BabyFavorite.class);
		ParseObject.registerSubclass(BabysitterFavorite.class);
		ParseObject.registerSubclass(BabyRecord.class);
		ParseObject.registerSubclass(City.class);
		ParseObject.registerSubclass(UserInfo.class);
		ParseObject.registerSubclass(Sitter.class);
		
		if (isRelease())
			ParseCrashReporting.enable(this);
		
	    //Parse.enableLocalDatastore(this);
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
		
		enablePushNotifications();

		initImageLoader(getApplicationContext());
		
		//throw new RuntimeException("Test Exception!");

	}
	
	
	
	private boolean isRelease() {
		return !BuildConfig.DEBUG;
	}



	private void enablePushNotifications() {
		ParsePush.subscribeInBackground("", new SaveCallback() {
		  @Override
		  public void done(ParseException e) {
		    if (e == null) {
		      LogUtils.LOGD("vic", "successfully subscribed to the broadcast channel.");
		    } else {
		    	LogUtils.LOGD("vic", "failed to subscribe for push");
		    }
		  }
		});		
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
/*		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
*/		
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
        .writeDebugLogs()
        .denyCacheImageMultipleSizesInMemory()
        .build();

		
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
