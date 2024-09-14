package com.IGS.LG_Bangalore.ClickListener;

import com.IGS.LG_Bangalore.Response.AirConditionResponse;

public interface AirConditionListener {

    void onSuccess(AirConditionResponse response);
    void onError (String error);
}
