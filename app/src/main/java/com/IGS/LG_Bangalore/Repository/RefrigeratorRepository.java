package com.IGS.LG_Bangalore.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.IGS.LG_Bangalore.ClickListener.RefrigeratorListener;
import com.IGS.LG_Bangalore.Response.RefrigeratorResponse;
import com.IGS.LG_Bangalore.retrofit.Api;
import com.IGS.LG_Bangalore.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefrigeratorRepository {
    public static RefrigeratorRepository repository;

    private final MutableLiveData<RefrigeratorResponse> mutableLiveData = new MutableLiveData<>();

    // Singleton pattern
    public static RefrigeratorRepository getInstance() {
        if (repository == null) {
            repository = new RefrigeratorRepository();
        }
        return repository;
    }

    private final Api apiInterface;

    // Constructor
    public RefrigeratorRepository() {
        apiInterface = RetrofitService.userService(Api.class);
    }

    // Method to fetch AC data
    public MutableLiveData<RefrigeratorResponse> getRefrigeratorLiveData(Context context, String id, RefrigeratorListener listener) {
        Call<RefrigeratorResponse> call = apiInterface.getFreezeResponse(id);
        call.enqueue(new Callback<RefrigeratorResponse>() {
            @Override
            public void onResponse(Call<RefrigeratorResponse> call, Response<RefrigeratorResponse> responseResponse) {
                if (responseResponse.isSuccessful() && responseResponse.body() != null) {
                    Log.d("ACRepository", "onResponse: " + responseResponse.body());
                    listener.onSuccess(responseResponse.body());
                    // Set value to LiveData
                }
            }

            @Override
            public void onFailure(Call<RefrigeratorResponse> call, Throwable t) {
                Log.e("ACRepository", "Failure: " + t.getMessage());
                //   listener.onError("Something went wrong: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<RefrigeratorResponse> getSetRefrigeratorLiveData(Context context, String id,String id1, RefrigeratorListener listener) {
        Call<RefrigeratorResponse> call = apiInterface.getSetFreezeResponse(id,id1);
        call.enqueue(new Callback<RefrigeratorResponse>() {
            @Override
            public void onResponse(Call<RefrigeratorResponse> call, Response<RefrigeratorResponse> responseResponse) {
                if (responseResponse.isSuccessful() && responseResponse.body() != null) {
                    Log.d("ACRepository", "onResponse: " + responseResponse.body());
                    listener.onSuccess(responseResponse.body());
                    // Set value to LiveData
                }
            }

            @Override
            public void onFailure(Call<RefrigeratorResponse> call, Throwable t) {
                Log.e("ACRepository", "Failure: " + t.getMessage());
                //   listener.onError("Something went wrong: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }
}
