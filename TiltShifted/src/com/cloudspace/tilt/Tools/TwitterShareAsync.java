package com.cloudspace.tilt.Tools;

import android.os.AsyncTask;
import android.view.View;

import com.cloudspace.tilt.MainActivity;

public class TwitterShareAsync extends AsyncTask<Void, Integer, Void>{

	MainActivity main;
	
	public TwitterShareAsync(MainActivity mainActivity){
		main = mainActivity;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		main.progressbar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Pass in the file path of the image to be shared
		main.twitterTools.share(main.tilt.sharePath,main.statusUpdate);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
	
}