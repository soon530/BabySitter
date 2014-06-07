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
	
	public Baby getBaby() {
		return (Baby) getParseObject("baby");
	}
	
	public void setBaby(Baby baby) {
		put("baby", baby);
	}
	
	public static ParseQuery<BabyFavorite> getQuery() {
		return ParseQuery.getQuery(BabyFavorite.class);
	}

}
