package tw.tasker.babysitter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Config {
	public static final String BABYSITTER_OBJECT_ID = "babysitterObjectId";
	public static final String BABY_OBJECT_ID = "babyObjectId";
	public static final String TOTAL_RATING = "totalRating";
	public static final String TOTAL_COMMENT = "totalComent";
	
	public static final String TOTAL_RECORD = "totalRecord";
	
	public static final int OBJECTS_PER_PAGE = 7;

	public static final DisplayImageOptions OPTIONS 
	= new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new SimpleBitmapDisplayer())
	.showImageOnFail(R.drawable.ic_launcher)
	.showImageOnLoading(R.drawable.ic_action_name)
	.build();

}
