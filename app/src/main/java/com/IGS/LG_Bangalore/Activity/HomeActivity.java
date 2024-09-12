package com.IGS.LG_Bangalore.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.IGS.LG_Bangalore.Fragment.Air_Condition_Fragment;
import com.IGS.LG_Bangalore.Fragment.Air_Purifier_Fragment;
import com.IGS.LG_Bangalore.Fragment.Blind_Fragment;
import com.IGS.LG_Bangalore.Fragment.Curtain_Fragment;
import com.IGS.LG_Bangalore.Fragment.Dish_Washer_Fragment;
import com.IGS.LG_Bangalore.Fragment.Kitchen_AC_Fragment;
import com.IGS.LG_Bangalore.Fragment.LivingRoom_AC_Fragment;
import com.IGS.LG_Bangalore.Fragment.Refrigerator_Fragment;
import com.IGS.LG_Bangalore.R;
import com.IGS.LG_Bangalore.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.linearBedroom.setOnClickListener(view -> setVisibleList(binding.llBedroomList));
        binding.linearLivingroom.setOnClickListener(view -> setVisibleList(binding.llLivingList));
        binding.linearKitchen.setOnClickListener(view -> setVisibleList(binding.llKitchenList));

        binding.linearAir.setOnClickListener(view -> replaceFragment(new Air_Condition_Fragment()));
        binding.linearBlind.setOnClickListener(view -> replaceFragment(new Blind_Fragment()));

        binding.linearLivingCurtain.setOnClickListener(view -> replaceFragment(new Curtain_Fragment()));
        binding.linearLivingAirPurifier.setOnClickListener(view -> replaceFragment(new Air_Purifier_Fragment()));
        binding.linearLivingAc.setOnClickListener(view -> replaceFragment(new LivingRoom_AC_Fragment()));

        binding.linearKitchenDishWasher.setOnClickListener(view -> replaceFragment(new Dish_Washer_Fragment()));
        binding.linearKitchenRefrigerator.setOnClickListener(view -> replaceFragment(new Refrigerator_Fragment()));
        binding.linearKitchenAc.setOnClickListener(view -> replaceFragment(new Kitchen_AC_Fragment()));



    }

    private void setVisibleList(View visibleList) {
        binding.llBedroomList.setVisibility(visibleList == binding.llBedroomList ? View.VISIBLE : View.GONE);
        binding.llLivingList.setVisibility(visibleList == binding.llLivingList ? View.VISIBLE : View.GONE);
        binding.llKitchenList.setVisibility(visibleList == binding.llKitchenList ? View.VISIBLE : View.GONE);
    }

    private void replaceFragment(Fragment fragment) {
        if (!isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}