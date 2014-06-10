package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("BabyFavorite")
public class BabyFavorite extends ParseObject {

	public ParseUser getUser() {
		return getParseUser("user");
	}
	
	public void setUser(ParseUser user) {
		put("user", user);
	}
	
	public BabyDiary getBabyDiary() {
		return (BabyDiary) getParseObject("BabyDiary");
	}
	
	public void setBabyDiary(BabyDiary baby) {
		put("BabyDiary", baby);
	}
	
	public static ParseQuery<BabyFavorite> getQuery() {
		return ParseQuery.getQuery(BabyFavorite.class);
	}

}
