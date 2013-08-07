package com.cloudspace.tilt.ImageManip;

import com.cloudspace.tilt.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageLoader {
	// Return the file path of the image selected from the gallery
	public static String loadFromGallery(String picturePath, Intent data, Context context){
		Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();
		return picturePath;
	}
	
	// Return the file path of the image taken by the camera
	public static String loadFromCamera(String picturePath, Uri imageUri, MainActivity context){
		String [] proj={MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,};
    	Cursor cursor = context.getContentResolver().query( imageUri,
    	            proj, // Which columns to return
    	            null,       // WHERE clause; which rows to return (all rows)
    	            null,       // WHERE clause selection arguments (none)
    	            null); // Order-by clause (ascending by name)
    	cursor.moveToFirst();
    	int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
    	picturePath = cursor.getString(columnIndex);
     	cursor.close();
		return picturePath;
	}
}
