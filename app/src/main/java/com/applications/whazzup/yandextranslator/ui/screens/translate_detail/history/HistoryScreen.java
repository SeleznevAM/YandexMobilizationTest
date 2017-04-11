package com.applications.whazzup.yandextranslator.ui.screens.translate_detail.history;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.FavoriteRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.HistoryScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.HistoryModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.mvp.presenters.MenuItemHolder;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;
import com.applications.whazzup.yandextranslator.ui.screens.translate_detail.DetailScreen;

import dagger.Provides;
import io.realm.Realm;
import mortar.MortarScope;
import rx.Subscriber;

@Screen(R.layout.screen_history)
public class HistoryScreen extends AbstractScreen<RootActivity.RootComponent> {
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerHistoryScreen_HistoryComponent.builder().rootComponent(parentComponent).historyModule(new HistoryModule()).build();
    }

    //region===============================DI==========================

    @dagger.Module
    public class HistoryModule{
        @Provides
        @HistoryScope
        HistoryPresenter provideHistoryPresenter(){
            return new HistoryPresenter();
        }

        @Provides
        @HistoryScope
        HistoryModel provideHistoryModel(){
            return new HistoryModel();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = HistoryModule.class)
    @HistoryScope
    public interface HistoryComponent{

        void inject(HistoryPresenter presenter);
        void inject(HistoryView view);
    }


    //endregion

    //region===============================Presenter==========================

    public class HistoryPresenter extends AbstractPresenter<HistoryView, HistoryModel>{

        @Override
        protected void initDagger(MortarScope scope) {
            ((HistoryComponent) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            initActionBar();
            mRootPresenter.getRootView().updateBottomBarState(R.id.navigation_history);
            mModel.getTranslateHistory().subscribe(new Subscriber<TranslateRealm>() {
                @Override
                public void onCompleted() {
                    getView().initView();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(final TranslateRealm translateRealm) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if(realm.where(FavoriteRealm.class).equalTo("originalText", translateRealm.getOriginalText()).equalTo("translateText", translateRealm.getTranslateText()).findFirst()!=null)
                                translateRealm.setFavorite(true);
                        }
                    });
                    getView().getAdapter().addItem(translateRealm);
                }
            });
        }


        public void clickFavorite(final TranslateRealm translateFromPosition) {

            mModel.saveTranslateToFavorite(translateFromPosition);

          /*  Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    translateFromPosition.changeFavorite();
                }
            });
            realm.close();*/
        }

        private void initActionBar(){
            mRootPresenter.newActionBarBuilder().setDirectionVisible(false).addAction(new MenuItemHolder("История", R.drawable.ic_delete_black_24dp, new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    getView().showAlertDialog();

                    return false;
                }
            })).build();
        }

        public void deleteFromFavorite(TranslateRealm translateFromPostition) {
            mModel.deletetTranslateFromFavorite(translateFromPostition);
        }

        public void clearHistory(){
             mModel.deleteAllHistory();
            getView().getAdapter().clearHistory();
        }
    }


    //endregion


}
