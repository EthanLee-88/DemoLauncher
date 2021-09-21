package com.example.demolauncher.retrofit;

import com.example.demolauncher.mvp.simple1.ModelDataCallBackInterface;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bruce on 19/7/20.
 */
public class NetRequestTool {
    private static final String TAG = "NetRequestTool";
    private static RetrofitRequestInterface mRetrofitRequestInterface;
    private static NetRequestTool mNetRequestTool;

    public static NetRequestTool getNetRequestToolInstance(){
        if (mNetRequestTool == null){
            synchronized (NetRequestTool.class){
                if (mNetRequestTool == null){
                    mNetRequestTool = new NetRequestTool();
                }
            }
        }
        return mNetRequestTool;
    }

    public static RetrofitRequestInterface getRequestInterface(){
        if (mRetrofitRequestInterface == null){
            synchronized (NetRequestTool.class){
                if (mRetrofitRequestInterface == null){
                    mRetrofitRequestInterface
                            = RetrofitInstance.getRetrofitInstance().create(RetrofitRequestInterface.class);
                }
            }
        }
        return mRetrofitRequestInterface;
    }

    /******
     * get方法同步请求
     * *****/
    public String getRequestWithToken(String Token , String path){
        String result = null;
        try {
            result = getRequestInterface().getRequestWithToken(Token , path).execute().body().string();
        }catch (IOException e){
        }
        return result;
    }
    /********
     * post方法同步请求
     * ********/
    public String postRequestWithToken(String token , String path , String params){
        String result = null;
        try {
            result = getRequestInterface().postRequestWithToken(token , path , params).execute().body().string();
        }catch (IOException io){

        }
        return result;
    }

    /************
     * get方法请求的RXJava形式
     * ************/
    public void getRequestWithTokenByRxJava(final ModelDataCallBackInterface callBack ,
                                            String token , String path){
       Disposable disposable = getRequestInterface().getRequestWithTokenByRXJava(token , path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((JSONObject jsonObject) -> {
                    callBack.onDataResult(jsonObject);
                });
    }

    public void postRequestWithTokenByRxJava(final ModelDataCallBackInterface callBack ,
                                 String token , String path , String params){
        Disposable disposable = getRequestInterface()
                .postRequestWithResultByRXJava(token , params , params)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((JSONObject jsonObject) -> {
                    callBack.onDataResult(jsonObject);
                });
    }
    private Consumer consumer = new Consumer<JSONObject>() {
        @Override
        public void accept(JSONObject jsonObject) throws Exception {

        }
    };

}
