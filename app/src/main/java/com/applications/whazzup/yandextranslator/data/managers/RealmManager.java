package com.applications.whazzup.yandextranslator.data.managers;


import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;

import io.realm.Realm;

public class RealmManager {

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
}
