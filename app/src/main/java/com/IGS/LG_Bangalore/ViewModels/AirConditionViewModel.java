package com.IGS.LG_Bangalore.ViewModels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.IGS.LG_Bangalore.ClickListener.AirConditionListener;
import com.IGS.LG_Bangalore.Repository.AirConditionRepository;
import com.IGS.LG_Bangalore.Response.AirConditionResponse;

public class AirConditionViewModel extends ViewModel {

    private Context context;

    private MutableLiveData<String> isFailed = new MutableLiveData<>();

    private MutableLiveData<Boolean> isConnecting = new MutableLiveData<>();

    private MutableLiveData<AirConditionResponse> responseMutableLiveData;


    private AirConditionRepository repository;

    public LiveData<String> getIsFailed(){
        return isFailed;
    }

    public LiveData<Boolean>getIsConnecting(){
        return isConnecting;


    }

    public LiveData<AirConditionResponse>getLiveData(){
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
        repository = AirConditionRepository.getInstance();
    }
    AirConditionListener listener = new AirConditionListener() {
        @Override
        public void onSuccess(AirConditionResponse response) {
            responseMutableLiveData.setValue(response);

        }

        @Override
        public void onError(String error) {
            isFailed.setValue(error);
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }

    };


    public void getAcModelData(String id){
        isConnecting.setValue(true);
        repository = AirConditionRepository.getInstance();
        repository.getAcLiveData(context,id,listener);
    }

    public void getSetAcModelData(String id,String id1){
        isConnecting.setValue(true);
        repository = AirConditionRepository.getInstance();
        repository.getSetAcLiveData(context,id,id1,listener);
    }

    public void getSetAcModelDataTemp(String id,String id1){
        isConnecting.setValue(true);
        repository = AirConditionRepository.getInstance();
        repository.getSetAcLiveDataTemp(context,id,id1,listener);
    }

}
