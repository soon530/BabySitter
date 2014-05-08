package tw.tasker.babysitter;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("babysitter")
public class BabysitterOutline extends ParseObject {

	public int getBabycareCount() {
		int value = getInt("babycare_count");
		return value;
	}
	
	public void setBabycareCount(int value) {
		put("babycare_count", value);
	}

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

	public String getAddress() {
		String value = getString("address");
		return value;
	}

	public void setAddress(String value) {
		put("text", value);
	}
	
	public int getTotalRating() {
		int value = getInt("totalRating");
		return value;
	}
	
	public void setTotalRating(int value) {
		put("totalRating", value);
	}
	
	public int getTotalComment() {
		int value = getInt("totalComment");
		return value;
	}
	
	public void setTotalComment(int value) {
		put("totalComment", value);
	}
	public static ParseQuery<BabysitterOutline> getQuery() {
		return ParseQuery.getQuery(BabysitterOutline.class);
	}
}
