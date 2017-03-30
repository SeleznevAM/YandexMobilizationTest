package com.applications.whazzup.yandextranslator.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class YandexLangRes {
    @SerializedName("dirs")
    @Expose
    public List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    public HashMap<String, String> langs = new HashMap<>();
}
