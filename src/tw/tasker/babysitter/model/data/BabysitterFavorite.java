package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("BabysitterFavorite")
public class BabysitterFavorite extends ParseObject {

	public ParseUser getUser() {
		return getParseUser("user");
	}
	
	public void setUser(ParseUser user) {
		put("user", user);
	}
	
	public Babysitter getBabysitter() {
		return (Babysitter) getParseObject("Babysitter");
	}
	
	public void setBaby(Babysitter babysitter) {
		put("Babysitter", babysitter);
	}
	
	public static ParseQuery<BabysitterFavorite> getQuery() {
		return ParseQuery.getQuery(BabysitterFavorite.class);
	}
}
