package com.cloudspace.tilt.ImageManip;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class Orientation {
	public static Bitmap orientImage(Bitmap baseImage, String index){
    	int orientation;
        try {
        	// Get the orientation of the image
            ExifInterface exif = new ExifInterface(index);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();

            // Rotate the image based on its current orientation
            switch(orientation){
                case 3:
                    matrix.postRotate(180);
                    break;
                case 6:
                    matrix.postRotate(90);
                    break;
                case 8:
                    matrix.postRotate(270);
                    break;
            }
            // Return the properly oriented image 
            return Bitmap.createBitmap(baseImage, 0, 0, baseImage.getWidth(), baseImage.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return baseImage;
    }
}
