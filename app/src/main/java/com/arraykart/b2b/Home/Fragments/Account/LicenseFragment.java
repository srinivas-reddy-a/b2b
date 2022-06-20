package com.arraykart.b2b.Home.Fragments.Account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.R;


public class LicenseFragment extends Fragment {

    private CheckBox pestCb;
    private CheckBox fertCb;
    private CheckBox seedCb;
    private Bundle bundle;
    private TextView continueTV;
    private Boolean Pchecked = false;
    private Boolean Fchecked = false;
    private Boolean Schecked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_license, container, false);

        checkToken();

        pestCb = view.findViewById(R.id.pestCb);
        fertCb = view.findViewById(R.id.fertCb);
        seedCb = view.findViewById(R.id.seedCb);
        continueTV = view.findViewById(R.id.continueTV);

        bundle = new Bundle();
        bundle.putString("pest", "false");
        bundle.putString("fert", "false");
        bundle.putString("seed", "false");
        handleCheckbox();
        continueToUpload();

        return view;
    }

    private void continueToUpload() {
        continueTV.setOnClickListener(v -> {
            if(isAdded()){
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                Fragment fragment = new LicenseUploadFragment();
                fragment.setArguments(bundle);
                transaction.addToBackStack("license");
                transaction.replace(R.id.accountContainer, fragment).commit();
            }
        });
    }

    private void handleCheckbox() {
        pestCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                bundle.putString("pest", "true");
                continueTV.setEnabled(true);
                continueTV.setBackgroundColor(getResources().getColor(R.color.green));
                Pchecked = true;
            }else {
                Pchecked=false;
                if(!Pchecked && !Fchecked && !Schecked) {
                    continueTV.setEnabled(false);
                    continueTV.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                bundle.putString("pest", "false");
            }
        });
        fertCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                bundle.putString("fert", "true");
                continueTV.setEnabled(true);
                continueTV.setBackgroundColor(getResources().getColor(R.color.green));
                Fchecked = true;
            }else {
                Fchecked=false;
                if(!Pchecked && !Fchecked && !Schecked) {
                    continueTV.setEnabled(false);
                    continueTV.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                bundle.putString("fert", "false");
            }
        });
        seedCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                bundle.putString("seed", "true");
                continueTV.setEnabled(true);
                continueTV.setBackgroundColor(getResources().getColor(R.color.green));
                Schecked = true;
            }else {
                Schecked=false;
                if(!Pchecked && !Fchecked && !Schecked) {
                    continueTV.setEnabled(false);
                    continueTV.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                bundle.putString("seed", "false");
            }
        });
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }
}