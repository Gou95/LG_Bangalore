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
import com.IGS.LG_Bangalore.Response.AirConditionResponse;
import com.IGS.LG_Bangalore.Session.AppSession;
import com.IGS.LG_Bangalore.Session.Constants;
import com.IGS.LG_Bangalore.ViewModels.AirConditionViewModel;
import com.IGS.LG_Bangalore.databinding.FragmentAirConditionBinding;


public class Air_Condition_Fragment extends Fragment {


    FragmentAirConditionBinding binding;
    boolean isChecked = false;
    DataBaseHelper dataBaseHelper;
    boolean isPowerOn = false;
    String updateTemperature = "";
    String deviceIdAC,jobMode,airFlow;
    AirConditionViewModel airConditionViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentAirConditionBinding.inflate(inflater, container, false);


        airConditionViewModel = new ViewModelProvider(this).get(AirConditionViewModel.class);
        airConditionViewModel.init(getContext());

        dataBaseHelper = new DataBaseHelper(getContext());
        deviceIdAC = dataBaseHelper.getDeviceID("DEVICE_AIR_CONDITIONER");






        if (deviceIdAC != null && !deviceIdAC.isEmpty()){
            fetchAcResponse();
            jobMode = AppSession.getInstance(getContext()).getString(Constants.MODE);
            airFlow = AppSession.getInstance(getContext()).getString(Constants.AIRFLOW);
        }else {
            Toast.makeText(getContext(), "Device ID not found", Toast.LENGTH_SHORT).show();
        }


        initClicks();
        onAttachObservers();



