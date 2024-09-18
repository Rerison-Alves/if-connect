package com.if_connect.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageManager {

    public static Bitmap base64StringToBitmap(String imageBase64) {
        if(imageBase64!=null){
            byte[] bytesimagem = Base64.decode(imageBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytesimagem, 0, bytesimagem.length);
        }else {
            return null;
        }
    }

    public static String bitmapToBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
}
