package com.cloudspace.tilt.ImageManip;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class Saturation{
	private static ColorMatrix colorMatrix;  
    private static ColorMatrixColorFilter cmFilter;  
    private static Paint cmPaint;  
  
    //These member variables are responsible for drawing the Bitmap  
    private static Canvas cv;  
    private static Bitmap canvasBitmap;
    
	
	public static Bitmap adjustedHue(Bitmap baseImage, int width, int height)
	{
		colorMatrix = new ColorMatrix();  
        //Initialize the ColorMatrixColorFilter object  
        cmFilter = new ColorMatrixColorFilter(colorMatrix);  
  
        //Initialize the cmPaint  
        cmPaint = new Paint();  
        //Set 'cmFilter' as the color filter of this paint  
        cmPaint.setColorFilter(cmFilter);
        
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        cv = new Canvas(canvasBitmap);
        colorMatrix.setSaturation(1.5f);  
        //Create a new ColorMatrixColorFilter with the recently altered colorMatrix  
        cmFilter = new ColorMatrixColorFilter(colorMatrix);  

        //Assign the ColorMatrix to the paint object again  
        cmPaint.setColorFilter(cmFilter);  

        //Draw the Bitmap into the mutable Bitmap using the canvas. Don't forget to pass the Paint as the last parameter  
        cv.drawBitmap(boost(baseImage, width, height), 0, 0, cmPaint);
		return canvasBitmap;
	}
	
	public static Bitmap boost(Bitmap baseImage,int width, int height) {
	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	 
	    int A, R, G, B;
	    int pixel;
	 
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            pixel = baseImage.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            R = (int)(R * (1));
	            if(R > 255) R = 255;

	            G = (int)(G * (1));
	            if(G > 255) G = 255;


	            B = (int)(B * (1));
	            if(B > 255) B = 255;
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	    return bmOut;
	}
}