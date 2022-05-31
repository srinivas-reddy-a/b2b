package com.arraykart.b2b.Home.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Home.Adapters.AdsRecyclerAdapter;
import com.arraykart.b2b.Home.Adapters.AllCropsAdapter;
import com.arraykart.b2b.Home.Adapters.BannerRecyclerAdapter;
import com.arraykart.b2b.Home.TechnicalName.TechnicalNameActivity;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Products.ProductsListingActivity;
import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.arraykart.b2b.Retrofit.ModelClass.Ad;
import com.arraykart.b2b.Retrofit.ModelClass.AllCrops;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Crop;
import com.arraykart.b2b.Retrofit.ModelClass.MetaData;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SignUpActivity;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.Home.Adapters.AllCategoriesAdapter;
import com.arraykart.b2b.Home.Adapters.CategoriesRecyclerAdapter;
import com.arraykart.b2b.Home.Adapters.CategoryWiseListingRecyclerAdapter;
import com.arraykart.b2b.Home.Adapters.ProductRecyclerAdapter;
import com.arraykart.b2b.Home.Adapters.TopProductsRecyclerAdapter;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.AllCategories;
import com.arraykart.b2b.Retrofit.ModelClass.Category;
import com.arraykart.b2b.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageButton;

public class ScrollFragment extends Fragment {

    private ArrayList<Integer> images;
    private ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(R.color.cherryRed,R.color.yellow,R.color.blue,R.color.darkGreen));
//    banner ads
    private RecyclerView banner;
    private List<Ad> ads;

    private BannerRecyclerAdapter bannerRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    //topproducts
    private RecyclerView topRV;
    private List<Product> products;

    private ProductRecyclerAdapter productRecyclerAdapter;
    private RecyclerView productsRV1;
    private ArrayList<Integer> imgs = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArrayList<String> categories = new ArrayList<>();
    private CategoriesRecyclerAdapter recyclerAdapter;
    private ArrayList<Integer> topProductsImages;
    private ArrayList<String> topProductsNames;
    private RecyclerView freqRV;
    private TopProductsRecyclerAdapter topProductsRecyclerAdapter;
    //ads rv
    private ArrayList<String> adsCompanyname;
    private ArrayList<String> adsOffer;
    private ArrayList<String> adsOfferExpl;
    private ArrayList<String> adsDate;
    private RecyclerView adsRV;
    private AdsRecyclerAdapter adsRecyclerAdapter;

    private NeumorphImageButton topRVMore;
    private NeumorphImageButton productRV1More;
    private NeumorphImageButton topProductsMore;

    //all categories
    private RecyclerView rvGridCat;
    private AllCategoriesAdapter allCategoriesAdapter;
    private List<Category> allCategories;

    //category wise products
    private RecyclerView categoryWiseNestedRV;
    private CategoryWiseListingRecyclerAdapter categoryWiseListingRecyclerAdapter;

    //loading bar
    private LoadingDialog loadingDialog;

    //timerAds
    private ConstraintLayout timerAds;
    private TextView adTimer;
    private Calendar calendar;
    private static IntentFilter s_intentFilter;
    static {
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
    }

    //allcrops
    private RecyclerView cropRV;
    private List<Crop> allCrops;
    private AllCropsAdapter allCropsAdapter;

    //technical names
    private NeumorphCardView techNCV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);

        //set technical name products listing
        setTechNames(view);

        //set Top Products
        setTopProducts(view);

        //set freq bought products
        setFreqProducts(view);

        //set banner ads
        setBanner(view);

//        topRVMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//        if(isAdded()) {
//                Intent i = new Intent(requireActivity(), SubCategoriesActivity.class);
//                startActivity(i);
//        }
//            }
//        });
        productRV1More = view.findViewById(R.id.productRV1More);
        Glide.with(view)
                .load(R.drawable.ic_baseline_arrow_forward_ios_24_gray)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(productRV1More);
        productRV1More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdded()) {
                    Intent i = new Intent(requireActivity(), ProductsListingActivity.class);
                    startActivity(i);
                }
            }
        });


//        not working
//        LinearSnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(banner);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() < (bannerRecyclerAdapter.getItemCount()-1)){
//                    linearLayoutManager.smoothScrollToPosition(banner,  new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
//                }
//                else {
//                    linearLayoutManager.smoothScrollToPosition(banner, new RecyclerView.State(), 0);
//
//                }
//            }
//        },0,3000);





//        productsRV1 = view.findViewById(R.id.productRV1);
//        productsRV1.setHasFixedSize(true);
//        if(isAdded()) {
//            productsRV1.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
//            productRecyclerAdapter = new ProductRecyclerAdapter(imgs, (HomeActivity) requireActivity());
//            productsRV1.setAdapter(productRecyclerAdapter);
//            productsRV1.smoothScrollToPosition(imgs.size() - 1);
//        }

