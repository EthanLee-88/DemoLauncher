package com.example.demolauncher.mvp.simple1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demolauncher.R;
import com.example.demolauncher.databinding.FragmentMvvmMvpBinding;
import com.example.demolauncher.retrofit.NetRequestTool;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class MvpFragment extends Fragment implements PresenterResultCallbackInterface{
    private static final String TAG = "MvpFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMvvmMvpBinding binding =
                DataBindingUtil.inflate(inflater , R.layout.fragment_mvvm_mvp , container , false);
        return getBindingView(inflater , container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBindingData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private FragmentMvvmMvpBinding mBinding;
    private View getBindingView(LayoutInflater inflater , ViewGroup container){ //dataBinding获取布局
        FragmentMvvmMvpBinding binding =
                DataBindingUtil.inflate(inflater , R.layout.fragment_mvvm_mvp ,
                        container , false);
        mBinding = binding;
        return binding.getRoot();
    }

    private void setBindingData(){
        MvvmHero mvvmHero = new MvvmHero("tony" , "钢铁侠" , "战士");
        mBinding.setMvvmHero(mvvmHero);
        mBinding.mvvmButton.setOnClickListener((View view) -> {

        });
    }

    public void initRes(){  //将MVP三者连接
        NetRequestTool netRequestTool = NetRequestTool.getNetRequestToolInstance();
        MvpPresenter mvpPresenter = new MvpPresenter(netRequestTool , this);
        setPresenter(mvpPresenter);
    }

    private MvpPresenter mMvpPresenter;
    public void setPresenter(MvpPresenter presenter) {
        mMvpPresenter = presenter;
    }

    private void getData(String token , String path){
        if (mMvpPresenter != null) mMvpPresenter.getRequestWithToken(token , path);
    }

    @Override
    public void onPresenterDataResult(JSONObject o) {  //presenter的数据回调

    }

    @Override
    public boolean isAlive() {
        return isAdded();
    }
}
