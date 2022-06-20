package com.arraykart.b2b.Home.TechnicalName;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Products.ProductsRecycleradapter;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.ModelClass.Techname;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechnicalWiseProductFragment extends Fragment {
    private List<Product> products;
    private String techName;
    private RecyclerView techproductRV;
    private ProductsRecycleradapter productsRecycleradapter;
    private ImageView backTN;
    private LoadingDialog loadingDialog;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_technical_wise_product, container, false);
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }

        checkToken();

        //back
        back(view);

        if(isAdded()){
            loadingDialog = new LoadingDialog(requireActivity());
            loadingDialog.startLoadingDialog();
        }

        //get products from techname
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            techName = bundle.getString("techname");
        }
        getProducts(view);

        return view;
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
//                int index = requireActivity().getFragmentManager().getBackStackEntryCount();
//                FragmentManager.BackStackEntry backEntry = requireActivity().getSupportFragmentManager().getBackStackEntryAt(index);
//                String tag = backEntry.getName();
//                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(tag);
//                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager()
//                        .beginTransaction();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
//
            }
        });
    }

    private void getProducts(View view) {


        if(isAdded()){
            Techname technameObj = new Techname(techName);
            if(sharedPreferenceManager.checkKey("token")){
                Call<CategoryWise> call = RetrofitClient.getClient()
                        .getApi().getTechNameProducts(
                                sharedPreferenceManager.getString("token"),
                                technameObj
                        );
                if(isAdded()){

                    call.enqueue(new Callback<CategoryWise>() {
                        @Override
                        public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
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
                            products = response.body().getProducts();
                            if(isAdded()){
                                techproductRV = requireActivity().findViewById(R.id.techproductRV);
                                techproductRV.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                                productsRecycleradapter = new ProductsRecycleradapter(requireActivity(), products);
                                techproductRV.setHasFixedSize(true);
                                techproductRV.setAdapter(productsRecycleradapter);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CategoryWise> call, @NonNull Throwable t) {
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














