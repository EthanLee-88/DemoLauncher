package com.example.demolauncher.lru;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class BitmapSample {

    private Bitmap bitmapSimpleFromResouces(Resources res , int resId , int reqHeight , int reqWidth){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res , resId , options);
        options.inSampleSize  = getSimpleSize(options , reqWidth , reqHeight); // 根据所需尺寸计算采样率
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res , resId , options);
    }

    public static Bitmap bitmapSimpleFromFile(FileDescriptor descriptor , int reqWidth , int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(descriptor , null , options);
        options.inSampleSize = getSimpleSize(options , reqWidth , reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(descriptor , null , options);
    }

    private static int getSimpleSize(BitmapFactory.Options options, int reqWidth , int reqHeight){
        if ((reqHeight == 0) || (reqWidth == 0)) return 1;
        final int bitmapWidth = options.outWidth;
        final int bitmapHeight = options.outHeight;
        int simpleSize = 1;
        if (bitmapWidth > reqWidth || bitmapHeight > reqHeight){
            final int halfWidth = bitmapWidth / 2;
            final int haltHeight = bitmapHeight / 2;
            while ((halfWidth / simpleSize > reqWidth) && (haltHeight / simpleSize > reqHeight)){
                simpleSize *= 2;
            }
        }
        return simpleSize;
    }
}
