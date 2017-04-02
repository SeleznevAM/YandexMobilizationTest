package com.applications.whazzup.yandextranslator.ui.screens.language;


import android.os.Bundle;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.managers.RealmManager;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.LanguageScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.LanguageModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dagger.Provides;
import flow.Flow;
import flow.TreeKey;
import mortar.MortarScope;

@Screen(R.layout.screen_language)
public class LanguageScreen extends AbstractScreen<TranslateScreen.Component> implements TreeKey {

    RealmManager mRealmManager = new RealmManager();


    int direction;

    public LanguageScreen(int i) {
        this.direction = i;
    }

    @Override
    public Object createScreenComponent(TranslateScreen.Component parentComponent) {
        return DaggerLanguageScreen_Component.builder().component(parentComponent).module(new Module()).build();
    }

    @Override
    public Object getParentKey() {
        return new TranslateScreen();
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
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView();

            for(LangRealm  r : mRealmManager.getAllLang()){
                getView().getAdapter().addItem(r);
            }

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((LanguageScreen.Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        public void selectLang(LangRealm language) {
            if(direction==1){
               getRootView().setLanFrom(language);
                mRootPresenter.setLanguageCodeFrom(language.getId());
                Flow.get(getView()).goBack();
            }else if(direction==0){
               getRootView().setLanTo(language);
                mRootPresenter.setLanguageCodeTo(language.getId());
                Flow.get(getView()).goBack();
            }
        }
    }

    //endregion

}
