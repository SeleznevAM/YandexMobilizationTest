package com.applications.whazzup.yandextranslator.mvp.models;


public class TranslateModel extends AbstractModel {
    public void getAllLang() {
        mDataManager.getAllLag();
    }

    public void translateText(String text){
        mDataManager.translateText(text);
    }
}
