package com.arraykart.b2b.Home.Fragments.Account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Address;
import com.arraykart.b2b.Retrofit.ModelClass.UserAddress;
import com.arraykart.b2b.Retrofit.ModelClass.UserProfile;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;


public class AddressFragment extends Fragment {

    private TextView shopNameLabel;
    private EditText shopName;
    private TextView phoneNumberLabel;
    private EditText phoneNumber;
    private TextView addressLabel;
    private EditText address;
    private TextView cityLabel;
    private EditText city;
    private TextView pincodeLabel;
    private EditText pincode;
    private Spinner stateSpinner;
    private NeumorphCardView submitNCV;
    private List<Address> addressList;
    private ArrayAdapter<String> adapter;
    private String[] states = {
            "Andaman and Nicobar Islands",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Lakshadweep",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Puducherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal"};

    //to get text from edittextview
    private String name;
    private String number;
    private String adrs;
    private String place;
    private String zipcode;
    private String state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        shopNameLabel = view.findViewById(R.id.shopNameLabel);
        shopName = view.findViewById(R.id.shopName);
        phoneNumberLabel = view.findViewById(R.id.phoneNumberLabel);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        addressLabel = view.findViewById(R.id.addressLabel);
        address = view.findViewById(R.id.address);
        cityLabel = view.findViewById(R.id.cityLabel);
        city = view.findViewById(R.id.city);
        pincodeLabel = view.findViewById(R.id.pincodeLabel);
        pincode = view.findViewById(R.id.pincode);
        stateSpinner = view.findViewById(R.id.stateSpinner);
        submitNCV = view.findViewById(R.id.submitNCV);
        if(isAdded()) {
            adapter = new ArrayAdapter(requireActivity(), R.layout.spineer_state_text_view_single_item, R.id.spinnerText, states);
            stateSpinner.setAdapter(adapter);
            stateSpinner.setSelection(27);
        }
        //set address
        setAddress();

