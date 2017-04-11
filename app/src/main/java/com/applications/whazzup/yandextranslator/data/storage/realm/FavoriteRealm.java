package com.applications.whazzup.yandextranslator.data.storage.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class FavoriteRealm extends RealmObject {
    @PrimaryKey
    private String id;
    private String originalText;
    private String translateText;
    private String direction;

    public FavoriteRealm() {
    }

    public FavoriteRealm(TranslateRealm translateRealm) {
        this.id = String.valueOf(this.hashCode());
        this.originalText = translateRealm.getOriginalText();
        this.translateText = translateRealm.getTranslateText();
        this.direction = translateRealm.getDirection();
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslateText() {
        return translateText;
    }

    public void setTranslateText(String translateText) {
        this.translateText = translateText;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
