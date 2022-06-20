package com.arraykart.b2b.Authenticate;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.arraykart.b2b.Home.HomeActivity;

import java.util.Locale;

public class LocaleManager {
    private Context context;

    public LocaleManager(Context context) {
        this.context = context;
    }

    public void updateResource(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
