package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;

import com.applications.whazzup.yandextranslator.data.network.res.YandexTranslateRes;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;
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

import io.realm.Realm;
import mortar.MortarScope;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Screen(R.layout.screen_translate)
public class TranslateScreen extends AbstractScreen<RootActivity.RootComponent> {

    public TranslateScreen() {
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
        //final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            initActionBar();
            mRootPresenter.getRootView().updateBottomBarState(R.id.navigation_home);
            Realm realm = Realm.getDefaultInstance();
            //TranslateRealm translateRealm =  mModel.getTranslateRealmFromSharedPreferneces();
            TranslateRealm translateRealm = realm.where(TranslateRealm.class).equalTo("id", mModel.loadTranslateByHash()).findFirst();
            if(translateRealm!=null){
            getView().initView(translateRealm);
            }
        }

        @Override
        protected void onExitScope() {
            super.onExitScope();
            Log.e("OnExitScope", "ON_EXIT_SCOPE");
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            //mModel.saveTranslateRealmToSharedPrefernces(new TranslateRealm(getView().getOriginalText(), getView().getTranslateText(), direction, getView().isFavorite()));
            if(getView()!=null) {
                mModel.saveTranslateHash(mModel.getTranslateRealmFromDb(getView().getOriginalText(), direction));
            }
        }


        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        public void translateText() {
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            TranslateRealm realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(), direction);

            if (realm == null) {
                Call<YandexTranslateRes> call = mModel.translateText(getView().getOriginalText().trim(), direction);
                call.enqueue(new Callback<YandexTranslateRes>() {
                    @Override
                    public void onResponse(Call<YandexTranslateRes> call, Response<YandexTranslateRes> response) {

                        if(response.code()==200){
                        mModel.saveTransletInHistory(getView().getOriginalText(), response.body().text.get(0), direction, false);

                        getView().setTranslateTest(response.body().text.get(0), false);
                        }
                        else{
                            Toast.makeText(getView().getContext(), "Что то пошло не так, повторите запрос", Toast.LENGTH_LONG).show();
                    }
                    }

                    @Override
                    public void onFailure(Call<YandexTranslateRes> call, Throwable t) {
                        getRootView().showError(t);
                    }
                });
            }else{
                getView().setTranslateTest(realm.getTranslateText(), realm.isFavorite());
            }
        }

        public boolean checkFavorite(){
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            return mModel.checkFavorite(getView().getOriginalText().trim(), direction);
        }

        private void initActionBar(){
            mRootPresenter.newActionBarBuilder().setDirectionVisible(true).setBackArrow(false).build();
            mRootPresenter.getRootView().setLanTo(mModel.getLangByCode(mRootPresenter.getLanguageCodeTo()));
           mRootPresenter.getRootView().setLanFrom(mModel.getLangByCode(mRootPresenter.getLanguageCodeFrom()));
        }

        public void saveFavorite() {
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            TranslateRealm realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(), direction);
            mModel.saveTranslateToFavorite(realm);
        }

        public void deleteFromFavorite() {
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            TranslateRealm realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(), direction);
            mModel.deleteTranslateFromFavorite(realm);
        }
    }


    // endregion
}
