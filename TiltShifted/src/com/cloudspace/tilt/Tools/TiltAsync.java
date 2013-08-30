package com.cloudspace.tilt.Tools;

import com.cloudspace.tilt.MainActivity;
import com.cloudspace.tilt.TiltShift.TiltShift;

import android.os.AsyncTask;
import android.view.View;

public class TiltAsync extends AsyncTask<Void, Integer, Void>{

	MainActivity main;
	
	public TiltAsync(MainActivity mainActivity){
		main = mainActivity;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		main.progressbar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Initialize TiltShift and pass in the user selected image
		main.tilt = new TiltShift(this.main);
		main.tilt.load(main.baseImage);
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Enable the save button
		main.save.setEnabled(true);
		main.share.setEnabled(true);
		PrepAsync prep = new PrepAsync(this.main);
		prep.execute();
	}

}
