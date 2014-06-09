package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("BabyDiary")
public class BabyDiary extends ParseObject {

	public String getName() {
		String value = getString("name");
		return value;
	}

	public void setName(String value) {
		put("name", value);
	}

	public String getNote() {
		String value = getString("note");
		return value;
	}

	public void setNote(String value) {
		put("note", value);
	}
	
	public int getFavorite() {
		int value = getInt("favorite");
		return value;
	}

	public void setFavorite(int value) {
		put("favorite", value);
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

	public void setBabysitter(Babysitter value) {
		put("Babysitter", value);
	}
	
	public Babysitter getBabysitter() {
		return (Babysitter) getParseObject("Babysitter");
	}

	public boolean getIsPublic() {
		return getBoolean("isPublic");
	}
	
	public void setIsPublic(boolean value) {
		put("isPublic", value);
	}
	
	public static ParseQuery<BabyDiary> getQuery() {
		return ParseQuery.getQuery(BabyDiary.class);
	}

}
