package com.applications.whazzup.yandextranslator.mvp.models;


import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;

import retrofit2.Call;

public class TranslateModel extends AbstractModel {
    public void getAllLang() {
        mDataManager.getAllLag();
    }

    public Call<YandexTranslateRes> translateText(String text, String direction){
        return mDataManager.translateText(text, direction);
    }

    public String getLanguageTo(){
       return mDataManager.getPreferencesManager().getLanguageTo();
    }

    public String getLanguageFrom(){
        return mDataManager.getPreferencesManager().getLanguageFrom();
    }

    public LangRealm getLangByCode(String code){
        return mDataManager.getRealmManager().getLangByCode(code);
    }

    public void saveTransletInHistory(String originalText, String translateText, String direction, boolean isFavorite){
        mDataManager.getRealmManager().saveTranslateInHistory(originalText, translateText, direction, isFavorite);
    }

    public String getTranslateTextFromBb(String originalText){
        return mDataManager.getRealmManager().getTranslateTextFromBd(originalText);
    }

    public TranslateRealm getTranslateRealmFromDb(String originalText, String direction){
        return mDataManager.getRealmManager().getTranslateRealmFromDb(originalText, direction);
    }


}
