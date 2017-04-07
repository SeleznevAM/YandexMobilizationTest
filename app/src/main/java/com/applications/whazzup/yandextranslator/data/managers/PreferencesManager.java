package com.applications.whazzup.yandextranslator.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.utils.ConstantManager;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public PreferencesManager(Context context) {
        this.mSharedPreferences = App.getSharedPreferences();
        mContext = context;
    }

    public String getLanguageTo(){
        return mSharedPreferences.getString(ConstantManager.LANGUAGE_TO, "en");
    }

    public String getLanguageFrom(){
        return mSharedPreferences.getString(ConstantManager.LANGUAGE_FROM, "ru");
    }


    public void saveLanguageTo(String language){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUAGE_TO, language);
        editor.apply();
    }

    public void saveLanguageFrom(String language){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUAGE_FROM, language);
        editor.apply();
    }

}
