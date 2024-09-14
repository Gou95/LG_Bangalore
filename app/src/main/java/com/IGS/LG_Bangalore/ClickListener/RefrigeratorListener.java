package com.IGS.LG_Bangalore.ClickListener;

import com.IGS.LG_Bangalore.Response.RefrigeratorResponse;

public interface RefrigeratorListener {
    void onSuccess(RefrigeratorResponse response);
    void onError (String error);
}
