package com.arraykart.b2b.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.ConnectionReceiver;
import com.arraykart.b2b.Authenticate.Kyc;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Home.Fragments.CartFragment;
import com.arraykart.b2b.Home.Fragments.ScrollFragment;
import com.arraykart.b2b.Home.Fragments.WalletFragment;
import com.arraykart.b2b.Inapp.AppRater;
import com.arraykart.b2b.Search.SearchActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.Fragments.Account.AccountFragment;
import com.arraykart.b2b.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageButton;

public class HomeActivity extends AppCompatActivity implements ConnectionReceiver.ReceiverListener {


    private Fragment scrollFragment;
    private Fragment walletFragment;
    private Fragment cartFragment;
    private Fragment accountFragment;
    private NeumorphImageButton home;
    private NeumorphImageButton wallet;
    private NeumorphImageButton cart;
    private NeumorphImageButton account;
    private ImageView imageView;
    private ImageView icon;
    private NeumorphCardView neumorphCardView;

    private EditText editText;
    //for getContentResolver to work in wallet fragment
    private static Context contextOfApplication;
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }


    //userLocation
    private LocationManager locationManager;
    private LocationListener locationListener;
    private com.google.android.gms.location.LocationRequest locationRequest;

    double lat, lon;
    private TextView pincode;
    private SwitchCompat translate;
    private Context context;
    private Resources resources;
    private LocaleManager localeManager;
    private static Boolean isRecreated = false;
    private static Boolean isRecreatedEn = true;
    private static final String LANGUAGE = "language";
    private TextView hindiA;

    //header and footer
    private NeumorphCardView footer;
    private ConstraintLayout header;

    //shared preferences
    private SharedPreferenceManager sharedPreferenceManager;

    //toggle animation
    private LinearLayout iconLL;
    private LottieAnimationView leavesAnimation;

    //in app update
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 13;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferenceManager = new SharedPreferenceManager(this);

//        if(sharedPreferenceManager.getString("token") == null){
//            Intent i =
//        }

        //check network connectivity
        checkConnection();
        //check token
        //java.lang.IllegalStateException: FragmentManager has been destroyed error
        //while calling following function
        checkToken();

        //check kyc status
        checkKyc();

        //check language from shared pref
        checkLang();

        //check in app update
        checkUpdate();

        AppRater.app_launched(this);



        //user location
//        if(!sharedPreferenceManager.checkKey("GPS")){
        //set location
        pincode = findViewById(R.id.pincode);
        pincode.setOnClickListener(v -> checkPermission());
            checkPermission();
//        }

        //language translation
        translate = findViewById(R.id.translate);
        hindiA = findViewById(R.id.hindiA);
        setTranslate();

        //for getContentResolver to work in wallet fragment
        contextOfApplication = getApplicationContext();


        //toggle animation on icon click
        toggleAnimation();


        scrollFragment = new ScrollFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeContainer, scrollFragment).commit();
        imageView = findViewById(R.id.homeCompanyName);
        Glide.with(this)
                .load(R.drawable.company_name_green)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(imageView);
        icon = findViewById(R.id.homeCompanyicon);
        Glide.with(this)
                .load(R.drawable.company_icon)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(icon);
        pincode = findViewById(R.id.pincode);
        neumorphCardView = findViewById(R.id.neumorphCardView);
        editText = findViewById(R.id.search);
        editText.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(i)    ;
        });

