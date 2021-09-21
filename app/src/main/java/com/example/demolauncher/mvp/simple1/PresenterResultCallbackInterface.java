package com.example.demolauncher.mvp.simple1;

import org.json.JSONObject;

public interface PresenterResultCallbackInterface {

    void onPresenterDataResult(JSONObject result);
    boolean isAlive();
}
