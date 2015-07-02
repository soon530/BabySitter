package tw.tasker.babysitter.parse;

import com.parse.ParseException;
import com.parse.ParseUser;

/*
 * ParseLoginCallbacks.java
 * Defines all the possible callbacks from the Parse sign in and registration process.
 */

public interface ParseLoginCallbacks {

    public void loginSucceeded(ParseUser user);
    public void loginFailed(ParseException e);
}
