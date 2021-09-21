package com.example.demolauncher.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.UnrepeatableRequestBody;

import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_PROXY_AUTH;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;
import static okhttp3.internal.Util.closeQuietly;
import static okhttp3.internal.http.StatusLine.HTTP_PERM_REDIRECT;
import static okhttp3.internal.http.StatusLine.HTTP_TEMP_REDIRECT;


/******
 * OkHttp 源码解析
 *
 *  API的方法发起请求，底层通过Dispatcher调度器来实现并发请求的管理，
 *  调度器包含并发执行任务数、主机数以及三个队列。
 *  三个队列分别用于装载等待请求以及正在请求的同步及异步请求。
 *  (OkHttp默认最大并行任务数 < 64 , 同一主机在线程池的最大任务数 < 5 )
 *
 *  API顶层Call的异步请求操作enqueue，在底层实际上是将请求打包成AsyncCall再交由
 *  Dispatcher来调度的，而AsyncCall就是一个Runnable，Dispatcher执行请求任务的时候
 *  就是将此Runnable丢给线程池执行。
 *  当并发请求数量过大时Dispatcher会将Runnable放入等待队列。Dispatcher由Call的实现类RealCall
 *  来获取。
 *  同步请求操作下，请求并不会交由Dispatcher调度的线程池中执行，而是在Call下直接进行网络请求，
 *  串行阻塞的。
 *  API底层的网络请求交由Call的getResponseWithInterceptorChain方法执行。
 *
 *  缓存策略 ： OkHttp采用基于LRU算法的缓存策略。
 *           缓存策略主要分两个部分来处理。第一部分是不联网的情况下，检查是否有缓存，若没有则
 *           返回504错误，若有可用缓存则使用缓存。
 *           第二部分是联网的情况下，在有本地缓存的情况下，会检查Http的的响应报头，如果缓存还在
 *           有效期内则使用缓存，并联网更新缓存。如果缓存失效则直接联网获取。
 *           三级缓存的操作在 CacheInterceptor 类里。
 *
 *  OkHttp复用连接池以维持长链接，支持5个并发Socket连接，KeepAlive时间是5分钟，当连接空闲超过5
 *  分钟后便被回收。
 * *******/
public class OkHttpEngine {
    private static final String TAG = "OkHttpEngine";
    private OkHttpEngine mOkHttpEngine;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    public OkHttpEngine getInstance(Context context) {
        if (mOkHttpEngine == null) {
            synchronized (OkHttpEngine.class) {
                if (mOkHttpEngine == null) {
                    mOkHttpEngine = new OkHttpEngine(context);
                }
            }
        }
        return mOkHttpEngine;
    }

    private OkHttpEngine(Context context) {
        File cache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(cache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /******
     * get 异步方法
     * *****/
    public void getAsynRequest(String url, ResultCallback resultCallback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = mOkHttpClient.newCall(request);
        parseResultGet(call, resultCallback);
    }

    private void parseResultGet(Call call, ResultCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailGet(call, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessGet(response, callback);
            }
        });
    }

    private void sendSuccessGet(Response response, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onSuccess(response);
            }
        });
    }

    private void sendFailGet(Call call, IOException e, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onFail(call.request(), e);
            }
        });
    }

    public void postAsynRequest(String url, ResultCallback resultCallback) {
        RequestBody body = new FormBody.Builder()
                .add("kay", "value")
                .build();
        Request request = new Request.Builder().url(url)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        parseResultPost(call, resultCallback);
    }

    private void parseResultPost(Call call, ResultCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailPost(call, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessPost(response, callback);
            }
        });
    }

    private void sendSuccessPost(Response response, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onSuccess(response);
            }
        });
    }

    private void sendFailPost(Call call, IOException e, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onFail(call.request(), e);
            }
        });
    }
}




