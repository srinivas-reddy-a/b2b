package com.arraykart.b2b.Home.TechnicalName;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.AllTechNames;
import com.arraykart.b2b.Retrofit.ModelClass.Techname;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.Search.SearchActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TechnicalListingFragment extends Fragment {
    private RecyclerView techNameRV;
    private LinearLayoutManager linearLayoutManager;
    private List<Techname> technames;
    private Set<String> techNameSet;
    private List<String> filteredTechNames;
    private TechNameRecyclerAdapter techNameRecyclerAdapter;
    private ImageView backTN;
    private LoadingDialog loadingDialog;
    private SharedPreferenceManager sharedPreferenceManager;
    private ImageView searchTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_technical_listing, container, false);
        if(isAdded()){
            loadingDialog = new LoadingDialog(requireActivity());
            loadingDialog.startLoadingDialog();
        }
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }
        checkToken();

//        back
        back(view);

//        get technical names to rv
        getTechNamesToRV(view);

        //search activity
        searchTN = view.findViewById(R.id.searchTN);
        setSearch();

        return view;
    }

    private void setSearch() {
        searchTN.setOnClickListener(v -> {
            if(isAdded()){
                Intent i = new Intent(requireActivity(), SearchActivity.class);
                startActivity(i);
            }
        });
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }

    private void back(View view) {
        backTN = view.findViewById(R.id.backTN);
        backTN.setOnClickListener(v -> {
            if(isAdded()){
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
                requireActivity().finish();
            }
        });
    }

    private void getTechNamesToRV(View view) {
        techNameRV = view.findViewById(R.id.techNameRV);
        if(isAdded()){
            if(sharedPreferenceManager.checkKey("token")){
                linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
                techNameRV.setLayoutManager(linearLayoutManager);
                Call<AllTechNames> call = RetrofitClient.getClient()
                        .getApi().getTechNames(sharedPreferenceManager.getString("token"));
                if(isAdded()) {

                call.enqueue(new Callback<AllTechNames>() {
                    @Override
                    public void onResponse(@NonNull Call<AllTechNames> call, @NonNull Response<AllTechNames> response) {
                        loadingDialog.dismissLoadingDialog();
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
                        technames = response.body().getTechnames();
                        techNameSet = new LinkedHashSet<>();
                        for (int i=0;i<technames.size();i++){
                            techNameSet.add(technames.get(i).getTechnicalName());
                        }
                        filteredTechNames = new ArrayList<>();
                        filteredTechNames.addAll(techNameSet);
                        if(isAdded()) {
                            techNameRecyclerAdapter = new TechNameRecyclerAdapter(filteredTechNames, requireActivity());
                            techNameRV.setAdapter(techNameRecyclerAdapter);
                            techNameRV.setHasFixedSize(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AllTechNames> call, @NonNull Throwable t) {
                        if(isAdded()) {
                            loadingDialog.dismissLoadingDialog();
                            Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }
            }

        }

    }
}