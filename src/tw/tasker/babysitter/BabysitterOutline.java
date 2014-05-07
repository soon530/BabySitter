package tw.tasker.babysitter;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("babysitter")
public class BabysitterOutline extends ParseObject {

	public String getText() {
		String value = getString("name");
		return value;
	}
	
	public void setText(String value) {
		put("text", value);
	}
	
	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}

	public static ParseQuery<BabysitterOutline> getQuery() {
		return ParseQuery.getQuery(BabysitterOutline.class);
	}
}
