package tw.tasker.babysitter.layer;

import com.layer.sdk.exceptions.LayerException;

/*
 * LayerCallbacks.java
 * Defines all the possible callbacks from the Layer Connection and Authentication flow. The
 *  ActivityBase class implements this interface, and each Activity can then decide how it wants to
 *  handle the change in state.
 */

public interface LayerCallbacks {

    //Layer connection callbacks
    public void onLayerConnected();
    public void onLayerDisconnected();
    public void onLayerConnectionError(LayerException e);

    //Layer authentication callbacks
    public void onUserAuthenticated(String id);
    public void onUserAuthenticatedError(LayerException e);
    public void onUserDeauthenticated();
}
