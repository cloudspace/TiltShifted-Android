package com.cloudspace.tilt.Tools;

import com.cloudspace.tilt.MainActivity;

import android.os.AsyncTask;
import android.view.View;

public class SaveAsync extends AsyncTask<Void, Integer, Void>{

	MainActivity main;
	
	public SaveAsync(MainActivity mainActivity){
		main = mainActivity;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		main.progressbar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Save the image
		main.tilt.save(main.progressbar);
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
	
}