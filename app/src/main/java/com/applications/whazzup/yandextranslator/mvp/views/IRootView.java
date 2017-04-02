package com.applications.whazzup.yandextranslator.mvp.views;

import android.support.annotation.Nullable;

import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;

public interface IRootView extends IView {

    void showMessage (String message);
    void showError (Throwable e);


    void showLoad();
    void hideLoad();

    public void setLanTo(LangRealm language);

    public void setLanFrom(LangRealm language);

    @Nullable
    IView getCurrentScreen();
}
