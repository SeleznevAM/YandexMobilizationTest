package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.util.Log;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.TranslateScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.TranslateModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;

import javax.inject.Inject;

import dagger.Provides;
import mortar.MortarScope;

@Screen(R.layout.screen_translate)
public class TranslateScreen extends AbstractScreen<RootActivity.RootComponent> {
    public TranslateScreen() {
        Log.e("Screen", " Create");
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerTranslateScreen_Component.builder().rootComponent(parentComponent).module(new Module()).build();
    }

    // region================DI==============

    @dagger.Module
    public class Module{
        @Provides
        @TranslateScope
        TranslateModel provideTranslateModel(){
            return new TranslateModel();
        }

        @Provides
        @TranslateScope
        Presenter providePresenter(){
            return new Presenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @TranslateScope
    public interface Component{
        void inject(TranslateView view);
        void inject(Presenter presenter);

    }

    // endregion

    // region================Presenter==============

    public class Presenter extends AbstractPresenter<TranslateView, TranslateModel>{

        @Inject
        RootPresenter mRootPresenter;

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }
    }


    // endregion
}