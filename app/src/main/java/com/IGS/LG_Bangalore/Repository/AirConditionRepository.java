package com.IGS.LG_Bangalore.Repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.IGS.LG_Bangalore.ClickListener.AirConditionListener;
import com.IGS.LG_Bangalore.Response.AirConditionResponse;
import com.IGS.LG_Bangalore.retrofit.Api;
import com.IGS.LG_Bangalore.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirConditionRepository {
    public static AirConditionRepository repository;

    private final MutableLiveData<AirConditionResponse> mutableLiveData = new MutableLiveData<>();

    // Singleton pattern
    public static AirConditionRepository getInstance() {
        if (repository == null) {
            repository = new AirConditionRepository();
        }
        return repository;
    }

    private final Api apiInterface;

    // Constructor
    public AirConditionRepository() {
        apiInterface = RetrofitService.userService(Api.class);
    }


    public MutableLiveData<AirConditionResponse> getAcLiveData(Context context, String id, AirConditionListener listener) {
        Call<AirConditionResponse> call = apiInterface.getACResponse(id);
        call.enqueue(new Callback<AirConditionResponse>() {
            @Override
            public void onResponse(Call<AirConditionResponse> call, Response<AirConditionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listener.onSuccess(response.body());
                    // Set value to LiveData
                }
            }

            @Override
            public void onFailure(Call<AirConditionResponse> call, Throwable t) {

                // listener.onError("Something went wrong: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<AirConditionResponse> getSetAcLiveData(Context context, String id,String id1, AirConditionListener listener) {
        Call<AirConditionResponse> call = apiInterface.getSetAcResponse(id,id1);
        call.enqueue(new Callback<AirConditionResponse>() {
            @Override
            public void onResponse(Call<AirConditionResponse> call, Response<AirConditionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listener.onSuccess(response.body());
                    // Set value to LiveData
                }
            }

            @Override
            public void onFailure(Call<AirConditionResponse> call, Throwable t) {

                // listener.onError("Something went wrong: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<AirConditionResponse> getSetAcLiveDataTemp(Context context, String id,String id1, AirConditionListener listener) {
        Call<AirConditionResponse> call = apiInterface.getSetAcTemp(id,id1);
        call.enqueue(new Callback<AirConditionResponse>() {
            @Override
            public void onResponse(Call<AirConditionResponse> call, Response<AirConditionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listener.onSuccess(response.body());
                    // Set value to LiveData
                }
            }

            @Override
            public void onFailure(Call<AirConditionResponse> call, Throwable t) {

                // listener.onError("Something went wrong: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }
}
