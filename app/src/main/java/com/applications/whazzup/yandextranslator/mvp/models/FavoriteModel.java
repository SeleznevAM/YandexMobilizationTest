package com.applications.whazzup.yandextranslator.mvp.models;


import com.applications.whazzup.yandextranslator.data.storage.realm.FavoriteRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;

import java.util.List;

import io.realm.RealmResults;

public class FavoriteModel extends AbstractModel {

    public RealmResults<FavoriteRealm> getFavorite(){
        return mDataManager.getRealmManager().getFavorite();
    }

    public void clearHistory(){
        mDataManager.getRealmManager().clearHistory();
    }
}
