package com.example.demolauncher.lru;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageLoader {
    private Context mContext;
    private LruCache<String , Bitmap> mLruCache;

    private ImageLoader(Context context){
        mContext = context;
        createLruCache();
        createDiskLruCache();
    }

    /****
     *  初始化 LruCache
     * ***/
    private void createLruCache(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024; // 获取应用最大内存，以KB为单位
        int cacheSize = maxMemory / 8; //取应用内存的八分之一作为bitmap缓存
        if (mLruCache == null) mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    /******
     * 初始化 DiskLruCache
     * ******/
    private DiskLruCache diskLruCache;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//设置磁盘空间缓存大小50MB
    private static final String BITMAP_PATH_NAME = "bitmap";
    private void createDiskLruCache(){
        File cachePath = createDiskPath(mContext , BITMAP_PATH_NAME);
        if (!cachePath.exists()) cachePath.mkdir();
        if (getDeviceSpace(cachePath) > DISK_CACHE_SIZE) {
            try {
                diskLruCache = DiskLruCache.open(cachePath , 1 , 1 , DISK_CACHE_SIZE);
            }catch (IOException i){

            }
        }
    }

    /*************
     *
     * 将网络图片写入磁盘
     * *************/
    private static final int DISK_CACHE_INDEX = 0;
    private Bitmap putDiskCacheBitmap(String url , int reqWidth , int reqHeight){
        if (Looper.myLooper() == Looper.getMainLooper())
            throw new RuntimeException("main thread to handle io !");
        if (diskLruCache == null) return null;
        Bitmap bitmap = null;
        String key = genMD5(url);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (loadBitmapToStream(url , outputStream)){
                    editor.commit();
                }else {
                    editor.abort();
                }
                diskLruCache.flush();
            }
        }catch (IOException io){

        }
        return bitmap;
    }
    /********
     * 从磁盘中读取数据
     * ********/
    private Bitmap getDiskCache(String url , int reqWidth , int reqHeight){
        if (Looper.myLooper() == Looper.getMainLooper())
            throw new RuntimeException("main thread to handle io !");
        if (diskLruCache == null) return null;
        Bitmap bitmap = null;
        String key = genMD5(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null){
                FileInputStream fi = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                FileDescriptor fd = fi.getFD();
                bitmap = BitmapSample.bitmapSimpleFromFile(fd , reqWidth , reqHeight);
            }
        }catch (IOException io){}
        return bitmap;
    }

    /*****
     *
     * 将网络输入流数据写入磁盘的输出流
     * ****/
    private boolean loadBitmapToStream(String url , OutputStream out){
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            final URL finalUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) finalUrl.openConnection();
            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream() , 8 * 1024);
            BufferedOutputStream bOut = new BufferedOutputStream(out);
            int bit;
            while ((bit = bufferedInputStream.read()) != -1){
                bOut.write(bit);
            }
            return true;
        }catch (Exception e){

        }finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            try {
                bufferedInputStream.close();
            }catch (Exception e){}
        }
        return false;
    }
    /****
     * url转换成MD5值
     * ****/
    private String genMD5(String urlStr){
        String md5 = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(urlStr.getBytes());
            md5 = byteToHexString(messageDigest.digest());
        }catch (NoSuchAlgorithmException e){
            md5 = String.valueOf(urlStr.hashCode());
        }
        return md5;
    }
    /*******
     * 十进制数组转换成十六进制字符串
     * ********/
    private String byteToHexString(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0 ; i < bytes.length ; i ++){
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() < 2) stringBuffer.append("0");
            stringBuffer.append(hex);
        }
        return stringBuffer.toString();
    }
    /****
     * 获取剩余内存
     * *****/
    private long getDeviceSpace(File path){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) return path.getUsableSpace();
        final StatFs statFs = new StatFs(path.getPath());
        return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
    }
    /****
     * 获取外存或内存缓存的磁盘缓存路径
     * @param context
     * @param pathName unique name of the cache
     * ****/
    private File createDiskPath(Context context , String pathName){
        boolean available = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String dir;
        if (available){
            dir = context.getExternalCacheDir().getPath();// 外部存储可用，优先使用外部存储
        }else {
            dir = context.getCacheDir().getPath(); // 使用内部存储
        }
        return new File(dir + File.separator + pathName); //存储缓存路径+/+文件夹
    }


    private void addBddBitmapToCache(String key , Bitmap bitmap){
        if ((key != null) && (bitmap != null)){
            if (getBitmapFromCache(key) == null){
                if (mLruCache != null) mLruCache.put(key , bitmap);
            }
        }
    }

    private Bitmap getBitmapFromCache(String key){
        if (key != null){
            if (mLruCache != null) return mLruCache.get(key);
        }
        return null;
    }

    private Bitmap loadBitmapFromUrl(String url){
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream = null;
        try {
           final URL finalUrl = new URL(url);
           httpURLConnection = (HttpURLConnection) finalUrl.openConnection();
           bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream() , 8 * 1024);
           bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        }catch (Exception e){

        }finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            try {
                bufferedInputStream.close();
            }catch (Exception e){}
        }
        return bitmap;
    }
}
