package com.applications.whazzup.yandextranslator.ui.screens.translate_detail;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;

import butterknife.BindView;


class DetailView extends AbstractView<DetailScreen.DetailPresenter> {

    @BindView(R.id.container)
    ViewPager mViewPager;


    public DetailView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<DetailScreen.DetailComponent>getDaggerComponent(context).inject(this);
    }

    public void initView(){
        DetailAdpater adapter = new DetailAdpater();
        mViewPager.setAdapter(adapter);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}
