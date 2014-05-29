package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Favorite")
public class Favorite extends ParseObject {

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
	
	public static ParseQuery<Favorite> getQuery() {
		return ParseQuery.getQuery(Favorite.class);
	}

}
