package tw.tasker.babysitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("baby")
public class Baby extends ParseObject {

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

	public static ParseQuery<Baby> getQuery() {
		return ParseQuery.getQuery(Baby.class);
	}

}
