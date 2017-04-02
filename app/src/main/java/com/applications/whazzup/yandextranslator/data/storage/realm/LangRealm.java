package com.applications.whazzup.yandextranslator.data.storage.realm;


import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LangRealm extends RealmObject implements Comparable {
    @PrimaryKey
    private String id;

    private String lang;

    public LangRealm() {
    }

    public LangRealm(String id, String lang) {
        this.id = id;
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        return lang.compareTo(((LangRealm) o).getLang());
    }


}
