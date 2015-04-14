package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}
	
	public String getName() {
		return getString("name");
	}
	
	public void setName(String vluae) {
		put("name", vluae);
	}
	
	public String getAddress() {
		return getString("address");
	}
	
	public void setAddress(String vluae) {
		put("address", vluae);
	}
	
	public String getPhone() {
		return getString("phone");
	}
	
	public void setPhone(String value) {
		put("phone", value);
	}
	
	public String getKidsAge() {
		return getString("kidsAge");
	}
	
	public void setKidsAge(String vluae) {
		put("kidsAge", vluae);
	}
	
	public String getKidsGender() {
		return getString("kidsGender");
	}
	
	public void setKidsGender(String vluae) {
		put("kidsGender", vluae);
	}

	public ParseFile getAvatorFile() {
		return getParseFile("avator");
	}

	public void setAvatorFile(ParseFile file) {
		put("avator", file);
	}
	
	public static ParseQuery<UserInfo> getQuery() {
		return ParseQuery.getQuery(UserInfo.class);
	}

}