//        animations for hints in search
//        final int interval_time=2000;
//        searchOptions = new ArrayList<>(Arrays.asList("Search Foods!", "Search Maggie!", "Search Chocolates!", "Search Biscuits!"));
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            int count=0;
//            @Override
//            public void run() {
//                if(count<searchOptions.size()){
//                    editText.setHint(searchOptions.get(count++));
//                    handler.postDelayed(this, interval_time);
//                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
//                    editText.startAnimation(animation);
//                }
//                if(count==searchOptions.size()){
//                    count=0;
//                }
//            }
//        };
//        handler.postDelayed(runnable, interval_time);

        home = findViewById(R.id.home);
        scrollFragment = getSupportFragmentManager().findFragmentByTag("homeFragment");
        Glide.with(this)
                .load(R.drawable.ic_outline_home_24_green)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(home);
        home.setOnTouchListener((v, event) -> {
            if(scrollFragment!=null && scrollFragment.isVisible()){
//                Toast.makeText(this, "visible", Toast.LENGTH_SHORT).show();
            }else{
//                Toast.makeText(this, "notvisible", Toast.LENGTH_SHORT).show();
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                home.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                home.setShapeType(0);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                scrollFragment = new ScrollFragment();
                FragmentTransaction fragmentTransaction14 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction14.replace(R.id.homeContainer, scrollFragment).commit();
                neumorphCardView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                icon.setVisibility(View.VISIBLE);
                pincode.setVisibility(View.VISIBLE);
                translate.setVisibility(View.VISIBLE);
                hindiA.setVisibility(View.VISIBLE);
            }
            //return true as motion up will occur only after motion down
            return true;
        });
        wallet = findViewById(R.id.wallet);
        Glide.with(this)
                .load(R.drawable.ic_outline_account_balance_wallet_24)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(wallet);
        wallet.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                wallet.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                wallet.setShapeType(0);
                if(walletFragment!=null && walletFragment.isVisible()){
//                    Toast.makeText(this, "visible", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(this, "notvisible", Toast.LENGTH_SHORT).show();
                }
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                walletFragment = new WalletFragment();
                neumorphCardView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                pincode.setVisibility(View.GONE);
                translate.setVisibility(View.GONE);
                hindiA.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction13 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction13.replace(R.id.homeContainer, walletFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });
        cart = findViewById(R.id.cart);
        Glide.with(this)
                .load(R.drawable.ic_outline_shopping_cart_24)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(cart);
        cart.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                cart.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                cart.setShapeType(0);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                cartFragment = new CartFragment();
                neumorphCardView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                pincode.setVisibility(View.GONE);
                translate.setVisibility(View.GONE);
                hindiA.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction12 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction12.replace(R.id.homeContainer,cartFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });
        account = findViewById(R.id.account);
        Glide.with(this)
                .load(R.drawable.ic_outline_account_circle_24)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(account);
        account.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                account.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                account.setShapeType(0);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                accountFragment = new AccountFragment();
                neumorphCardView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                pincode.setVisibility(View.GONE);
                translate.setVisibility(View.GONE);
                hindiA.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.homeContainer, accountFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });

        //all categories from api
    }

    InstallStateUpdatedListener installStateUpdatedListener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED){
            //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
            popupSnackbarForCompleteUpdate();
        }
