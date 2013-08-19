package com.cloudspace.tilt.Facebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cloudspace.tilt.MainActivity;
import com.cloudspace.tilt.R;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class FacebookPoster {
	public static MainActivity activity;
	public Bitmap shareImage;
	public static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	public ViewGroup controlsContainer;
    public UiLifecycleHelper uiHelper;
    public Session.StatusCallback statusCallback = new SessionStatusCallback();
    public final String PENDING_ACTION_BUNDLE_KEY = "com.cloudspace.tilt.mainactivity:PendingAction";
    public static PendingAction pendingAction = PendingAction.NONE;
	public enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
	
	// Pass in the main activity to access the UI
	public FacebookPoster(MainActivity mainActivity) {
		activity = mainActivity;
	}

    public Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        }
    }

	@SuppressWarnings("incomplete-switch")
	public static void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
            	activity.postPhoto();
                break;
        }
    }
    
    public static boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    public void sharePhoto(String string, File sharePath) throws FileNotFoundException{
    	Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), sharePath, string, new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
            	activity.progressbar.setVisibility(View.INVISIBLE);
                showPublishResult(response.getError());
            }
        });
        request.executeAsync();
    }
    
    // Notify the user if the image was posted
    public void showPublishResult(FacebookRequestError error) {
        if (error == null) {
        	Toast.makeText(activity,"Image Posted",Toast.LENGTH_SHORT).show();
        } else {
        	Toast.makeText(activity,"There was an issue sharing your photo. Please try again.",Toast.LENGTH_SHORT).show();
        }
    }
	
	public static void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(activity)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
                pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
        	handlePendingAction();
        }
    }
	
	public void performPublish(PendingAction action) {
        Session session = Session.getActiveSession();
        if (session != null) {
        	pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
            	handlePendingAction();
            } else {
                // We need to get new permissions, then complete the action when we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(activity, PERMISSIONS));
            }
        }
    }
}
