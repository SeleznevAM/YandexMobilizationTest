package com.applications.whazzup.yandextranslator.data.storage.realm;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TranslateRealm extends RealmObject {


    private String originalText;
    private String translateText;
    private String direction;
    private boolean isFavorite;

    public TranslateRealm() {

    }

    public TranslateRealm(String originalText, String translateText, String direction, boolean isFavorite) {
        this.originalText = originalText;
        this.translateText = translateText;
        this.direction = direction;
        this.isFavorite = isFavorite;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getTranslateText() {
        return translateText;
    }

    public String getDirection() {
        return direction;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
