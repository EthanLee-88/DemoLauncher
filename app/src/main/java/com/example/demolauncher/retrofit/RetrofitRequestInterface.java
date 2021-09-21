package com.example.demolauncher.retrofit;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface RetrofitRequestInterface {

    @GET
    Call<ResponseBody> getRequest(@Url String url);

    @GET
    Call<ResponseBody> getRequestWithToken(@Header("token") String token, @Url String url);

    @POST
    @Headers("content-type: application/json")
    Call<ResponseBody> postRequest(@Url String url, @Body String param);

    @POST
    @Headers("content-type: application/json")
    Call<ResponseBody> postRequestWithToken(@Header("token") String token, @Url String url, @Body String param);

    @Multipart
    @POST
    Call<ResponseBody> uploadMemberIcon(@Header("token") String token, @Url String url, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @POST
    @Multipart
    Call<ResponseBody> uploadECGraph(@Header("token") String token, @Url String url, @Part MultipartBody.Part file);

    /************ RXJava与 retrofit结合***********/
    @GET
    Observable<JSONObject> getRequestWithTokenByRXJava(@Header("token") String token , @Url String url);

    @POST
    @Headers("content-type: application/json")
    Observable<JSONObject> postRequestWithResultByRXJava(@Header("token") String token , @Url String url , @Body String params);
}
