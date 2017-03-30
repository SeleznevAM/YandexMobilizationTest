package com.applications.whazzup.yandextranslator.data.managers;


import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.data.network.RestService;
import com.applications.whazzup.yandextranslator.data.network.res.YandexLangRes;
import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;
import com.applications.whazzup.yandextranslator.di.components.DaggerDataManagerComponent;
import com.applications.whazzup.yandextranslator.di.modules.NetworkModule;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {

    private static DataManager ourInstance = null;

    @Inject
    RestService mRestService;

    RealmManager mRealmManager = new RealmManager();

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }


    private DataManager() {
        DaggerDataManagerComponent.builder().appComponent(App.getAppComponent()).networkModule(new NetworkModule()).build().inject(this);
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

    public void translateText(String text){
        Call<YandexTranslateRes> call = mRestService.translateText("trnsl.1.1.20170315T090000Z.1bbd09f1e8dede27.4aad6b1dded1e6519341fbcfd427ebea820535bd", "Привет", "ru-en");
        call.enqueue(new Callback<YandexTranslateRes>() {
            @Override
            public void onResponse(Call<YandexTranslateRes> call, Response<YandexTranslateRes> response) {
                System.out.println(response.body().text);
            }

            @Override
            public void onFailure(Call<YandexTranslateRes> call, Throwable t) {

            }
        });
    }
}
