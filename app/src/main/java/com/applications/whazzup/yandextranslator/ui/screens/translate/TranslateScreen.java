package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.os.Bundle;

import android.util.Log;

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

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            initActionBar();
            mRootPresenter.getRootView().updateBottomBarState(R.id.navigation_home);
            Realm realm = Realm.getDefaultInstance();
            TranslateRealm translateRealm = realm.where(TranslateRealm.class).equalTo("id", mModel.loadTranslateByHash()).findFirst();
            if(translateRealm!=null){
            getView().initView(translateRealm);
            }
            mRootPresenter.getRootView().setBottomNavigationViewVisibility(true);
        }

        @Override
        protected void onExitScope() {
            super.onExitScope();
            Log.e("OnExitScope", "ON_EXIT_SCOPE");
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
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
                        switch (response.code()){
                            case 200:
                                mModel.saveTranslateInHistory(getView().getOriginalText(), response.body().text.get(0), direction, false);
                                mRootPresenter.setLastDirection(direction);
                                if(response.body().text.get(0)!=null) {
                                    getView().setTranslateTest(response.body().text.get(0), checkFavorite());
                                }
                                break;
                            case 404:
                                mRootPresenter.getRootView().showMessage("Превышено суточное ограничение на объем переведенного текста");
                                break;
                            case 413:
                                mRootPresenter.getRootView().showMessage("Превышен максимально допустимый размер текста");
                                break;
                            case 422:
                                mRootPresenter.getRootView().showMessage("Текст не может быть переведен");
                                break;
                            default:
                                if(getView().getOriginalText().length()>0){
                                    mRootPresenter.getRootView().showMessage("Что то пошло не так повторите попытку пойзже.");
                                }

                        }
                    }

                    @Override
                    public void onFailure(Call<YandexTranslateRes> call, Throwable t) {
                        getRootView().showError(t);
                    }
                });
            }else{
                mRootPresenter.setLastDirection(direction);
                getView().setTranslateTest(realm.getTranslateText(), checkFavorite());
            }
        }

        boolean checkFavorite(){
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            if(direction.equals(mRootPresenter.getLastDirection())){
            return mModel.checkFavorite(getView().getOriginalText().trim(), mRootPresenter.getLastDirection());
            }else{
                return mModel.checkFavorite(getView().getOriginalText().trim(), direction);
            }
        }

        private void initActionBar(){
            mRootPresenter.newActionBarBuilder().setDirectionVisible(true).setBackArrow(false).build();
            mRootPresenter.getRootView().setLanTo(mModel.getLangByCode(mRootPresenter.getLanguageCodeTo()));
           mRootPresenter.getRootView().setLanFrom(mModel.getLangByCode(mRootPresenter.getLanguageCodeFrom()));
        }

        void saveFavorite() {
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            TranslateRealm realm;
            if(direction.equals(mRootPresenter.getLastDirection())){
                realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(),direction);
            }else{
                realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(), mRootPresenter.getLastDirection());
            }
            mModel.saveTranslateToFavorite(realm);
        }

        void deleteFromFavorite() {
            final String direction = mRootPresenter.getLanguageCodeFrom() + "-" + mRootPresenter.getLanguageCodeTo();
            TranslateRealm realm;
            if(direction.equals(mRootPresenter.getLastDirection())){
                realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(),direction);
            }else{
                realm = mModel.getTranslateRealmFromDb(getView().getOriginalText().trim(), mRootPresenter.getLastDirection());
            }
            mModel.deleteTranslateFromFavorite(realm);
        }
    }


    // endregion
}
