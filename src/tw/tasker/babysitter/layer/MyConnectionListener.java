package tw.tasker.babysitter.layer;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerConnectionListener;

public class MyConnectionListener implements LayerConnectionListener {

/*
 * MyConnectionListener.java
 * Handles the Connection process. Layer automatically tries to connect during Authentication, when
 *  sending messages, or when the connection is dropped. If the connection is dropped, Layer will
 *  continue to try to connect in the background.
 */

    //The Connection listener will execute callbacks on the current Activity
    private LayerCallbacks mCurrentContext;

    //When a connection is established, execute the appropriate callback
    public void onConnectionConnected(LayerClient client) {
        if(mCurrentContext != null)
            mCurrentContext.onLayerConnected();
    }

    //When a connection is closed/dropped, execute the appropriate callback
    public void onConnectionDisconnected(LayerClient client) {
        if(mCurrentContext != null)
            mCurrentContext.onLayerDisconnected();
    }

    //If there was an error connecting, execute the appropriate callback
    public void onConnectionError(LayerClient client, LayerException e) {
        if(mCurrentContext != null)
            mCurrentContext.onLayerConnectionError(e);
    }

    //Helper function to keep track of the current Activity (set whenever an Activity resumes)
    public void setActiveContext(LayerCallbacks context){
        mCurrentContext = context;
    }
}