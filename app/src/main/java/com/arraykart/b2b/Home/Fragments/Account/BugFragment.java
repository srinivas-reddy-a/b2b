package com.arraykart.b2b.Home.Fragments.Account;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.BugReport;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.Search.SearchActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;


public class BugFragment extends Fragment {

    private EditText bugET;
    private ConstraintLayout reportBug;
    private String bugDetail;
    private SharedPreferenceManager sharedPreferenceManager;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bug, container, false);

        //report bug
        setBug(view);
        return view;
    }

    private void setBug(View view) {
        bugET =view.findViewById(R.id.bugET);
        reportBug = view.findViewById(R.id.reportBug);
        reportBug.setEnabled(false);
        bugET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 9) {
                    bugDetail = s.toString();
                    reportBug.setBackgroundColor(view.getResources().getColor(R.color.green));
                    reportBug.setEnabled(true);
                }else {
//                    if(isAdded()) {
//                    Toast.makeText(requireActivity(), "Enter atleast 10 characters!", Toast.LENGTH_SHORT).show();
//                    }
                    reportBug.setBackgroundColor(view.getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reportBug.setOnClickListener(v -> {
                sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                if(sharedPreferenceManager.checkKey("token")){
                    token = sharedPreferenceManager.getString("token");
                }
                BugReport bugReport = new BugReport(bugDetail);
                Call<SuccessMessage> call = RetrofitClient.getClient()
                        .getApi().setReportBug(token, bugReport);
                call.enqueue(new Callback<SuccessMessage>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessMessage> call, @NonNull Response<SuccessMessage> response) {
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
                                return;
                            }
                        }
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessMessage> call, @NonNull Throwable t) {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        });
    }
}