//        recyclerView = view.findViewById(R.id.rv);
//        recyclerView.setHasFixedSize(true);
//        if(isAdded()) {
//            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
//            categories.add("All Categories");
//            categories.add("Manufacturers");
//            categories.add("Categories");
//            categories.add("Promotions");
//            recyclerAdapter = new CategoriesRecyclerAdapter(categories, (HomeActivity) requireActivity());
//            recyclerView.setAdapter(recyclerAdapter);
//            recyclerView.smoothScrollToPosition(imgs.size() - 1);
//        }



        adsRV = view.findViewById(R.id.adsRV);
        adsRV.setHasFixedSize(true);
        adsCompanyname = new ArrayList<>(Arrays.asList("Inventory Management","Digital Lending","DigitalShop"));
        adsOffer = new ArrayList<>(Arrays.asList("", "", ""));
        adsOfferExpl = new ArrayList<>(Arrays.asList("Coming soon...", "Coming soon...","Coming soon..."));
        adsDate = new ArrayList<>(Arrays.asList("", "" , ""));
        topProductsImages = new ArrayList<>(Arrays.asList(R.drawable.inventorymanagement, R.drawable.digitallending, R.drawable.digitalshop));
        if(isAdded()) {
        adsRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));;
        adsRecyclerAdapter = new AdsRecyclerAdapter(adsCompanyname, adsOffer, adsOfferExpl, adsDate, topProductsImages, colors, (HomeActivity) requireActivity());
        adsRV.setAdapter(adsRecyclerAdapter);
        }
        
        //all categories
        getAllCategories(view);

        //timerAds
