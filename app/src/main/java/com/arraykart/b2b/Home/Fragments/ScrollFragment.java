package com.arraykart.b2b.Home.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Products.ProductsListingActivity;
import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.arraykart.b2b.Retrofit.ModelClass.AllCrops;
import com.arraykart.b2b.Retrofit.ModelClass.Crop;
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
import soup.neumorphism.NeumorphImageButton;

public class ScrollFragment extends Fragment {

    private ArrayList<Integer> images;
    private ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(R.color.cherryRed,R.color.yellow,R.color.blue,R.color.darkGreen));
    private RecyclerView banner;
    private BannerRecyclerAdapter bannerRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView productsRV;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private RecyclerView productsRV1;
    private ArrayList<Integer> imgs = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArrayList<String> categories = new ArrayList<>();
    private CategoriesRecyclerAdapter recyclerAdapter;
    private ArrayList<Integer> topProductsImages;
    private ArrayList<String> topProductsNames;
    private RecyclerView topProductsRV;
    private TopProductsRecyclerAdapter topProductsRecyclerAdapter;
    //ads rv
    private ArrayList<String> adsCompanyname;
    private ArrayList<String> adsOffer;
    private ArrayList<String> adsOfferExpl;
    private ArrayList<String> adsDate;
    private RecyclerView adsRV;
    private AdsRecyclerAdapter adsRecyclerAdapter;

    private NeumorphImageButton productRVMore;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);
        productRVMore = view.findViewById(R.id.productRVMore);
        Glide.with(view)
                .load(R.drawable.ic_baseline_arrow_forward_ios_24_gray)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(productRVMore);
