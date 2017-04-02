package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.network.res.YandexLangRes;
import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.TranslateScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.TranslateModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;
import com.applications.whazzup.yandextranslator.ui.screens.language.LanguageScreen;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        RootPresenter getRootPresenter();

    }

    // endregion

    // region================Presenter==============

    public class Presenter extends AbstractPresenter<TranslateView, TranslateModel>{

        @Inject
        RootPresenter mRootPresenter;


        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
           // mModel.getAllLang();

            initActionBar();

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        public void clickOnLangBtn() {
            //Flow.get(getView()).set(new LanguageScreen(0));
            //mRootPresenter.getRootView().showMessage(mRootPresenter.getLanguageCodeFrom() + " - " + mRootPresenter.getLanguageCodeTo());
           Call<YandexTranslateRes> call = mModel.translateText(getView().getTranslateText(), mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo());
            call.enqueue(new Callback<YandexTranslateRes>() {
                @Override
                public void onResponse(Call<YandexTranslateRes> call, Response<YandexTranslateRes> response) {
                    getView().setTranslateTest(response.body().text.toString());
                }

                @Override
                public void onFailure(Call<YandexTranslateRes> call, Throwable t) {

                }
            });
        }

        private void initActionBar(){
            mRootPresenter.newActionBarBuilder().setDirectionVisible(true).setBackArrow(false).build();
            mRootPresenter.getRootView().setLanTo(mModel.getLangByCode(mRootPresenter.getLanguageCodeTo()));
           mRootPresenter.getRootView().setLanFrom(mModel.getLangByCode(mRootPresenter.getLanguageCodeFrom()));
        }
    }


    // endregion
}
