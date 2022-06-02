package com.arraykart.b2b.Home.TechnicalName;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.AllTechNames;
import com.arraykart.b2b.Retrofit.ModelClass.Techname;
import com.arraykart.b2b.Retrofit.RetrofitClient;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_technical_listing, container, false);

//        get technical names to rv
        getTechNamesToRV(view);

        return view;
    }

    private void getTechNamesToRV(View view) {
        techNameRV = view.findViewById(R.id.techNameRV);
        if(isAdded()){
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            if(sharedPreferenceManager.checkKey("token")){
                linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
                techNameRV.setLayoutManager(linearLayoutManager);
                Call<AllTechNames> call = RetrofitClient.getClient()
                        .getApi().getTechNames(sharedPreferenceManager.getString("token"));
                call.enqueue(new Callback<AllTechNames>() {
                    @Override
                    public void onResponse(Call<AllTechNames> call, Response<AllTechNames> response) {
                        if(!response.isSuccessful()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
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
//                            techNameRV.setHasFixedSize(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllTechNames> call, Throwable t) {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }

    }
}