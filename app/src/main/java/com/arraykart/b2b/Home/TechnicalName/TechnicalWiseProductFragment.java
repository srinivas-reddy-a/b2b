package com.arraykart.b2b.Home.TechnicalName;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_technical_wise_product, container, false);

        //get products from techname
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            techName = bundle.getString("techname");
        }
        getProducts(view);

        return view;
    }

    private void getProducts(View view) {


        if(isAdded()){
            Techname technameObj = new Techname(techName);
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            if(sharedPreferenceManager.checkKey("token")){
                Call<CategoryWise> call = RetrofitClient.getClient()
                        .getApi().getTechNameProducts(
                                sharedPreferenceManager.getString("token"),
                                technameObj
                        );
                call.enqueue(new Callback<CategoryWise>() {
                    @Override
                    public void onResponse(Call<CategoryWise> call, Response<CategoryWise> response) {
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
                    public void onFailure(Call<CategoryWise> call, Throwable t) {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }
}














