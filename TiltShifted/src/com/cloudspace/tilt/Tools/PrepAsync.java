package com.cloudspace.tilt.Tools;

import com.cloudspace.tilt.MainActivity;

import android.os.AsyncTask;
import android.view.View;

public class PrepAsync extends AsyncTask<Void, Integer, Void>{

	MainActivity main;
	
	public PrepAsync(MainActivity mainActivity){
		main = mainActivity;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		main.prepareViews();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Pass in base values and display the blurred images
		main.prepareViews();
		main.tilt.shiftTop(main.width,100);
		main.tilt.shiftBottom(main.width,50);
		main.imageView2.setImageBitmap(main.tilt.topShift);
		main.imageView3.setImageBitmap(main.tilt.bottomShift);
		main.progressbar.setVisibility(View.INVISIBLE);
	}
}
