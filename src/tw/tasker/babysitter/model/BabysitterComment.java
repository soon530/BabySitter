package tw.tasker.babysitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class BabysitterComment extends ParseObject {

	public int getRating() {
		int value = getInt("rating");
		return value;
	}
	
	public void setRating(int value) {
		put("rating", value);
	}

	public String getTitle() {
		String value = getString("comment");
		return value;
	}

	public void setTitle(String value) {
		put("comment", value);
	}
	public String getComment() {
		String value = getString("comment");
		return value;
	}

	public void setComment(String value) {
		put("comment", value);
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}
	
	
	
	public static ParseQuery<BabysitterComment> getQuery() {
		return ParseQuery.getQuery(BabysitterComment.class);
	}
}
