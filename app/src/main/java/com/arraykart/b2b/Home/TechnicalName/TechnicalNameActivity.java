package com.arraykart.b2b.Home.TechnicalName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;

import com.arraykart.b2b.R;

public class TechnicalNameActivity extends AppCompatActivity {
    private Fragment fragment;
    private ImageView backTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_name);

        //back
        backTN = findViewById(R.id.backTN);
        backTN.setOnClickListener(v -> finish());

        try{
            fragment = new TechnicalListingFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.techNameContainer, fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}