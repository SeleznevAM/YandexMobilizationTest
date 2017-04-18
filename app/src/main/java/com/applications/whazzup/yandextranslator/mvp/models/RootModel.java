package com.applications.whazzup.yandextranslator.mvp.models;


import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;

public class RootModel extends AbstractModel {

    public void getAllLang() {
        mDataManager.getAllLang();
    }

    public TranslateRealm getTranslateRealmFromDb(String originalText, String direction) {
        return mDataManager.getRealmManager().getTranslateRealmFromDb(originalText, direction);
    }

    public void saveTranslateHash(TranslateRealm translateRealm){
        mDataManager.getPreferencesManager().saveTranslateHash(translateRealm);
    }

    public String loadTranslateByHash(){
        return mDataManager.getPreferencesManager().loadTranslateByHash();
    }

    public void saveLanguageTo(String language){
        mDataManager.getPreferencesManager().saveLanguageTo(language);
    }

    public void saveLanguageFrom(String language) {
        mDataManager.getPreferencesManager().saveLanguageFrom(language);
    }

    public String getLanguageTo() {
        return mDataManager.getPreferencesManager().getLanguageTo();
    }

    public String getLanguageFrom() {
        return mDataManager.getPreferencesManager().getLanguageFrom();
    }
}