//        else if (state.installStatus() == InstallStatus.INSTALLED){
//            if (mAppUpdateManager != null){
//                mAppUpdateManager.unregisterListener(installStateUpdatedListener);
//            }
//
//        }
        else {
            Log.i("update", "InstallStateUpdatedListener: state: " + state.installStatus());
        }
    };

    private void checkUpdate() {
        // Creates instance of the manager.

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

//        com.google.android.play.core.tasks.Task<AppUpdateInfo> task = mAppUpdateManager.getAppUpdateInfo();
//        task.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){
//
//                try {
//                    mAppUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, HomeActivity.this, RC_APP_UPDATE);
//
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, HomeActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
//                Log.e("update", "checkForAppUpdateAvailability: something else");
            }
        });
    }

    private void checkLang() {
        localeManager = new LocaleManager(HomeActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
            translate = findViewById(R.id.translate);
            if(sharedPreferenceManager.getString(LANGUAGE).trim().toLowerCase().contains("hi")){
                translate.setChecked(true);
            }else {
                translate.setChecked(false);
            }
        }
    }



    private void setTranslate() {
//        translate.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Restart app?");
//            builder.setCancelable(true);
//            builder.setPositiveButton("No", (dialog, which) -> {
//                dialog.cancel();
//            });
////            builder.setNegativeButton("Yes", (dialog, which) -> {
////
////            })
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        });
        translate.setOnCheckedChangeListener((buttonView, isChecked) -> {
                editText = findViewById(R.id.search);
                localeManager = new LocaleManager(HomeActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("App will be restarted!");
                builder.setCancelable(false);
//                builder.setPositiveButton("No", (dialog, which) -> {
//                    if(sharedPreferenceManager.getString(LANGUAGE).trim().toLowerCase().contains("hi")){
//                        translate.setChecked(true);
//                    }else {
//                        translate.setChecked(false);
//                    }
//                    dialog.cancel();
//                });
                builder.setNegativeButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    if (isChecked) {
                        sharedPreferenceManager.setString(LANGUAGE, "hi");
                        isRecreatedEn = true;
                    } else {
                        sharedPreferenceManager.setString(LANGUAGE, "en");
                        isRecreated = false;
                    }
                    localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
                    if (!isRecreated && isChecked) {
                        isRecreated = true;
//                   recreate();
                        startActivity(getIntent());
                        finish();
                        overridePendingTransition(0, 0);
                    }
                    if (isRecreatedEn && !isChecked) {
                        isRecreatedEn = false;
//                   recreate();
                        startActivity(getIntent());
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            });
    }

    private void checkKyc() {
        Kyc kyc = new Kyc(this);
        kyc.kycStatus();
    }

    private void toggleAnimation() {
        iconLL = findViewById(R.id.iconLL);
        leavesAnimation = findViewById(R.id.leavesAnimation);
        iconLL.setOnClickListener(v -> {
            leavesAnimation.setVisibility(View.VISIBLE);
            leavesAnimation.playAnimation();
            Handler handler = new Handler();
//                MediaPlayer mPlayer = MediaPlayer.create(HomeActivity.this, R.raw.leaves_audio_01);
//                mPlayer.start();
            handler.postDelayed(() -> {
                leavesAnimation.setVisibility(View.GONE);
                leavesAnimation.pauseAnimation();
//                        mPlayer.pause();
            },2000);
        });
    }

    private void checkPermission(){
        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        //Log.e("loc", "version");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(isGPSEnabled()){
                    LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                                            .removeLocationUpdates(this);
                                    if(locationResult != null && locationResult.getLocations().size() > 0){
                                        int index = locationResult.getLocations().size() - 1 ;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
//                                        sharedPreferenceManager.setString("GPS","gps");
                                        float[] results = new float[1];
                                        //Log.e("loc", ""+latitude);
                                        //Log.e("loc", ""+longitude);
                                        List<Address> addresses;
                                        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                                        try {
                                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                            String address = addresses.get(0).getAddressLine(0);
                                            String city = addresses.get(0).getLocality();
                                            String state = addresses.get(0).getAdminArea();
                                            String country = addresses.get(0).getCountryName();
                                            String postalCode = addresses.get(0).getPostalCode();
                                            String knownName = addresses.get(0).getFeatureName();
                                            pincode.setText(String.valueOf(postalCode));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
//                                        Location.distanceBetween(latitude, longitude, 27.8899595, 78.0989536, results);
//                                        float distanceInMeters = results[0];
//                                        boolean isWithinRange = distanceInMeters < 30000;
//
//
//                                        if (isWithinRange) {
//
//                                        }else {
//                                            alert("we are not servicing in your area we will reach you soon");
//                                        }
                                    }
                                }
                            }, Looper.getMainLooper());
                }else {
                    turnOnGPS();
//                    sharedPreferenceManager.setString("GPS","gps");
                }
            }else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(HomeActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                            resolvableApiException.startResolutionForResult(
                                    HomeActivity.this,2);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Device does not have location
                        break;
                }
            }
        });
    }


    private Boolean isGPSEnabled(){
        LocationManager locationManager = null;
        boolean isEnabled =false;
        if(locationManager == null){
            locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled ;

    }

    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }

    private boolean checkConnection() {
        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // display snack bar
        showAlertDialogue(isConnected);
        return isConnected;
    }

    private void showAlertDialogue(boolean isConnected) {
        // check condition
        if (isConnected) {

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("No Internet!");
            builder1.setCancelable(true);

            builder1.setPositiveButton("Ok",
                    (dialog, id) -> dialog.cancel());

//            builder1.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }
    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.green));
        snackbar.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call method
        checkToken();
        checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // call method
        checkToken();
        checkConnection();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    checkPermission();

                }else {

                    turnOnGPS();
                }
            }
        }
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        showAlertDialogue(isConnected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                checkPermission();
            }
        }
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "app download failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}














