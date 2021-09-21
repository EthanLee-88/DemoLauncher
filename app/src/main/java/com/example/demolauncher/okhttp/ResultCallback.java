package com.example.demolauncher.okhttp;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public interface ResultCallback {
   void onSuccess(Response response);
   void onFail(Request request , IOException e);
}