//        timerAds = view.findViewById(R.id.timerAds);
//        timerAds.setVisibility(View.GONE);
//        adTimer = view.findViewById(R.id.adTimer);
//        if(isAdded()) {
//            requireActivity().registerReceiver(m_timeChangedReceiver, s_intentFilter);
//            //to see, delete after demo
//            timerAds.setVisibility(View.VISIBLE);
//            setTimerAds((12 * 60 * 60 * 1000));
//        }
        //allcrops
        getAllCrops(view);

        return view;
    }

    private void setTechNames(View view) {
        techNCV = view.findViewById(R.id.techNCV);
        techNCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdded()) {
                    Intent i = new Intent(requireActivity(), TechnicalNameActivity.class);
                    requireActivity().startActivity(i);
                }
            }
        });
    }

    private void setBanner(View view) {
        banner = view.findViewById(R.id.banner);
        banner.setHasFixedSize(true);
        if(isAdded()) {
            linearLayoutManager = (new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
            banner.setLayoutManager(linearLayoutManager);
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            if(sharedPreferenceManager.checkKey("token")){
                Call<MetaData> call = RetrofitClient.getClient()
                        .getApi().getAds(sharedPreferenceManager.getString("token"));
                call.enqueue(new Callback<MetaData>() {
                    @Override
                    public void onResponse(Call<MetaData> call, Response<MetaData> response) {
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
                        ads = response.body().getAds();
                        if(isAdded()) {
                            bannerRecyclerAdapter = new BannerRecyclerAdapter(ads, requireActivity());
                            banner.setAdapter(bannerRecyclerAdapter);
//                        banner.addItemDecoration(new LinePagerIndicatorDecoration());
                            banner.smoothScrollToPosition(0);
                            final int interval_time = 3000;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                int count = 0;

                                @Override
                                public void run() {
                                    if (count < ads.get(0).getProduct().split(",").length) {
                                        banner.smoothScrollToPosition(count++);
                                        handler.postDelayed(this, interval_time);
                                        if (count == ads.get(0).getProduct().split(",").length) {
                                            count = 0;
                                        }
                                    }
                                }
                            };
                            handler.postDelayed(runnable, interval_time);
                        }
                    }

                    @Override
                    public void onFailure(Call<MetaData> call, Throwable t) {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(requireActivity(), "Signup first!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(requireActivity(), SignUpActivity.class);
                requireActivity().startActivity(i);
            }

        }
    }

    private void setFreqProducts(View view) {
        freqRV = view.findViewById(R.id.freqRV);
        freqRV.setHasFixedSize(true);
        if(isAdded()) {
            freqRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
            Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                    .setFreqProducts(true, 5);
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
                    if(isAdded()) {
                        productRecyclerAdapter = new ProductRecyclerAdapter(products, requireActivity());
                        freqRV.setAdapter(productRecyclerAdapter);
                        freqRV.smoothScrollToPosition(products.size()-1);
                    }
                }

                @Override
                public void onFailure(Call<CategoryWise> call, Throwable t) {
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            topProductsRecyclerAdapter = new TopProductsRecyclerAdapter(topProductsImages, topProductsNames, (HomeActivity) requireActivity());
//            freqRV.setAdapter(topProductsRecyclerAdapter);
//            freqRV.smoothScrollToPosition(topProductsImages.size() - 1);
        }
    }

    private void setTopProducts(View view) {
        topRVMore = view.findViewById(R.id.topRVMore);
        Glide.with(view)
                .load(R.drawable.ic_baseline_arrow_forward_ios_24_gray)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(topRVMore);
        topRV = view.findViewById(R.id.topRV);
        topRV.setHasFixedSize(true);
        if(isAdded()) {
            topRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
            Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                    .setTopProducts(true, 4);
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
                    if(isAdded()) {
                        productRecyclerAdapter = new ProductRecyclerAdapter(products, requireActivity());
                        topRV.setAdapter(productRecyclerAdapter);
                        topRV.smoothScrollToPosition(products.size()-1);
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

    private void getAllCrops(View view) {
        cropRV = view.findViewById(R.id.cropRV);
        cropRV.setHasFixedSize(true);
        cropRV.setItemViewCacheSize(20);
        cropRV.setDrawingCacheEnabled(true);
        cropRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if(isAdded()) {
            linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
            cropRV.setLayoutManager(linearLayoutManager);
        }
        Call<AllCrops> call = RetrofitClient.getClient().getApi().getCrops(10);
        if(isAdded()) {
            loadingDialog = new LoadingDialog(requireActivity());
        }
//        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<AllCrops>() {
            @Override
            public void onResponse(Call<AllCrops> call, Response<AllCrops> response) {
//                loadingDialog.dismissLoadingDialog();
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
                allCrops = response.body().getCrops();
                if(isAdded()){
                    allCropsAdapter = new AllCropsAdapter(requireActivity(), allCrops);
                    allCropsAdapter.setHasStableIds(true);
                    cropRV.setAdapter(allCropsAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllCrops> call, Throwable t) {
//                loadingDialog.dismissLoadingDialog();
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                    action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);
                timerAds.setVisibility(View.GONE);
                if(calendar.get(Calendar.AM_PM) == Calendar.AM && hour >= 11){
                    int milliSec = Math.abs(7 - hour) * Math.abs(60 - min) *  Math.abs(60 - sec) * 1000;
                    timerAds.setVisibility(View.VISIBLE);
                    setTimerAds((12*60*60*1000) - milliSec);
                    if(isAdded()) {
                        requireActivity().unregisterReceiver(m_timeChangedReceiver);
                    }
                }
            }
        }
    };

    private void setTimerAds(int milliSec) {
        new CountDownTimer(milliSec, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                String h = twoDigitString((millisUntilFinished/(60*60*1000)) % 24);
                String m = twoDigitString((millisUntilFinished/(60 * 1000)) % 60);
                String s = twoDigitString((millisUntilFinished/(1000) % 60));
                adTimer.setText("Ends in: " + h + ":"+ m + ":" + s);
            }

            @Override
            public void onFinish() {
                timerAds.setVisibility(View.GONE);
            }
        }.start();
    }
    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    private void getCatWiseProducts(View view, List allCategories) {
        categoryWiseNestedRV = view.findViewById(R.id.categoryWiseNestedRV);
        categoryWiseNestedRV.getLayoutParams().height = 400*9;
        categoryWiseNestedRV.setHasFixedSize(true);
        categoryWiseNestedRV.setItemViewCacheSize(20);
        categoryWiseNestedRV.setDrawingCacheEnabled(true);
        categoryWiseNestedRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if(isAdded()) {
            linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
            categoryWiseNestedRV.setLayoutManager(linearLayoutManager);
            categoryWiseListingRecyclerAdapter = new CategoryWiseListingRecyclerAdapter(allCategories, requireActivity());
            categoryWiseNestedRV.setAdapter(categoryWiseListingRecyclerAdapter);
        }
    }

    private void getAllCategories(View view) {
        rvGridCat = view.findViewById(R.id.catRV);
        rvGridCat.setHasFixedSize(true);
        rvGridCat.setItemViewCacheSize(20);
        rvGridCat.setDrawingCacheEnabled(true);
        rvGridCat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if(isAdded()) {
            linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
            rvGridCat.setLayoutManager(linearLayoutManager);
        }
        Call<AllCategories> call = RetrofitClient.getClient().getApi().getAllCategories();
        if(isAdded()) {
            loadingDialog = new LoadingDialog(requireActivity());
        }
//        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<AllCategories>() {
            @Override
            public void onResponse(Call<AllCategories> call, Response<AllCategories> response) {
//                loadingDialog.dismissLoadingDialog();
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
                allCategories = response.body().getCategories();
                if(isAdded()) {
                    allCategoriesAdapter = new AllCategoriesAdapter(requireActivity(), allCategories);
                    allCategoriesAdapter.setHasStableIds(true);
                    rvGridCat.setAdapter(allCategoriesAdapter);
                    //cat wise products
                    getCatWiseProducts(view, allCategories);
                }

            }

            @Override
            public void onFailure(Call<AllCategories> call, Throwable t) {
//                loadingDialog.dismissLoadingDialog();
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}