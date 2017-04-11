package com.applications.whazzup.yandextranslator.mvp.models;


import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;

import io.realm.RealmResults;

public class LanguageModel extends AbstractModel {

    public RealmResults<LangRealm> getAllLang() {
        return mDataManager.getRealmManager().getAllLang();
    }
}
