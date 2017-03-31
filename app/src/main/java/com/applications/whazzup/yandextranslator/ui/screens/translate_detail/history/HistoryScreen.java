package com.applications.whazzup.yandextranslator.ui.screens.translate_detail.history;


import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.scopes.HistoryScope;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.HistoryModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;
import com.applications.whazzup.yandextranslator.ui.screens.translate_detail.DetailScreen;

import dagger.Provides;
import mortar.MortarScope;
@Screen(R.layout.screen_history)
public class HistoryScreen extends AbstractScreen<DetailScreen.DetailComponent> {
    @Override
    public Object createScreenComponent(DetailScreen.DetailComponent parentComponent) {
        return DaggerHistoryScreen_HistoryComponent.builder().detailComponent(parentComponent).historyModule(new HistoryModule()).build();
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

    @dagger.Component(dependencies = DetailScreen.DetailComponent.class, modules = HistoryModule.class)
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
    }

    //endregion


}