        return view;
    }

    private void addAddress() {
        if(!shopName.getText().toString().isEmpty()){
            shopNameLabel.setVisibility(View.VISIBLE);
        }else{
            shopNameLabel.setVisibility(View.INVISIBLE);
        }
        if(!phoneNumber.getText().toString().isEmpty()){
            phoneNumberLabel.setVisibility(View.VISIBLE);
        }else{
            phoneNumberLabel.setVisibility(View.INVISIBLE);
        }
        if(!address.getText().toString().isEmpty()){
            addressLabel.setVisibility(View.VISIBLE);
        }else{
            addressLabel.setVisibility(View.INVISIBLE);
        }
        if(!city.getText().toString().isEmpty()){
            cityLabel.setVisibility(View.VISIBLE);
        }else{
            cityLabel.setVisibility(View.INVISIBLE);
        }
        if(!pincode.getText().toString().isEmpty()){
            pincodeLabel.setVisibility(View.VISIBLE);
        }else{
            pincodeLabel.setVisibility(View.INVISIBLE);
        }
        shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                shopNameLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    shopNameLabel.setVisibility(View.INVISIBLE);
                }else{
                    shopNameLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                phoneNumberLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                number = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    phoneNumberLabel.setVisibility(View.INVISIBLE);
                }else{
                    phoneNumberLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                addressLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adrs = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    addressLabel.setVisibility(View.INVISIBLE);
                }else{
                    addressLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cityLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                place = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    cityLabel.setVisibility(View.INVISIBLE);
                }else{
                    cityLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pincodeLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                zipcode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    pincodeLabel.setVisibility(View.INVISIBLE);
                }else{
                    pincodeLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = states[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitNCV.setOnClickListener(v -> {
            if(shopName.getText().toString().isEmpty()
                    || shopName.getText().toString()==null
                    || address.getText().toString().isEmpty()
                    || address.getText().toString()==null
                    || phoneNumber.getText().toString().isEmpty()
                    || phoneNumber.getText().toString()==null
                    || pincode.getText().toString().isEmpty()
                    || pincode.getText().toString()==null
                    || city.getText().toString().isEmpty()
                    || city.getText().toString()==null
            ){
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Address addressObject = new Address(
                        name,
                        adrs,
                        Integer.parseInt(zipcode),
                        Long.parseLong(number),
                        place,
                        state
                );

                SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                String token = sharedPreferenceManager.getString("token");
                Call<UserAddress> call1 = RetrofitClient
                        .getClient().getApi().setUserAddress(token, addressObject);
                call1.enqueue(new Callback<UserAddress>() {
                    @Override
                    public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                        if (!response.isSuccessful()) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        assert response.body() != null;
                        if (!response.body().getSuccess()) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Added Address!", Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAddress> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void setAddress() {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        String token = sharedPreferenceManager.getString("token");
        Call<UserAddress> call = RetrofitClient.getClient().getApi()
                .getUserAddress(token);
        call.enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                if(!response.isSuccessful()){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                addressList = response.body().getAddress();
                if(!addressList.isEmpty()) {
                    if (!addressList.get(0).getUserId().toString().isEmpty()
                            || addressList.get(0).getUserId().toString() != null
                    ) {
                        shopName.setText(addressList.get(0).getAddressName());
                        name = addressList.get(0).getAddressName();
                        phoneNumber.setText(addressList.get(0).getPhoneNumber().toString());
                        number = addressList.get(0).getPhoneNumber().toString();
                        address.setText(addressList.get(0).getAddressLine1());
                        adrs = addressList.get(0).getAddressLine1();
                        city.setText(addressList.get(0).getCity());
                        place = addressList.get(0).getCity();
                        pincode.setText(addressList.get(0).getPostalCode().toString());
                        zipcode = addressList.get(0).getPostalCode().toString();
                        stateSpinner.setSelection(Arrays
                                .asList(states)
                                .indexOf(addressList.get(0).getState()));
                        state = addressList.get(0).getState();
                    }
                }

                //edit address
                if(!addressList.isEmpty()) {
                    if(!addressList.get(0).getUserId().toString().isEmpty()
                            || addressList.get(0).getUserId().toString()!=null) {
                        editAddress();
                }
                }else {
                    addAddress();
                }
            }

            @Override
            public void onFailure(Call<UserAddress> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editAddress() {
        if(!shopName.getText().toString().isEmpty()){
            shopNameLabel.setVisibility(View.VISIBLE);
        }else{
            shopNameLabel.setVisibility(View.INVISIBLE);
        }
        if(!phoneNumber.getText().toString().isEmpty()){
            phoneNumberLabel.setVisibility(View.VISIBLE);
        }else{
            phoneNumberLabel.setVisibility(View.INVISIBLE);
        }
        if(!address.getText().toString().isEmpty()){
            addressLabel.setVisibility(View.VISIBLE);
        }else{
            addressLabel.setVisibility(View.INVISIBLE);
        }
        if(!city.getText().toString().isEmpty()){
            cityLabel.setVisibility(View.VISIBLE);
        }else{
            cityLabel.setVisibility(View.INVISIBLE);
        }
        if(!pincode.getText().toString().isEmpty()){
            pincodeLabel.setVisibility(View.VISIBLE);
        }else{
            pincodeLabel.setVisibility(View.INVISIBLE);
        }
        shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                shopNameLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    shopNameLabel.setVisibility(View.INVISIBLE);
                }else{
                    shopNameLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                phoneNumberLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                number = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    phoneNumberLabel.setVisibility(View.INVISIBLE);
                }else{
                    phoneNumberLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                addressLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adrs = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    addressLabel.setVisibility(View.INVISIBLE);
                }else{
                    addressLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cityLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                place = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    cityLabel.setVisibility(View.INVISIBLE);
                }else{
                    cityLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pincodeLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                zipcode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    pincodeLabel.setVisibility(View.INVISIBLE);
                }else{
                    pincodeLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = states[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submitNCV.setOnClickListener(v -> {
            Address address = new Address(
                    name,
                    adrs,
                    Integer.parseInt(zipcode),
                    Long.parseLong(number),
                    place,
                    state
            );
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            String token = sharedPreferenceManager.getString("token");
            Call<UserAddress> call1 = RetrofitClient
                    .getClient().getApi().putUserAddress(token, address);
            call1.enqueue(new Callback<UserAddress>() {
                @Override
                public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    assert response.body() != null;
                    if(!response.body().getSuccess()){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "Added Address!", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<UserAddress> call, Throwable t) {

                }
            });

        });
    }
}