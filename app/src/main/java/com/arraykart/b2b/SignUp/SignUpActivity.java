package com.arraykart.b2b.SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.Fragments.SignUpFragment;
import com.arraykart.b2b.SignUp.Fragments.SplashScreenFragment;
import com.arraykart.b2b.R;

public class SignUpActivity extends AppCompatActivity {
//    review
    private Fragment splashScreenFragment;
    private Fragment signUpFragment;

    //share preferences
    private SharedPreferenceManager sharedPreferenceManager;
    private FragmentTransaction fragmentTransactionSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPreferenceManager = new SharedPreferenceManager(this);
        if(!sharedPreferenceManager.checkKey("existingUser")){
            sharedPreferenceManager.setBoolean("existingUser", false);
        }
        fragmentTransactionSplash = this.getSupportFragmentManager().beginTransaction();
        splashScreenFragment = new SplashScreenFragment();
        fragmentTransactionSplash.replace(R.id.signUPFragContainer, splashScreenFragment).commit();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferenceManager = new SharedPreferenceManager(SignUpActivity.this);
                if(sharedPreferenceManager.checkKey("token") && sharedPreferenceManager.getString("token") != null){
                    finish();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(i);
                }else {
                    signUpFragment = new SignUpFragment();
                    FragmentTransaction fragmentTransactionSignUp = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSignUp.replace(R.id.signUPFragContainer, signUpFragment).commit();
                }

            }
        }, 2000);
    }
}