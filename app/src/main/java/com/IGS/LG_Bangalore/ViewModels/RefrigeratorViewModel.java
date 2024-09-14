package com.IGS.LG_Bangalore.ViewModels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.IGS.LG_Bangalore.ClickListener.RefrigeratorListener;
import com.IGS.LG_Bangalore.Repository.RefrigeratorRepository;
import com.IGS.LG_Bangalore.Response.RefrigeratorResponse;

public class RefrigeratorViewModel extends ViewModel {
    private Context context;

    private MutableLiveData<String> isFailed = new MutableLiveData<>();

    private MutableLiveData<Boolean> isConnecting = new MutableLiveData<>();

    private MutableLiveData<RefrigeratorResponse> responseMutableLiveData;


    private RefrigeratorRepository repository;

    public LiveData<String> getIsFailed(){
        return isFailed;
    }

    public LiveData<Boolean>getIsConnecting(){
        return isConnecting;


    }

    public LiveData<RefrigeratorResponse>getLiveData(){
        if (responseMutableLiveData == null){
            responseMutableLiveData = new MutableLiveData<>();
        }

        return responseMutableLiveData;
    }
    public void init(Context context){
        this.context = context;
        if (responseMutableLiveData == null){
            return;
        }
        repository = RefrigeratorRepository.getInstance();
    }
    RefrigeratorListener listener = new RefrigeratorListener() {
        @Override
        public void onSuccess(RefrigeratorResponse refrigeratorResponse) {
            responseMutableLiveData.setValue(refrigeratorResponse);

        }

        @Override
        public void onError(String error) {
            isFailed.setValue(error);
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }

    };


    public void getFreezeModelData(String id){
        isConnecting.setValue(true);
        repository = RefrigeratorRepository.getInstance();
        repository.getRefrigeratorLiveData(context,id,listener);

    }

    public void getSetFreezeModelData(String id,String id1){
        isConnecting.setValue(true);
        repository = RefrigeratorRepository.getInstance();
        repository.getSetRefrigeratorLiveData(context,id,id1,listener);

    }

}
