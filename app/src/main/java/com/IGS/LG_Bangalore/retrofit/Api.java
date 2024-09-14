package com.IGS.LG_Bangalore.retrofit;



import com.IGS.LG_Bangalore.Response.AirConditionResponse;
import com.IGS.LG_Bangalore.Response.RefrigeratorResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("postData.php")
    Call<AirConditionResponse> getACResponse(@Field("id") String id);

    @FormUrlEncoded
    @POST("setData.php")
    Call<AirConditionResponse> getSetAcResponse(@Field("id") String id, @Field("id1") String id1);

    @FormUrlEncoded
    @POST("setData.php")
    Call<AirConditionResponse> getSetAcTemp(@Field("id") String id, @Field("id1") String id1);

    @FormUrlEncoded
    @POST("postData.php")
    Call<RefrigeratorResponse> getFreezeResponse(@Field("id") String id);

    @FormUrlEncoded
    @POST("setData.php")
    Call<RefrigeratorResponse> getSetFreezeResponse(@Field("id") String id, @Field("id1") String id1);

}