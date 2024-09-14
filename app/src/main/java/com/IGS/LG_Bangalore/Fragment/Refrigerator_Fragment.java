package com.IGS.LG_Bangalore.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.IGS.LG_Bangalore.Database.DataBaseHelper;
import com.IGS.LG_Bangalore.R;
import com.IGS.LG_Bangalore.Response.RefrigeratorResponse;
import com.IGS.LG_Bangalore.Session.AppSession;
import com.IGS.LG_Bangalore.Session.Constants;
import com.IGS.LG_Bangalore.ViewModels.RefrigeratorViewModel;
import com.IGS.LG_Bangalore.databinding.FragmentRefrigeratorBinding;


public class Refrigerator_Fragment extends Fragment {


    FragmentRefrigeratorBinding binding;
    boolean isChecked = false;
    private boolean isPowerOn = false;
    private RefrigeratorViewModel refrigeratorViewModel;
    private DataBaseHelper db;
    private String deviceId;
    private String tmpFreezer = ""; // Initialize with example values
    private String tmpFridge = "";
    private String isExpressMode = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentRefrigeratorBinding.inflate(inflater, container, false);

        refrigeratorViewModel = new ViewModelProvider(this).get(RefrigeratorViewModel.class);
        refrigeratorViewModel.init(getContext());

        db = new DataBaseHelper(getContext());
        deviceId = db.getDeviceID("DEVICE_REFRIGERATOR");

        if (deviceId !=null && !deviceId.isEmpty()){
            fetchFreezeResponse();
        }else {
            Toast.makeText(getContext(), "DeviceId not found", Toast.LENGTH_SHORT).show();
        }

        onAttachObservers();
        initClicks();


        return binding.getRoot();
    }
    private void initClicks() {

        binding.cardArrowUp.setOnClickListener(view -> {
            adjustFridgeTemp("tmpFridgeInc");
            handleFridgeTemp();
            animateButton(binding.cardArrowUp);
        });

        binding.cardArrowDown.setOnClickListener(view -> {
            adjustFridgeTemp("tmpFridgeDec");
            handleFridgeTemp();
            animateButton(binding.cardArrowDown);
        });

        binding.cardArrowUp1.setOnClickListener(view -> {
            ; adjustFreezerTemp("tmpFreezerInc");
            handleFreezerTemp();
            animateButton(binding.cardArrowUp1);
        });
        binding.cardArrowDown1.setOnClickListener(view -> {
            adjustFreezerTemp("tmpFreezerDec");
            handleFreezerTemp();
            animateButton(binding.cardArrowDown1);
        });

    }

    private void fetchFreezeResponse() {
        showLoader(true);
        refrigeratorViewModel.getFreezeModelData(deviceId);
        refrigeratorViewModel.getSetFreezeModelData(deviceId,tmpFridge);
        refrigeratorViewModel.getSetFreezeModelData(deviceId,tmpFreezer);
//        refrigeratorViewModel.getSetFreezeModelData(deviceId,isExpressMode);
    }

    private void onAttachObservers() {

        refrigeratorViewModel.getLiveData().observe(getViewLifecycleOwner(),response -> {
            if (response != null && response.getResponse() != null){
                handleFreezeRes(response.getResponse());

            }else{
                Toast.makeText(getContext(), "Api call failed", Toast.LENGTH_SHORT).show();
            }
            showLoader(false);
        });
    }

    private void handleFreezeRes(RefrigeratorResponse.Response response) {
        if (response !=null && response.getTemperature() !=null){
            for (RefrigeratorResponse.Temperature temperature : response.getTemperature()){
                if (temperature !=null){
                    if ("FRIDGE".equalsIgnoreCase(temperature.getLocationName())) {
                        tmpFridge = String.valueOf(temperature.getTargetTemperature());
                        binding.txtFridge.setText(String.format("%s°C", tmpFridge));
                        AppSession.getInstance(getContext()).putString(Constants.FRIDGETEMP, tmpFridge);
                    }
                    else if ("FREEZER".equalsIgnoreCase(temperature.getLocationName())) {
                        tmpFreezer = String.valueOf(temperature.getTargetTemperature());
                        binding.txtFreezer.setText(String.format("%s°C", tmpFreezer));
                        AppSession.getInstance(getContext()).putString(Constants.FREEZERTEMP, tmpFreezer);
                    }
                }
            }

            String strExpressMode = String.valueOf(response.getRefrigeration().getExpressMode());
            boolean blnExpressMode = "TRUE".equalsIgnoreCase(strExpressMode);

            binding.cardOnoff.setCardBackgroundColor(blnExpressMode ? Color.GREEN : Color.RED);
            binding.imgDoor.setImageResource(blnExpressMode ? R.drawable.open_ref : R.drawable.door);
        }

    }

    private void handleFridgeTemp() {
        if (deviceId == null || deviceId.isEmpty()) {

            return;
        }
        refrigeratorViewModel.getSetFreezeModelData(deviceId, tmpFridge);
    }

    private void handleFreezerTemp() {
        if (deviceId == null || deviceId.isEmpty()) {

            return;
        }
        refrigeratorViewModel.getSetFreezeModelData(deviceId, tmpFreezer);
    }

    private void adjustFridgeTemp(String command) {
        if (deviceId == null || deviceId.isEmpty()) return;

        tmpFridge = command;
        //  binding.txtFridge.setText(String.format( tmpFridge));
        showLoader(true);
        handleFridgeTemp();

    }

    private void adjustFreezerTemp(String command) {
        if (deviceId == null || deviceId.isEmpty()) return;

        tmpFreezer = command;
        // binding.txtFreezer.setText(String.format("%s°C", tmpFreezer));
        showLoader(true);
        handleFreezerTemp();

    }


    private void togglePowerState() {
        if (deviceId == null || deviceId.isEmpty()) {

            return;
        }

        isPowerOn = !isPowerOn;
        isExpressMode = isPowerOn ? "FALSE" : "TRUE";

        // Debug log
        Log.d("RefrigeratorFragment", "Toggling power state. New express mode: " + isExpressMode);

        binding.cardOnoff.setCardBackgroundColor("TRUE".equalsIgnoreCase(isExpressMode) ? Color.GREEN : Color.RED);
        binding.imgDoor.setImageResource("TRUE".equalsIgnoreCase(isExpressMode) ? R.drawable.open_ref : R.drawable.door);

        refrigeratorViewModel.getSetFreezeModelData(deviceId, isExpressMode);

        if (isPowerOn) {
            fetchFreezeResponse();
        }
    }


    private void animateButton(View view) {
        if (!isChecked) {
            // Start animation for the clicked button
            animateScale(view);

            isChecked = true;

            // Reset the animation state after the duration of the animation
            view.postDelayed(() -> {
                isChecked = false;
            }, 1600); // 800ms for scale up + 800ms for scale down
        }
    }


    private void animateScale(View view) {
        // Scale up animation
        ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f);

        scaleXUp.setDuration(800);
        scaleYUp.setDuration(800);

        scaleXUp.start();
        scaleYUp.start();

        // Scale back to original size after 800ms
        scaleXUp.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1f);
                ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(view, "scaleY", 1.5f, 1f);

                scaleXBack.setDuration(800);
                scaleYBack.setDuration(800);

                scaleXBack.start();
                scaleYBack.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
    private void showLoader(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }


}