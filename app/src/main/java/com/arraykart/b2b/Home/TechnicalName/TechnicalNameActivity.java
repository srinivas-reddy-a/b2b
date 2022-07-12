package com.arraykart.b2b.Home.TechnicalName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

public class TechnicalNameActivity extends AppCompatActivity {
    private Fragment fragment;
    private ImageView backTN;
    private static final String LANGUAGE = "language";
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_name);
        sharedPreferenceManager = new SharedPreferenceManager(TechnicalNameActivity.this);

        checkToken();

        //checkLang();

        try{
            FragmentManager manager = getSupportFragmentManager();
            fragment = new TechnicalListingFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.addToBackStack("techList");
            //add tag here instead of adding in xml
            fragmentTransaction.replace(R.id.techNameContainer, fragment, "techList").commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(TechnicalNameActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }

    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            finish();
            //additional code
        }
    }
}