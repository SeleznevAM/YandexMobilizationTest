package com.applications.whazzup.yandextranslator.ui.screens.language;


import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.LanguageScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.mvp.models.LanguageModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;

import dagger.Provides;
import mortar.MortarScope;

public class LanguageScreen extends AbstractScreen<TranslateScreen.Component> {


    @Override
    public Object createScreenComponent(TranslateScreen.Component parentComponent) {
        return DaggerLanguageScreen_Component.builder().component(parentComponent).module(new Module()).build();
    }

    //region===============================DI==========================

    @dagger.Module
    public class Module{

        @Provides
        @LanguageScope
        LanguageModel provideLanguageModel(){
            return new LanguageModel();
        }

        @Provides
        @LanguageScope
        LabguagePresenter providePresenter(){
            return new LabguagePresenter();
        }
    }

    @dagger.Component(dependencies = TranslateScreen.Component.class, modules = Module.class)
    @LanguageScope
    public interface Component{

        void inject(LanguageView view);
        void inject(LabguagePresenter presenter);
    }



    //endregion

    //region===============================Presenter==========================

    class LabguagePresenter extends AbstractPresenter<LanguageView, LanguageModel>{

        @Override
        protected void initDagger(MortarScope scope) {
            ((LanguageScreen.Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }
    }

    //endregion

}
