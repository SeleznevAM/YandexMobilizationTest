package com.applications.whazzup.yandextranslator.data.network;


import com.applications.whazzup.yandextranslator.data.network.res.YandexLangRes;
import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestService {

    @POST("getLangs")
    Call<YandexLangRes> getAllLang(@Query("key") String apiKey, @Query("ui") String ui);

    @FormUrlEncoded
    @POST("translate")
    Call<YandexTranslateRes> translateText(@Field("key") String apiKey, @Field("text") String text, @Field("lang") String direct);
}
