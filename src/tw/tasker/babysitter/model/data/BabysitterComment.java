package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("BabysitterComment")
public class BabysitterComment extends ParseObject {

	public float getRating() {
		Number rating = getNumber("rating");
		float value = rating.floatValue();
		return value;
	}

	public void setRating(float value) {
		put("rating", value);
	}

	public String getTitle() {
		String value = getString("title");
		return value;
	}

	public void setTitle(String value) {
		put("title", value);
	}

	public String getDescription() {
		String value = getString("description");
		return value;
	}

	public void setDescription(String value) {
		put("description", value);
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}

	public Babysitter getBabysitter() {
		return (Babysitter) getParseObject("Babysitter");
	}

	public void setBabysitter(Babysitter value) {
		put("Babysitter", value);
	}

	public static ParseQuery<BabysitterComment> getQuery() {
		return ParseQuery.getQuery(BabysitterComment.class);
	}
}
