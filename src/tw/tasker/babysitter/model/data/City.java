package tw.tasker.babysitter.model.data;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("City")
public class City extends ParseObject {


	public String getName() {
		String value = getString("name");
		return value;
	}
	public void setName(String value) {
		put("name", value);
	}
	
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}
	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}

	public static ParseQuery<City> getQuery() {
		return ParseQuery.getQuery(City.class);
	}
}
