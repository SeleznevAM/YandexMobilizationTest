package com.applications.whazzup.yandextranslator.di.modules;

import com.applications.whazzup.yandextranslator.di.scopes.RootScope;
import com.applications.whazzup.yandextranslator.mvp.models.RootModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class RootModule {

    @Provides
    @RootScope
    RootPresenter provideRootPresenter(){
        return RootPresenter.getInstance();
    }

    @Provides
    @RootScope
    RootModel provideRootModel(){
        return new RootModel();
    }
}

