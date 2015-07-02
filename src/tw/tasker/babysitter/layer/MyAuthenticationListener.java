package tw.tasker.babysitter.layer;

import android.util.Log;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerAuthenticationListener;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

public class MyAuthenticationListener implements LayerAuthenticationListener {

/*
 * MyAuthenticationListener.java
 * Handles the Authentication process. When a user is Authenticated through Layer, they are
 *  effectively "signed in" and can send and receive messages.
 *
 *  You can learn more about Authentication here:
 *  https://developer.layer.com/docs/integration#authentication
 *
 *  Part of the Authentication process will involve setting up a Web Service to sign a JWT token.
 *  We have an example Parse Function you will need to configure before launching your app:
 *  https://github.com/layerhq/layer-parse-module
 *
 *  Authentication will execute after the user logs in with Parse, so we will use the Parse User ID
 *   to track all users in this app.
 */

    //The Authentication listener will execute callbacks on the current Activity
    private LayerCallbacks mCurrentContext;

    //After calling LayerClient.authenticate(), a one-time use token is returned. This nonce can only
    // be used to Authenticate one user, and will expire after 10 minutes
    public void onAuthenticationChallenge(final LayerClient client, String nonce) {

        //Validate the current Parse user
        if(ParseUser.getCurrentUser() == null || ParseUser.getCurrentUser().getUsername() == null){
            onAuthenticationError(client, new LayerException(LayerException.Type.USER_NOT_FOUND, "Invalid Parse User ID"));
            return;
        }

        ParseUser user = ParseUser.getCurrentUser();
        String userID = user.getObjectId();

        //Print out the nonce for validation purposes (useful for debugging)
        Log.d("Authentication", "User ID: " + userID + " with nonce: " + nonce);

        // Make a request to your Parse Cloud Function to acquire a Layer identityToken
        HashMap<String, Object> params = new HashMap<>();
        params.put("userID", userID);
        params.put("nonce", nonce);

        ParseCloud.callFunctionInBackground("generateToken", params, new FunctionCallback<String>() {
            public void done(String token, ParseException e) {
                if (e == null) {
                    //Once you have a Valid Identity Token, return it to the Layer SDK which will
                    // complete the Authentication process
                    Log.d("Authentication", "Identity token: " + token);
                    client.answerAuthenticationChallenge(token);
                } else {
                    Log.d("Authentication", "Parse Cloud function failed to be called to generate token with error: " + e.getMessage());
                }
            }
        });
    }

    //Called when the Authentication process completes successfully. Execute the callback in the
    // current Activity
    public void onAuthenticated(LayerClient client, String userName) {
        if(mCurrentContext != null)
            mCurrentContext.onUserAuthenticated(userName);
    }

    //Called when the Authentication process fails. Most likely this will be because of an invalid
    // Identity Token. You can validate your token on the Layer Dashboard under "Authentication"
    public void onAuthenticationError(LayerClient layerClient, LayerException e) {
        if(mCurrentContext != null)
            mCurrentContext.onUserAuthenticatedError(e);
    }

    //Called when the user Deauthenticates (logs out). Execute the callback in the current Activity
    public void onDeauthenticated(LayerClient client) {
        if(mCurrentContext != null)
            mCurrentContext.onUserDeauthenticated();
    }

    //Helper function to keep track of the current Activity (set whenever an Activity resumes)
    public void setActiveContext(LayerCallbacks context){
        mCurrentContext = context;
    }
}