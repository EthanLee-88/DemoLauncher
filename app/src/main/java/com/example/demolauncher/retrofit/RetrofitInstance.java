package com.example.demolauncher.retrofit;

import com.example.demolauncher.application.BaseApplication;
import com.example.demolauncher.util.InternetStatus;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*******
 * Retrofit基于OkHttp，通过注解提供网络请求、传参等功能，方便灵活。
 * 同时提供网络响应数据转换。
 *
 * *********/
public class RetrofitInstance {
    private volatile static  Retrofit retrofit;
    private static final int READ_TIME_OUT = 20;
    private static final int CONNECT_TIME_OUT = 20;
    private static final int WRITE_TIME_OUT = 20;
    private static final String CACHE_NAME = "cache";
    private static boolean DEBUG_MODE = true;

    public static Retrofit getRetrofitInstance() {
        final String  baseUrl = "" + "/";
        if (retrofit == null) {
            synchronized (NetRequestTool.class) {
                if (retrofit == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    if (DEBUG_MODE) {
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                    }
                    File cacheFile = new File(BaseApplication.applicationContext.getExternalCacheDir(), CACHE_NAME);
                    Cache cache = new Cache(cacheFile,1024 * 1024 * 20);
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(loggingInterceptor)
                            .addInterceptor(cacheControlInterceptor)
                            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);
                    retrofit = new Retrofit.Builder()
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .baseUrl(baseUrl)
                            .client(builder.build())
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static final Interceptor cacheControlInterceptor = new Interceptor() {   //缓存设置
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!InternetStatus.isNetAvailable(BaseApplication.applicationContext)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            if (InternetStatus.isNetAvailable(BaseApplication.applicationContext)) {
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
}
