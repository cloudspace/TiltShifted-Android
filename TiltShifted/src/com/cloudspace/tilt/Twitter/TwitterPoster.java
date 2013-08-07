package com.cloudspace.tilt.Twitter;

import java.io.File;

import com.cloudspace.tilt.ImageManip.ImageLoader;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class TwitterPoster extends Activity {
	
	/** Initialize variables */
	private SharedPreferences prefs;
	protected static final int EDIT_MESSAGE1 = 0;
	protected static final int EDIT_MESSAGE2 = 1;
	public String shareTweet = "";
	public Boolean isAuthed=false;
	public String galleryImage;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        new TwitterLogin().execute();
 	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	public void sendTweet() {
		// Begin sharing photo
		Intent i = new Intent(
        		Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        		startActivityForResult(i,0);
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
 		super.onActivityResult(requestCode, resultCode, data);
 		// Get file path of image to be shared
 		galleryImage = ImageLoader.loadFromGallery(galleryImage, data, this);
 		Thread t = new Thread() {
	        public void run() {
	        	try{
	        		// Post image to Twitter
	        		Twitter twitter = TwitterUtils.twitter;
	        		Uri uri = Uri.fromFile(new File(galleryImage));
	        		File file = new File(uri.getPath());
	                StatusUpdate status = new StatusUpdate("Created with #TiltShifted for Android by @Cloudspace!");
	                status.setMedia(file);
	                twitter.updateStatus(status);}
	            catch(TwitterException e){
	                try {
						throw e;
					} catch (TwitterException e1) {
						e1.printStackTrace();
					}
	            }
	        }
 		}    ;
 		t.start();
 		/** End the activity and send user back to the main activity */
 		finish();
	}
	
    private class TwitterLogin extends AsyncTask<Void, Integer, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// Check if user is already logged in
		    if (TwitterUtils.isAuthenticated(prefs)) {
		    	isAuthed = true;
		    } 
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// If user is not logged in prompt them to
			if(isAuthed==false){
				Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
				startActivity(i);
				finish();
			}
			else{
				// Update status
				sendTweet();
			}
		}
    }
}