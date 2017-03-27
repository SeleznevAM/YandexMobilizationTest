package com.applications.whazzup.yandextranslator.di.components;


import android.content.Context;

import com.applications.whazzup.yandextranslator.di.modules.AppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
