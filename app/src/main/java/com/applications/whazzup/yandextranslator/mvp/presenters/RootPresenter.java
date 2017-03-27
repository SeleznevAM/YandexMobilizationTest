package com.applications.whazzup.yandextranslator.mvp.presenters;


import android.support.annotation.Nullable;

import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.mvp.views.IRootView;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;

import mortar.Presenter;
import mortar.bundler.BundleService;

public class RootPresenter extends Presenter<IRootView> {

    public RootPresenter() {
        App.getRootActivityComponent().inject(this);
    }

    @Nullable
    public IRootView getRootView() {
        return getView();
    }

    @Override
    protected BundleService extractBundleService(IRootView view) {
        return BundleService.getBundleService((RootActivity)view);
    }


}
