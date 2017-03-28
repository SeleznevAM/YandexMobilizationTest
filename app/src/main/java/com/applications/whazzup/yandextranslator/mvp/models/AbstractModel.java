package com.applications.whazzup.yandextranslator.mvp.models;


import com.applications.whazzup.yandextranslator.data.managers.DataManager;
import com.applications.whazzup.yandextranslator.di.components.DaggerModelComponent;
import com.applications.whazzup.yandextranslator.di.modules.ModelModule;

import javax.inject.Inject;

public class AbstractModel {
    @Inject
    DataManager mDataManager;

    public AbstractModel() {
        DaggerModelComponent.builder().modelModule(new ModelModule()).build().inject(this);
    }
}
