package com.arraykart.b2b.SharedPreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceManager {
    private static String sharedPreferenceName = "User";//storage class name
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;//to save data

    public SharedPreferenceManager(Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public SharedPreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    //to check if a key is present or not
    public Boolean checkKey(String key){
        return sharedPreferences.contains(key);
    }

    //to set int value
    public int getInt(String key){
        return sharedPreferences.getInt(key, 0);
    }

    //to get int value
    public void setInt(String key, int value){
        editor.putInt(key, value).commit();
    }

    //to set string value
    public String getString(String key){
        return sharedPreferences.getString(key, null);
    }

    //to set string value
    public void setString(String key, String value){
        editor.putString(key, value).commit();
    }

    //to get boolean value
    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    //to set boolean value
    public void setBoolean(String key, Boolean value){
        editor.putBoolean(key, value).commit();
    }

    //to delete all values
    public void delSharPref(){
        editor.clear().commit();
    }
}
