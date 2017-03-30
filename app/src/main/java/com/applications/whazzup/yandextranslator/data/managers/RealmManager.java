package com.applications.whazzup.yandextranslator.data.managers;


import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmManager {
    private Realm mRealmInstance;

    public void saveLangToRealm(String id, String lang){
        Realm realm = Realm.getDefaultInstance();

        final LangRealm langRealm = new LangRealm(id, lang);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(langRealm);
            }
        });

        realm.close();
    }

    public RealmResults<LangRealm> getAllLang (){
        RealmResults<LangRealm> list = getQueryRealmInstance().where(LangRealm.class).findAll();
        return list;
    }

    private Realm getQueryRealmInstance() {
        if(mRealmInstance == null || mRealmInstance.isClosed()) {
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }
}
