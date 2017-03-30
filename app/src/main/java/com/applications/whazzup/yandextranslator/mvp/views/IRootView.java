package com.applications.whazzup.yandextranslator.mvp.views;

import android.support.annotation.Nullable;

public interface IRootView extends IView {

    void showMessage (String message);
    void showError (Throwable e);

    void showLoad();
    void hideLoad();

    public void setLanTo(String lang);

    public void setLanFrom( String lang);

    @Nullable
    IView getCurrentScreen();
}
