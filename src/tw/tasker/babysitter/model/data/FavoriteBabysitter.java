package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("FavoriteBabysitter")
public class FavoriteBabysitter extends ParseObject {

	public ParseUser getUser() {
		return getParseUser("user");
	}
	
	public void setUser(ParseUser user) {
		put("user", user);
	}
	
	public Babysitter getBabysitter() {
		return (Babysitter) getParseObject("babysitter");
	}
	
	public void setBaby(Babysitter babysitter) {
		put("babysitter", babysitter);
	}
	
	public static ParseQuery<FavoriteBabysitter> getQuery() {
		return ParseQuery.getQuery(FavoriteBabysitter.class);
	}
}
