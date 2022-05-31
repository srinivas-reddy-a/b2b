package com.arraykart.b2b.Home.Fragments.Account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.User;
import com.arraykart.b2b.Retrofit.ModelClass.UserProfile;
import com.arraykart.b2b.Retrofit.ModelClass.UserProfileUpdate;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

public class MyProfileFragment extends Fragment {
    private TextView nameLabel;
    private TextView numberLabel;
    private TextView emailLabel;
    private EditText name;
    private EditText number;
    private EditText email;
    private NeumorphCardView submit;
    private List<User> user;
    //to set user value in shared preferences
    private static String username;

    private String nameET;
    private String emailET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        nameLabel = view.findViewById(R.id.nameLabel);
        numberLabel = view.findViewById(R.id.phoneNumberLabel);
        emailLabel = view.findViewById(R.id.emailLabel);
        name = view.findViewById(R.id.name);
        number = view.findViewById(R.id.number);
        email = view.findViewById(R.id.email);
        submit = view.findViewById(R.id.submitNCV);

        //setup profile page
        setProfile();

        return view;
    }

    private void setProfile() {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        String token = sharedPreferenceManager.getString("token");
        Call<UserProfile> call = RetrofitClient.getClient().getApi()
                .getUserProfile(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if(!response.isSuccessful()){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                user = response.body().getUser();
//                //Log.e("size", String.valueOf(user.size()));
                String getName = user.get(0).getName();
                if(getName!=null
                && !getName.isEmpty()){
                    name.setText(getName);
                }
                String getNumber = String.valueOf(user.get(0).getPhoneNumber());
                if(getNumber!=null
                && !getNumber.isEmpty()){
                    number.setText(getNumber);
                }
                String getEmail = user.get(0).getEmail();
                if(getEmail!=null
                && !getEmail.isEmpty()){
                    email.setText(getEmail);
                }
                //edit profile page
                editProfile(token);
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editProfile(String token) {
        if(!name.getText().toString().isEmpty()){
            nameLabel.setVisibility(View.VISIBLE);
        }else {
            nameLabel.setVisibility(View.INVISIBLE);
        }
        if(!email.getText().toString().isEmpty()){
            emailLabel.setVisibility(View.VISIBLE);
        }else {
            emailLabel.setVisibility(View.INVISIBLE);
        }

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nameLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameET = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    nameLabel.setVisibility(View.INVISIBLE);
                }else{
                    nameLabel.setVisibility(View.VISIBLE);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailET = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    nameLabel.setVisibility(View.INVISIBLE);
                }else{
                    nameLabel.setVisibility(View.VISIBLE);
                }
            }
        });
        submit.setOnClickListener(v -> {
            if(name.getText().toString().isEmpty()
            || name.getText().toString()==null
            || email.getText().toString().isEmpty()
            || email.getText().toString() == null){
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "Please fill the fields!", Toast.LENGTH_SHORT).show();
                }
            }else{
                username =  name.getText().toString();
                User user = new User(Long.parseLong(String.valueOf(number.getText())), nameET, emailET);
                Call<UserProfileUpdate> call1 = RetrofitClient.getClient().getApi()
                        .setUserProfile(token, user);
                call1.enqueue(new Callback<UserProfileUpdate>() {
                    @Override
                    public void onResponse(@NonNull Call<UserProfileUpdate> call, @NonNull Response<UserProfileUpdate> response) {
                        if(!response.isSuccessful()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        assert response.body() != null;
                        if(!response.body().getSuccess()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if(isAdded()) {
                            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                            sharedPreferenceManager.setString("user", username);
                            Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserProfileUpdate> call, @NonNull Throwable t) {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            
        });
    }
}