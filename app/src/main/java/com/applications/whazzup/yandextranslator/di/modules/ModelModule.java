package com.applications.whazzup.yandextranslator.di.modules;


import com.applications.whazzup.yandextranslator.data.managers.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    @Singleton
    DataManager provideDataManager() {
        return DataManager.getInstance();
    }
}
