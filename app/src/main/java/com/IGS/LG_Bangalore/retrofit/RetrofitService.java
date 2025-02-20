package com.IGS.LG_Bangalore.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

   private static final String BASE_URL = "http://192.168.10.166/IGS/";
   // private static final String BASE_URL = "https://mmfinfotech.co/machine_test/api/";

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);



    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .writeTimeout(5,TimeUnit.SECONDS).build();

    private static final Gson gson = new GsonBuilder().setLenient().create();


    private static final Retrofit.Builder loginBuilder = new Retrofit.Builder().baseUrl(BASE_URL);

    public static <S> S userService(Class<S> serviceClass) {
        Retrofit retrofit = loginBuilder.client(client).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
        return retrofit.create(serviceClass);
    }


}
