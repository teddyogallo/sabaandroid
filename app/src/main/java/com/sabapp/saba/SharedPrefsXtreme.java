package com.sabapp.saba;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsXtreme {
    private static SharedPrefsXtreme yourPreference;
    private SharedPreferences sharedPreferences;

    public static SharedPrefsXtreme getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new SharedPrefsXtreme(context);
        }
        return yourPreference;
    }

    private SharedPrefsXtreme(Context context) {
        sharedPreferences = context.getSharedPreferences("PermsPrefs",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.apply();
    }

    public void deleteData(String key) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.remove(key).commit();
    }

    public void deleteAllData(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("PermsPrefs", Context.MODE_PRIVATE);
        settings.edit().clear().commit();




    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public int getIntData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }

    public void saveIntData(String key,int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putInt(key, value);
        prefsEditor.apply();
    }

    public void deleteIntData(String key) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.remove(key).commit();
    }


}
