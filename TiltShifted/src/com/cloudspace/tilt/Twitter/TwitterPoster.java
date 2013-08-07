package com.cloudspace.tilt.Twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
		}
    }
}