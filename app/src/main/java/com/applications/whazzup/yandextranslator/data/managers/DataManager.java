package com.applications.whazzup.yandextranslator.data.managers;


import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.data.network.RestService;
import com.applications.whazzup.yandextranslator.data.network.res.YandexLangRes;
import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;
import com.applications.whazzup.yandextranslator.di.components.DaggerDataManagerComponent;
import com.applications.whazzup.yandextranslator.di.modules.LocalModule;
import com.applications.whazzup.yandextranslator.di.modules.NetworkModule;
import com.applications.whazzup.yandextranslator.utils.ConstantManager;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {

    private static DataManager ourInstance = null;

    @Inject
    RestService mRestService;
    @Inject
    PreferencesManager mPreferencesManager;

    RealmManager mRealmManager = new RealmManager();

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }


    private DataManager() {
        DaggerDataManagerComponent.builder()
                .appComponent(App.getAppComponent())
                .networkModule(new NetworkModule())
                .localModule(new LocalModule())
                .build().inject(this);
    }


    public void getAllLag() {
        Call<YandexLangRes> call = mRestService.getAllLang("trnsl.1.1.20170315T090000Z.1bbd09f1e8dede27.4aad6b1dded1e6519341fbcfd427ebea820535bd", "ru");
        call.enqueue(new Callback<YandexLangRes>() {
            @Override
            public void onResponse(Call<YandexLangRes> call, Response<YandexLangRes> response) {
                for(Map.Entry<String, String> entry : response.body().langs.entrySet()){
                    mRealmManager.saveLangToRealm(entry.getKey(), entry.getValue());
                }
            }

            @Override
            public void onFailure(Call<YandexLangRes> call, Throwable t) {

            }
        });
    }

    public Call<YandexTranslateRes> translateText(String text, String direction){
        return mRestService.translateText(ConstantManager.API_KEY, text, direction);
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public RealmManager getRealmManager() {
        return mRealmManager;
    }
}
