package com.applications.whazzup.yandextranslator.di.components;


import com.applications.whazzup.yandextranslator.data.managers.DataManager;
import com.applications.whazzup.yandextranslator.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = NetworkModule.class)
@Singleton
public interface DataManagerComponent {
    void inject (DataManager manager);
}
