package com.applications.whazzup.yandextranslator.mvp.models;


import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;

import retrofit2.Call;

public class TranslateModel extends AbstractModel {

    public Call<YandexTranslateRes> translateText(String text, String direction) {
        return mDataManager.translateText(text, direction);
    }

    public LangRealm getLangByCode(String code) {
        return mDataManager.getRealmManager().getLangByCode(code);
    }

    public void saveTranslateInHistory(String originalText, String translateText, String direction, boolean isFavorite) {
        mDataManager.getRealmManager().saveTranslateInHistory(originalText, translateText, direction, isFavorite);
    }

    public TranslateRealm getTranslateRealmFromDb(String originalText, String direction) {
        return mDataManager.getRealmManager().getTranslateRealmFromDb(originalText, direction);
    }

    public boolean checkFavorite(String originalText, String direction){
        return mDataManager.getRealmManager().checkFavorite(originalText, direction);
    }

    public void saveTranslateToFavorite(TranslateRealm translateRealm) {
        mDataManager.getRealmManager().saveTranslateToFavorite(translateRealm);
    }

    public void deleteTranslateFromFavorite(TranslateRealm translateRealm) {
        mDataManager.getRealmManager().deleteTranslateFromFavorite(translateRealm);
    }

    public void saveTranslateHash(TranslateRealm translateRealm){
        mDataManager.getPreferencesManager().saveTranslateHash(translateRealm);
    }
    public String loadTranslateByHash(){
        return mDataManager.getPreferencesManager().loadTranslateByHash();
    }

}
