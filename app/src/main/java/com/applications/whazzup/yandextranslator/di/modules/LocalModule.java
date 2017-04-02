package com.applications.whazzup.yandextranslator.di.modules;


import android.content.Context;

import com.applications.whazzup.yandextranslator.data.managers.PreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule {
    @Provides
    @Singleton
    PreferencesManager providePreferencesManager(Context context){
        return new PreferencesManager(context);
    }
}
