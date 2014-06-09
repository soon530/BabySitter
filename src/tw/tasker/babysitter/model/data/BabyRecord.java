package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("BabyRecord")
public class BabyRecord extends ParseObject {

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

	public BabyDiary getBaby() {
		return (BabyDiary) getParseObject("BabyDiary");
	}
	
	public void setBaby(BabyDiary value) {
		put("BabyDiary", value);
	}
	
	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}

	public ParseFile getPhotoFile() {
		return getParseFile("photo");
	}

	public void setPhotoFile(ParseFile file) {
		put("photo", file);
	}

	public static ParseQuery<BabyRecord> getQuery() {
		return ParseQuery.getQuery(BabyRecord.class);
	}
}
