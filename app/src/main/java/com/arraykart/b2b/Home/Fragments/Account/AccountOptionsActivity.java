package com.arraykart.b2b.Home.Fragments.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Home.TechnicalName.TechnicalNameActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

public class AccountOptionsActivity extends AppCompatActivity {

    private TextView pageName;
    private ImageView back;
    private String fragmentName;
    private Fragment fragment;
    private static final String LANGUAGE = "language";
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_options);
        sharedPreferenceManager = new SharedPreferenceManager(this);


        checkToken();

        //checkLang();

        //set page name
        pageName = findViewById(R.id.pageName);
        setPageName();

        //switch fragment
        switchFragment();


        //back
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
//            int count = getSupportFragmentManager().getBackStackEntryCount();
//            if (count == 0) {
//                finish();
//            }else {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.popBackStack();
//            }
            finish();
        });
    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(AccountOptionsActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }
    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }

    private void switchFragment() {
        fragmentName = getIntent().getStringExtra("fragmentName").toLowerCase();
        switch(fragmentName){
            case "tutorial":
                fragment = new TutorialsFragment();
                break;
            case "profile":
                fragment = new MyProfileFragment();
                break;
            case "privacy":
                fragment = new PrivacyPolicyFragment();
                break;
            case "return":
                fragment = new ReturnPolicyFragment();
                break;
            case "terms":
                fragment = new TermsConditionsFragment();
                break;
            case "contact":
                fragment = new ContactFragment();
                break;
            case "bug":
                fragment = new BugFragment();
                break;
            case "kyc":
                if(sharedPreferenceManager.checkKey("kycstatus")){
                    if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("BP")){
                        fragment = new LicenseFragment();
                    }else {
                        fragment = new KYCFragment();
                    }
                }
                break;
            case "address":
                fragment = new AddressFragment();
                break;
            case "about":
                fragment = new AboutUsFragment();
                break;

            case "myorders":
                fragment = new MyOrdersFragment();
                break;

            default:
                break;


        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.addToBackStack("kycfrag");
        fragmentTransaction.replace(R.id.accountContainer, fragment).commit();
    }

    private void setPageName() {
        pageName.setText(getIntent().getStringExtra("pageName"));
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