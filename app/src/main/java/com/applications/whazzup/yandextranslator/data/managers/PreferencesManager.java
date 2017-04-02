package com.applications.whazzup.yandextranslator.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.utils.ConstantManager;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;
    Context mContext;

    public PreferencesManager(Context context) {
        this.mSharedPreferences = App.getSharedPreferences();
        mContext = context;
    }

    public String getLanguageTo(){
        return mSharedPreferences.getString(ConstantManager.LANGUAGETO, "en");
    }

    public String getLanguageFrom(){
        return mSharedPreferences.getString(ConstantManager.LANGUAGEFROM, "ru");
    }

    public void saveLanguageTo(String language){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUAGETO, language);
        editor.apply();
    }

    public void saveLanguageFrom(String language){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUAGEFROM, language);
        editor.apply();
    }

}
