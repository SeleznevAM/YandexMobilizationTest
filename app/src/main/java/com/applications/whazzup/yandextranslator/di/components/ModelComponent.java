package com.applications.whazzup.yandextranslator.di.components;

import com.applications.whazzup.yandextranslator.di.modules.ModelModule;
import com.applications.whazzup.yandextranslator.mvp.models.AbstractModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel model);
}
