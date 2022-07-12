package com.arraykart.b2b.Authenticate;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.arraykart.b2b.Home.HomeActivity;

import java.util.Locale;

public class LocaleManager {
    private Context context;

    public LocaleManager(Context context) {
        this.context = context;
    }

    public void updateResource(String code) {
        Log.e("check", "allo " + code);
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            Log.e("check", "allo " + "latest");
            configuration.setLocale(locale);
        } else{
            Log.e("check", "allo " + "old");
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            context.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }

    }
}
