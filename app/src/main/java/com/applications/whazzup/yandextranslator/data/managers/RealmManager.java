package com.applications.whazzup.yandextranslator.data.managers;


import com.applications.whazzup.yandextranslator.data.storage.realm.FavoriteRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

public class RealmManager {
    private Realm mRealmInstance = null;

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

    public void saveTranslateInHistory(String originalText, String translateText, String direction, boolean isFavorite){
        Realm realm = Realm.getDefaultInstance();
        final TranslateRealm translateRealm = new TranslateRealm(originalText, translateText, direction, isFavorite);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(translateRealm);
            }
        });

        realm.close();
    }

    public void saveTranslateToFavorite(final TranslateRealm translateRealm){
        Realm realm = Realm.getDefaultInstance();
        final FavoriteRealm favoriteRealm = new FavoriteRealm(translateRealm);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                translateRealm.changeFavorite();
                realm.insertOrUpdate(favoriteRealm);

            }
        });
        realm.close();
    }

    public void deleteTranslateFromFavorite(final TranslateRealm translateRealm){
        Realm realm = Realm.getDefaultInstance();
        final FavoriteRealm favoriteRealm = new FavoriteRealm(translateRealm);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                translateRealm.changeFavorite();
               realm.where(FavoriteRealm.class).equalTo("originalText", favoriteRealm.getOriginalText()).equalTo("translateText", favoriteRealm.getTranslateText()).findFirst().deleteFromRealm();

            }
        });
        realm.close();
    }

    public LangRealm getLangByCode(String code){
        return getQueryRealmInstance().where(LangRealm.class).equalTo("id", code).findFirst();
    }

    private Realm getQueryRealmInstance() {

        if(mRealmInstance == null || mRealmInstance.isClosed()) {
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }

    public String getTranslateTextFromBb(String originalText){
        if(getQueryRealmInstance().where(TranslateRealm.class).equalTo("originalText", originalText).findFirst()==null){
            return null;
        }else{
            return getQueryRealmInstance().where(TranslateRealm.class).equalTo("originalText", originalText).findFirst().getTranslateText();
        }
    }

    public TranslateRealm getTranslateRealmFromDb(String originalText, String direction){
        return getQueryRealmInstance().where(TranslateRealm.class).equalTo("originalText", originalText).equalTo("direction", direction).findFirst();
    }

    public Observable<TranslateRealm> getTranslateHistory(){
        RealmResults<TranslateRealm> managerdTranslate = getQueryRealmInstance().where(TranslateRealm.class).findAll();
        return managerdTranslate.asObservable()
                .filter(new Func1<RealmResults<TranslateRealm>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<TranslateRealm> translateRealms) {
                        return translateRealms.isLoaded();
                    }
                })
                .first()
                .flatMap(new Func1<RealmResults<TranslateRealm>, Observable<? extends TranslateRealm>>() {
                    @Override
                    public Observable<? extends TranslateRealm> call(RealmResults<TranslateRealm> translateRealms) {
                        return Observable.from(translateRealms);
                    }
                });
    }

    public RealmResults<FavoriteRealm> getFavorite(){
        RealmResults<FavoriteRealm> managerTranslate = getQueryRealmInstance().where(FavoriteRealm.class).findAll();
        return managerTranslate;
    }

    public void deleteAllHistory(){
        Realm realm = getQueryRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(TranslateRealm.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public void clearHistory(){
        Realm realm = getQueryRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<FavoriteRealm> favoriteRealms =  realm.where(FavoriteRealm.class).findAll();
                for(FavoriteRealm favoriteRealm : favoriteRealms){
                    realm.where(TranslateRealm.class)
                            .equalTo("originalText", favoriteRealm.getOriginalText())
                            .equalTo("translateText", favoriteRealm.getTranslateText()).findFirst().changeFavorite();
                }
                favoriteRealms.deleteAllFromRealm();
            }
        });
        realm.close();
    }
}
