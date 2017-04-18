package com.applications.whazzup.yandextranslator.mvp.views;

import android.support.annotation.Nullable;

import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;

public interface IRootView extends IView {

    void showMessage(String message);

    void showError(Throwable e);

    void setBottomNavigationViewVisibility(boolean isVisible);

    void setLanTo(LangRealm language);

    void setLanFrom(LangRealm language);

    @Nullable
    IView getCurrentScreen();

    void updateBottomBarState(int actionId);
}
