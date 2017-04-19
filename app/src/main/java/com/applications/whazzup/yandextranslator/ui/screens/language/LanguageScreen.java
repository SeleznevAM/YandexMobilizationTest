package com.applications.whazzup.yandextranslator.ui.screens.language;


import android.os.Bundle;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.LanguageScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.LanguageModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;

import dagger.Provides;
import flow.Flow;
import flow.TreeKey;
import mortar.MortarScope;

@Screen(R.layout.screen_language)
public class LanguageScreen extends AbstractScreen<TranslateScreen.Component> implements TreeKey {

    //Переменная для отслеживания направления перевода
    private int direction;

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
        LanguagePresenter providePresenter(){
            return new LanguagePresenter();
        }
    }

    @dagger.Component(dependencies = TranslateScreen.Component.class, modules = Module.class)
    @LanguageScope
    public interface Component{

        void inject(LanguageView view);
        void inject(LanguagePresenter presenter);
    }



    //endregion

    //region===============================Presenter==========================

    class LanguagePresenter extends AbstractPresenter<LanguageView, LanguageModel>{

        @Override
        protected void initDagger(MortarScope scope) {
            ((LanguageScreen.Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView();
            fillLanguageAdapter();
            initActionBar();
            mRootPresenter.getRootView().setBottomNavigationViewVisibility(false);

        }

        private void initActionBar() {
          mRootPresenter.newActionBarBuilder().setBackArrow(true).setTitle("Язык перевода").build();

        }

        private void fillLanguageAdapter(){
            for(LangRealm  r : mModel.getAllLang()){
                getView().getAdapter().addItem(r);
            }
        }


        void selectLang(LangRealm language) {
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
