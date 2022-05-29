package com.arraykart.b2b.Home.Fragments.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.arraykart.b2b.R;

public class AccountOptionsActivity extends AppCompatActivity {

    private TextView pageName;
    private ImageView back;
    private String fragmentName;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_options);



        //set page name
        pageName = findViewById(R.id.pageName);
        setPageName();

        //switch fragment
        switchFragment();


        //back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void switchFragment() {
        fragmentName = getIntent().getStringExtra("fragmentName").toLowerCase();
        switch(fragmentName){
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
                fragment = new KYCFragment();
                break;
            case "address":
                fragment = new AddressFragment();
                break;
            default:
                break;


        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.accountContainer, fragment).commit();
    }

    private void setPageName() {
        pageName.setText(getIntent().getStringExtra("pageName"));
    }
}