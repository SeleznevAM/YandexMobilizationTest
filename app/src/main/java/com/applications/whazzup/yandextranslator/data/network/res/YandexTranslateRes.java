package com.applications.whazzup.yandextranslator.data.network.res;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO перевода текста от сервера
 */
public class YandexTranslateRes {

    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("lang")
    @Expose
    public String lang;
    @SerializedName("text")
    @Expose
    public List<String> text = null;
}
