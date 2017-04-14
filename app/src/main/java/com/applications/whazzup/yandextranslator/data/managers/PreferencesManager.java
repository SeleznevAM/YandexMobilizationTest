package com.applications.whazzup.yandextranslator.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;
import com.applications.whazzup.yandextranslator.utils.ConstantManager;
import com.google.gson.Gson;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public PreferencesManager(Context context) {
        this.mSharedPreferences = App.getSharedPreferences();
        mContext = context;
    }

    public String getLanguageTo() {
        return mSharedPreferences.getString(ConstantManager.LANGUAGE_TO, "en");
    }

    public String getLanguageFrom() {
        return mSharedPreferences.getString(ConstantManager.LANGUAGE_FROM, "ru");
    }


    public void saveLanguageTo(String language) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUAGE_TO, language);
        editor.apply();
    }

    public void saveLanguageFrom(String language) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LANGUAGE_FROM, language);
        editor.apply();
    }

    public void saveTranslateRealm(TranslateRealm translateRealm){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(translateRealm);
        editor.putString(ConstantManager.TRANSLATE_REALM, json);
        editor.commit();
    }

    public TranslateRealm loadTranslateRealm(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(ConstantManager.TRANSLATE_REALM, "");
        return  gson.fromJson(json, TranslateRealm.class);
    }

    public void saveTranslateHash(TranslateRealm translateRealm){
        if(translateRealm!=null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("TRANSLATE_HASH", translateRealm.getId());
            editor.commit();
        }
    }

    public String loadTranslateByHash(){
        return mSharedPreferences.getString("TRANSLATE_HASH", null);

    }
}
