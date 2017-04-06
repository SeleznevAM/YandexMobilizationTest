package com.applications.whazzup.yandextranslator.ui.screens.translate_detail.favorite;

import android.os.Bundle;
import android.view.MenuItem;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.FavoriteRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.FavoriteScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.FavoriteModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;

import com.applications.whazzup.yandextranslator.mvp.presenters.MenuItemHolder;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;

import dagger.Provides;
import io.realm.Realm;
import mortar.MortarScope;

@Screen(R.layout.screen_favorite)
public class FavoriteScreen extends AbstractScreen<RootActivity.RootComponent> {

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerFavoriteScreen_FavoriteComponent.builder().rootComponent(parentComponent).favoriteModule(new FavoriteModule()).build();
    }

    //region===============================DI==========================

    @dagger.Module
    public class FavoriteModule{

        @Provides
        @FavoriteScope
        FavoritePresenter provideFavoritePresenter(){
            return new FavoritePresenter();
        }

        @Provides
        @FavoriteScope
        FavoriteModel provideFavoriteModel(){
            return new FavoriteModel();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = FavoriteModule.class)
    @FavoriteScope
    public interface FavoriteComponent{
        void inject (FavoritePresenter presenter);
        void inject (FavoriteView view);
    }


    //endregion

    //region===============================Presenter==========================

    public class FavoritePresenter extends AbstractPresenter<FavoriteView, FavoriteModel>{


        public FavoritePresenter() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((FavoriteComponent) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            saveFavoriteToAdapter();
            initActionBar();
            getView().initView();
        }

        public void saveFavoriteToAdapter(){
            for(FavoriteRealm realm : mModel.getFavorite()){
                getView().getAdapter().addItem(realm);
            }
        }

        private void initActionBar(){
            mRootPresenter.newActionBarBuilder().setDirectionVisible(false).addAction(new MenuItemHolder("Избранное", R.drawable.ic_delete_black_24dp, new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    getView().showAlertDialog();
                    return false;
                }
            })).build();
        }

        public void clickFavorite(final FavoriteRealm favoriteFromPosition) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    TranslateRealm translateRealm = realm.where(TranslateRealm.class).equalTo("originalText", favoriteFromPosition.getOriginalText()).equalTo("translateText", favoriteFromPosition.getTranslateText()).findFirst();
                    if(translateRealm!=null){
                        translateRealm.changeFavorite();
                        favoriteFromPosition.deleteFromRealm();
                    }else{
                        favoriteFromPosition.deleteFromRealm();
                    }

                }
            });
            realm.close();
        }

        public void clearHistory(){
            mModel.clearHistory();
            getView().getAdapter().clearFavorite();
        }
    }



    //endregion


}
