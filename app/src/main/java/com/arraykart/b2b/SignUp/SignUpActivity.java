package com.arraykart.b2b.SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.Fragments.LanguageFragment;
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

    private static final String LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPreferenceManager = new SharedPreferenceManager(this);

        checkLang();

        if(!sharedPreferenceManager.checkKey("existingUser")){
            sharedPreferenceManager.setBoolean("existingUser", false);
        }
        if(!sharedPreferenceManager.checkKey("firstInstall")){
            sharedPreferenceManager.setBoolean("firstInstall", true);
        }
        fragmentTransactionSplash = this.getSupportFragmentManager().beginTransaction();
        splashScreenFragment = new SplashScreenFragment();
        fragmentTransactionSplash.replace(R.id.signUPFragContainer, splashScreenFragment).commit();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (sharedPreferenceManager.getBoolean("firstInstall")) {
                Fragment fragment = new LanguageFragment();
                try {
                    FragmentManager fragmentTransactionSignUp = getSupportFragmentManager();
                    if(!fragmentTransactionSignUp.isDestroyed()) {
                        fragmentTransactionSignUp.beginTransaction()
                                .replace(R.id.signUPFragContainer, fragment).commit();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            else if (sharedPreferenceManager.checkKey("token")) {
                if(sharedPreferenceManager.getString("token") != null
                        && !sharedPreferenceManager.getString("token").isEmpty()) {
                    try {
                        finish();
                        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    signUpFragment = new SignUpFragment();
                    FragmentTransaction fragmentTransactionSignUp = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSignUp.replace(R.id.signUPFragContainer, signUpFragment).commit();
                }
            } else {
                signUpFragment = new SignUpFragment();
                FragmentTransaction fragmentTransactionSignUp = getSupportFragmentManager().beginTransaction();
                fragmentTransactionSignUp.replace(R.id.signUPFragContainer, signUpFragment).commit();
            }
        }, 3000);
    }
    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(SignUpActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }
}