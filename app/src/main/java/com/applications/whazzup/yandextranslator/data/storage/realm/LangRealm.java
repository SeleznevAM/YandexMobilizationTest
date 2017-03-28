package com.applications.whazzup.yandextranslator.data.storage.realm;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LangRealm extends RealmObject {
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
}
