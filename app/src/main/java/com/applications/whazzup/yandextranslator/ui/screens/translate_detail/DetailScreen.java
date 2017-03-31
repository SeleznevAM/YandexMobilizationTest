package com.applications.whazzup.yandextranslator.ui.screens.translate_detail;


import android.os.Bundle;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mvp.models.DetailModel;
import com.applications.whazzup.yandextranslator.mvp.presenters.AbstractPresenter;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;

import dagger.Provides;
import mortar.MortarScope;

@Screen(R.layout.screen_detail)
public class DetailScreen extends AbstractScreen<RootActivity.RootComponent> {




    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerDetailScreen_DetailComponent.builder().rootComponent(parentComponent).detailModule(new DetailModule()).build();
    }

    //region===============================DI==========================

    @dagger.Module
    public class DetailModule{
        @Provides
        DetailPresenter provideDetailPresenter(){
            return new DetailPresenter();
        }

        @Provides
        DetailModel provideDetailModel(){
            return new DetailModel();
        }
        }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = DetailModule.class)
    public interface DetailComponent{
        void inject(DetailPresenter presenter);
        void inject(DetailView view);

        RootPresenter getRootPresenter();
    }


    //endregion

    //region===============================Presenter==========================

    public class DetailPresenter extends AbstractPresenter<DetailView, DetailModel>{

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView();
            initAppBar();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((DetailComponent) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        private void initAppBar(){
            mRootPresenter.newActionBarBuilder().setDirectionVisible(false).setBackArrow(true).setTab(getView().getViewPager()).build();
        }
    }

    //endregion


}
