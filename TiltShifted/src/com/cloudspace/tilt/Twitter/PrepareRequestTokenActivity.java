/*@author  https://github.com/ddewaele */

package com.cloudspace.tilt.Twitter;


import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Prepares a OAuthConsumer and OAuthProvider 
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret.
 * OAuthProvider is configured with the 3 OAuth endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
public class PrepareRequestTokenActivity extends Activity {

	final String TAG = getClass().getName();
	
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    private final Handler mTwitterHandler = new Handler();
    final Runnable mUpdateTwitterNotification = new Runnable() {
    	
    	/** Notify user of update */
        public void run() {
        	Toast.makeText(getBaseContext(), "You are now logged in to Twitter and may post your photo!", Toast.LENGTH_SHORT).show();
        }
    };
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	try {
    		this.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    		this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,Constants.ACCESS_URL,Constants.AUTHORIZE_URL);
    	} catch (Exception e) {
		}

		new OAuthRequestTokenTask(this,consumer,provider).execute();
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the request token).
	 * The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent); 
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			new RetrieveAccessTokenTask(this,consumer,provider,prefs).execute(uri);
			finish();	
		}
	}
	
	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;
		
		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,OAuthProvider provider, SharedPreferences prefs) {
			this.consumer = consumer;
			this.provider = provider;
			this.prefs=prefs;
		}


		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret 
		 * for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri...params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();
				
				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				
				consumer.setTokenWithSecret(token, secret);

				executeAfterAccessTokenRetrieval();
				
				
			} catch (Exception e) {
			}

			return null;
		}


		private void executeAfterAccessTokenRetrieval() {
			mTwitterHandler.post(mUpdateTwitterNotification);
				finish();
		}
	}	
	
}