        return binding.getRoot();
    }

    private void fetchAcResponse() {
        showLoader(true);
        airConditionViewModel.getAcModelData(deviceIdAC);
        airConditionViewModel.getSetAcModelData(deviceIdAC,jobMode);
        airConditionViewModel.getSetAcModelData(deviceIdAC,updateTemperature);
//        airConditionViewModel.getSetAcModelDataTemp(deviceIdAC,updateTemperature);
    }

    private void initClicks() {
        binding.linearCool.setOnClickListener(view -> {
            handleJobMode("COOL");


        });
        binding.linearDry.setOnClickListener(view -> {
            handleJobMode("AIR_DRY");


        });
        binding.linearFan.setOnClickListener(view -> {
            handleJobMode("FAN");


        });
        binding.linearLow.setOnClickListener(view -> {
            handleJobMode("LOW");
            handleAirMode("LOW");

        });
        binding.linearMedium.setOnClickListener(view -> {
            handleJobMode("MID");
            handleAirMode("MID");

        });
        binding.linearHigh.setOnClickListener(view -> {
            handleJobMode("HIGH");
            handleAirMode("HIGH");


        });
        binding.cardOnoff.setOnClickListener(view -> togglePowerState());
        binding.cardArrowUp.setOnClickListener(view -> {
            adjustTemperature("increase");
            animateButton(binding.cardArrowUp);

        });
        binding.cardArrowDown.setOnClickListener(view -> {
            adjustTemperature("decrease");
            animateButton(binding.cardArrowDown);
        });
    }

    private void onAttachObservers() {

        airConditionViewModel.getLiveData().observe(getViewLifecycleOwner(),response -> {
            if (response !=null && response.getResponse() !=null && "OK".equals(response.getStatus().getFlagMessage())){
                 handleAcResponse(response);
            }else {
                Toast.makeText(getContext(), "Invalid AC response", Toast.LENGTH_SHORT).show();
            }
            showLoader(false);
        });
    }






    private void handleAcResponse(AirConditionResponse response) {
        AirConditionResponse.Response res = response.getResponse();
        if (res != null && res.getTemperature() != null){

        updateTemperature = String.valueOf(res.getTemperature().getTargetTemperature());
            binding.txtTimer.setText(String.format("%s°C", updateTemperature));
            binding.txtCurrentTemp.setText(String.format("%s°C", res.getTemperature().getCurrentTemperature()));


            AppSession.getInstance(getContext()).putString(Constants.MODE, res.getAirConJobMode().getCurrentJobMode());
        AppSession.getInstance(getContext()).putString(Constants.AIRFLOW, res.getAirFlow().getWindStrength());

        isPowerOn = "POWER_ON".equals(res.getOperation().getAirConOperationMode());
        binding.cardOnoff.setCardBackgroundColor(isPowerOn? Color.GREEN : Color.RED);
        binding.txtOnoff.setText(isPowerOn? "ON":"OFF");

        String mode = res.getAirConJobMode().getCurrentJobMode();
        updateUILayoutBasedOnJobMode(mode);

        String windMode = res.getAirFlow().getWindStrength();
        updateUILayoutBasedOnAirMode(windMode);

        if (res.getAirQualitySensor() != null){
            updateAirQualityData(
                    res.getAirQualitySensor().getPm1(),
                    res.getAirQualitySensor().getPm2(),
                    res.getAirQualitySensor().getPm10(),
                    res.getAirQualitySensor().getTotalPollution()
            );

        }
        else {
            setDefaultAirQualityData();
        }
        updateUILayoutBasedOnJobMode(AppSession.getInstance(getContext()).getString(Constants.MODE));
        updateUILayoutBasedOnAirMode(AppSession.getInstance(getContext()).getString(Constants.AIRFLOW));
    }
    }

    private void handleJobMode(String newMode) {
        if (deviceIdAC == null || deviceIdAC.isEmpty()) {
            Toast.makeText(getContext(), "Device ID not found", Toast.LENGTH_SHORT).show();
            return;
        }
        jobMode = newMode;
        fetchAcResponse();
    }

    private void handleAirMode(String newAirMode) {
        if (deviceIdAC == null || deviceIdAC.isEmpty()) {
            Toast.makeText(getContext(), "Device ID not found", Toast.LENGTH_SHORT).show();
            return;
        }
        airFlow = newAirMode;
        fetchAcResponse();
    }



    private void updateUILayoutBasedOnJobMode(String jobMode) {

        if ("COOL".equals(jobMode)) {
            binding.linearCool1.setVisibility(View.VISIBLE);
            binding.linearFan1.setVisibility(View.GONE);
            binding.linearDry1.setVisibility(View.GONE);


        } else if ("AIR_DRY".equals(jobMode)) {
            binding.linearCool1.setVisibility(View.GONE);
            binding.linearFan1.setVisibility(View.GONE);
            binding.linearDry1.setVisibility(View.VISIBLE);


        } else if ("FAN".equals(jobMode)) {
            binding.linearCool1.setVisibility(View.GONE);
            binding.linearFan1.setVisibility(View.VISIBLE);
            binding.linearDry1.setVisibility(View.GONE);
        }
        else if ("LOW".equals(jobMode)) {

        } else if ("MID".equals(jobMode)) {


        } else if ("HIGH".equals(jobMode)) {


        }

    }
    private void updateUILayoutBasedOnAirMode(String windStrength) {

        if ("LOW".equals(windStrength)){
            binding.linearLow1.setVisibility(View.VISIBLE);
            binding.linearMedium1.setVisibility(View.GONE);
            binding.linearHigh1.setVisibility(View.GONE);
        }
        else if ("MID".equals(windStrength)) {
            binding.linearLow1.setVisibility(View.GONE);
            binding.linearMedium1.setVisibility(View.VISIBLE);
            binding.linearHigh1.setVisibility(View.GONE);
        }else if ("HIGH".equals(windStrength)) {
            binding.linearLow1.setVisibility(View.GONE);
            binding.linearMedium1.setVisibility(View.GONE);
            binding.linearHigh1.setVisibility(View.VISIBLE);
        }
    }

    private void togglePowerState() {
        if (deviceIdAC != null && !deviceIdAC.isEmpty()) {
            String mode = isPowerOn ? "POWER_OFF" : "POWER_ON";
            showLoader(true);
            airConditionViewModel.getSetAcModelData(deviceIdAC, mode);
            isPowerOn = !isPowerOn;
            binding.cardOnoff.setCardBackgroundColor(isPowerOn ? Color.GREEN : Color.RED);
            binding.txtOnoff.setText(isPowerOn ? "ON" : "OFF");
        } else {
            Toast.makeText(getContext(), "Device ID not found", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateAirQualityData(int pm1, int pm2, int pm10, int totalPollution) {
        binding.txtPm1.setText(String.valueOf(pm1));
        binding.txtPm2.setText(String.valueOf(pm2));
        binding.txtPm10.setText(String.valueOf(pm10));
        binding.txtTotalPopulation.setText(String.valueOf(totalPollution));
    }

    private void setDefaultAirQualityData() {
        binding.txtPm1.setText("N/A");
        binding.txtPm2.setText("N/A");
        binding.txtPm10.setText("N/A");
        binding.txtTotalPopulation.setText("N/A");
    }

    private void adjustTemperature(String increase) {
        if (deviceIdAC != null && !deviceIdAC.isEmpty()) {
            updateTemperature = increase;
            showLoader(true);
            airConditionViewModel.getSetAcModelData(     deviceIdAC, updateTemperature);
            Log.d("temp", "adjustTemperature: "+updateTemperature);
        } else {
            Toast.makeText(getContext(), "Device ID not found", Toast.LENGTH_SHORT).show();
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