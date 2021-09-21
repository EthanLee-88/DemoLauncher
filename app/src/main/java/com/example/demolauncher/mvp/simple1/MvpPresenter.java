package com.example.demolauncher.mvp.simple1;

import com.example.demolauncher.retrofit.NetRequestTool;

import org.json.JSONObject;
/*******
 * MVP Presenter将View部分和Model部分解耦，View端通过Presenter
 * 间接调用Model数据模块，Presenter则通过接口向View端返回数据
 * 与此同时，Presenter直接调用Model的方法，Model则通过接口向
 * Presenter返回结果。
 * MVVM结合官方支持库data Binding主要以减少样板代码的编写为主
 * data binding可实现数据model与view双向绑定，以进一步减轻view
 * 的负担。
 * java framework层由java编写，提供应用层所需的各种API。JNI层
 * 由native代码编写，JNI层作为连接java层和native层的桥梁，可以
 * 实现java方法与native方法的相互调用，其中native方法一般用C/CPP
 * 编写。
 * *******/
public class MvpPresenter implements ModelDataCallBackInterface<JSONObject>{
    private static final String TAG = "MvpPresenter";
    private NetRequestTool mNetRequestTool;
    private PresenterResultCallbackInterface mPreCallback;

    public MvpPresenter(NetRequestTool netRequestTool , PresenterResultCallbackInterface preCallback){
        mNetRequestTool = netRequestTool;
        mPreCallback = preCallback;
    }

    public void getRequestWithToken(String token , String path){
        if (mNetRequestTool != null)
            mNetRequestTool.getRequestWithTokenByRxJava(this , token , path);
    }

    public void postRequestWithToken(String token , String path , String params){
        if (mNetRequestTool != null)
            mNetRequestTool.postRequestWithTokenByRxJava(this , token , path , params);
    }

    @Override
    public void onDataResult(JSONObject result) {  //model模块数据回调
        if (mPreCallback != null) {
            if (mPreCallback.isAlive()) mPreCallback.onPresenterDataResult(result);
        }
    }
}