//        productRVMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(requireActivity(), SubCategoriesActivity.class);
//                startActivity(i);
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
                Intent i = new Intent(requireActivity(), ProductsListingActivity.class);
                startActivity(i);
            }
        });
        topProductsMore = view.findViewById(R.id.topProductsMore);
        Glide.with(view)
                .load(R.drawable.ic_baseline_arrow_forward_ios_24_smoke_white)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(topProductsMore);
        topProductsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity(), ProductsListingActivity.class);
                startActivity(i);
            }
        });
        banner = view.findViewById(R.id.banner);
        banner.setHasFixedSize(true);
        linearLayoutManager = (new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        banner.setLayoutManager(linearLayoutManager);
        banner.addItemDecoration(new LinePagerIndicatorDecoration());
        images = new ArrayList<>(Arrays.asList(R.drawable.banner0,R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4));
        bannerRecyclerAdapter = new BannerRecyclerAdapter(images, (HomeActivity) requireActivity());
        banner.setAdapter(bannerRecyclerAdapter);
        banner.smoothScrollToPosition(0);
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
        final int interval_time=3000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count=0;
            @Override
            public void run() {
                if(count<images.size()){
                    banner.smoothScrollToPosition(count++);
                    handler.postDelayed(this, interval_time);
                    if(count==images.size()){
                        count=0;
                    }
                }
            }
        };
        handler.postDelayed(runnable,interval_time);


        productsRV = view.findViewById(R.id.productRV);
        productsRV.setHasFixedSize(true);
        productsRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        imgs.add(R.drawable.img0);
        imgs.add(R.drawable.img1);
        imgs.add(R.drawable.img2);
        imgs.add(R.drawable.img3);
        imgs.add(R.drawable.img4);
        productRecyclerAdapter = new ProductRecyclerAdapter(imgs, (HomeActivity) requireActivity());
        productsRV.setAdapter(productRecyclerAdapter);
        productsRV.smoothScrollToPosition(imgs.size()-1);

        productsRV1 = view.findViewById(R.id.productRV1);
        productsRV1.setHasFixedSize(true);
        productsRV1.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        productRecyclerAdapter = new ProductRecyclerAdapter(imgs, (HomeActivity) requireActivity());
        productsRV1.setAdapter(productRecyclerAdapter);
        productsRV1.smoothScrollToPosition(imgs.size()-1);


        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        categories.add("All Categories");
        categories.add("Manufacturers");
        categories.add("Categories");
        categories.add("Promotions");
        recyclerAdapter = new CategoriesRecyclerAdapter(categories, (HomeActivity) requireActivity());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.smoothScrollToPosition(imgs.size()-1);

        topProductsRV = view.findViewById(R.id.topProductsRV);
        topProductsRV.setHasFixedSize(true);
        topProductsRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        topProductsImages = new ArrayList<>();
        topProductsImages.add(R.drawable.tp0);
        topProductsImages.add(R.drawable.tp1);
        topProductsImages.add(R.drawable.tp2);
        topProductsImages.add(R.drawable.tp3);
        topProductsNames = new ArrayList<>();
        topProductsNames.add("Maggie");
        topProductsNames.add("Oil");
        topProductsNames.add("Soap");
        topProductsNames.add("Deodorant");
        topProductsRecyclerAdapter = new TopProductsRecyclerAdapter(topProductsImages, topProductsNames, (HomeActivity) requireActivity());
        topProductsRV.setAdapter(topProductsRecyclerAdapter);
        topProductsRV.smoothScrollToPosition(topProductsImages.size()-1);

        adsRV = view.findViewById(R.id.adsRV);
        adsRV.setHasFixedSize(true);
        adsCompanyname = new ArrayList<>(Arrays.asList("Nestle","Tata","Nipton","ECS"));
        adsOffer = new ArrayList<>(Arrays.asList("Buy one get one free!", "Buy one get one free!", "Buy one get one free!", "Buy one get one free!"));
        adsOfferExpl = new ArrayList<>(Arrays.asList("On products above 300", "On products above 500","Extra 300 off on orders above 1300", "Extra 300 off on orders above 1500"));
        adsDate = new ArrayList<>(Arrays.asList("From 3rd April", "From 3rd April", "From 3rd April" , "From 3rd April"));
        adsRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));;
        adsRecyclerAdapter = new AdsRecyclerAdapter(adsCompanyname, adsOffer, adsOfferExpl, adsDate, topProductsImages, colors, (HomeActivity) requireActivity());
        adsRV.setAdapter(adsRecyclerAdapter);
        
        //all categories
        getAllCategories(view);

        //timerAds
        timerAds = view.findViewById(R.id.timerAds);
        timerAds.setVisibility(View.GONE);
        adTimer = view.findViewById(R.id.adTimer);
        requireActivity().registerReceiver(m_timeChangedReceiver, s_intentFilter);
        //to see, delete after demo
        timerAds.setVisibility(View.VISIBLE);
        setTimerAds((12*60*60*1000));
        //allcrops
        getAllCrops(view);
        
        return view;
    }

    private void getAllCrops(View view) {
        cropRV = view.findViewById(R.id.cropRV);
        cropRV.setHasFixedSize(true);
        cropRV.setItemViewCacheSize(20);
        cropRV.setDrawingCacheEnabled(true);
        cropRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        cropRV.setLayoutManager(linearLayoutManager);
        Call<AllCrops> call = RetrofitClient.getClient().getApi().getCrops(10);
        loadingDialog = new LoadingDialog(requireActivity());
//        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<AllCrops>() {
            @Override
            public void onResponse(Call<AllCrops> call, Response<AllCrops> response) {
//                loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(requireActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.body().getSuccess()){
                    Toast.makeText(requireActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                allCrops = response.body().getCrops();
                allCropsAdapter = new AllCropsAdapter(requireActivity(), allCrops);
                allCropsAdapter.setHasStableIds(true);
                cropRV.setAdapter(allCropsAdapter);

            }

            @Override
            public void onFailure(Call<AllCrops> call, Throwable t) {
//                loadingDialog.dismissLoadingDialog();
                Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    requireActivity().unregisterReceiver(m_timeChangedReceiver);
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
        linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        categoryWiseNestedRV.setLayoutManager(linearLayoutManager);
        categoryWiseListingRecyclerAdapter = new CategoryWiseListingRecyclerAdapter(allCategories, requireActivity());
        categoryWiseNestedRV.setAdapter(categoryWiseListingRecyclerAdapter);

    }

    private void getAllCategories(View view) {
        rvGridCat = view.findViewById(R.id.catRV);
        rvGridCat.setHasFixedSize(true);
        rvGridCat.setItemViewCacheSize(20);
        rvGridCat.setDrawingCacheEnabled(true);
        rvGridCat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvGridCat.setLayoutManager(linearLayoutManager);
        Call<AllCategories> call = RetrofitClient.getClient().getApi().getAllCategories();
        loadingDialog = new LoadingDialog(requireActivity());
//        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<AllCategories>() {
            @Override
            public void onResponse(Call<AllCategories> call, Response<AllCategories> response) {
//                loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(requireActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.body().getSuccess()){
                    Toast.makeText(requireActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                allCategories = response.body().getCategories();
                allCategoriesAdapter = new AllCategoriesAdapter(requireActivity(), allCategories);
                allCategoriesAdapter.setHasStableIds(true);
                rvGridCat.setAdapter(allCategoriesAdapter);
                //cat wise products
                getCatWiseProducts(view, allCategories);

            }

            @Override
            public void onFailure(Call<AllCategories> call, Throwable t) {
//                loadingDialog.dismissLoadingDialog();
                Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}