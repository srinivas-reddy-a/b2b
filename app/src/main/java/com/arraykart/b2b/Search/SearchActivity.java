package com.arraykart.b2b.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.Search.Adapter.SearchPageRecyclerAdapter;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ImageView back;
    private EditText editText;
    private ArrayList<String> searchOptions;
    private List<Product> products;
    private SearchPageRecyclerAdapter searchPageRecyclerAdapter;
    private RecyclerView searchRV;
    private TextView searchEx;
    private TextView searchedFor;
    private LoadingDialog loadingDialog;
    private static final String LANGUAGE = "language";
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sharedPreferenceManager = new SharedPreferenceManager(this);

        checkToken();

        checkLang();

        editText = findViewById(R.id.search);
        //to open keyboard automatically as activity loads
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //if delay is not used keyboard doesnt open immediately
        editText.postDelayed(() -> {
            editText.requestFocus();
            imm.showSoftInput(editText,0);
        },100);


        back = findViewById(R.id.back);
        searchEx = findViewById(R.id.searchEx);
        searchedFor = findViewById(R.id.searchedFor);
        searchRV = findViewById(R.id.searchRV);
        Glide.with(this)
                .load(R.drawable.ic_baseline_keyboard_backspace_24)
//                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(back);
        back.setOnClickListener(v -> finish());

//        final int interval_time = 1000;
//        searchOptions = new ArrayList<>(Arrays.asList("Search Foods!", "Search Maggie!", "Search Chocolates!", "Search Biscuits!"));
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            int count=0;
//            @Override
//            public void run() {
//                if (count<searchOptions.size()){
//                    editText.setHint(searchOptions.get(count++));
//                    handler.postDelayed(this, interval_time);
//                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
//                    editText.startAnimation(animation);
//                }
//                if(count==searchOptions.size()){
//                    count=0;
//                }
//
//            }
//        };
//        handler.postDelayed(runnable, interval_time);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){

                    //used categorywise as search json response syntax is also same
                    Call<CategoryWise> call = RetrofitClient.getClient()
                            .getApi().getSearch(s.toString(), null);
//                    loadingDialog = new LoadingDialog(SearchActivity.this);
//                    loadingDialog.startLoadingDialog();
                    call.enqueue(new Callback<CategoryWise>() {
                        @Override
                        public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
//                            loadingDialog.dismissLoadingDialog();
                            if (!response.isSuccessful()) {
                                Toast.makeText(SearchActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            assert response.body() != null;
                            if (!response.body().getSuccess()) {
                                Toast.makeText(SearchActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            searchEx.setVisibility(View.GONE);
                            searchedFor.setVisibility(View.VISIBLE);
                            searchRV.setVisibility(View.VISIBLE);
                            products = response.body().getProducts();
                            searchRV.setHasFixedSize(true);
                            searchPageRecyclerAdapter = new SearchPageRecyclerAdapter(
                                    SearchActivity.this, products);
                            searchRV.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                            searchRV.setAdapter(searchPageRecyclerAdapter);
                            return;
                        }

                        @Override
                        public void onFailure(@NonNull Call<CategoryWise> call, @NonNull Throwable t) {
//                            loadingDialog.dismissLoadingDialog();
                            Toast.makeText(SearchActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    searchEx.setVisibility(View.VISIBLE);
                    searchedFor.setVisibility(View.GONE);
                    searchRV.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                editText.clearAnimation();
//                handler.removeCallbacks(runnable);
            }
        });


    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(SearchActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }

    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }
}