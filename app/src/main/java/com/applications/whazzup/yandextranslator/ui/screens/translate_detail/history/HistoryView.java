package com.applications.whazzup.yandextranslator.ui.screens.translate_detail.history;

import android.content.Context;
import android.util.AttributeSet;

import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;

/**
 * Created by ЗавТер on 30.03.2017.
 */

public class HistoryView extends AbstractView<HistoryScreen.HistoryPresenter> {


    public HistoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<HistoryScreen.HistoryComponent>getDaggerComponent(context).inject(this);
    }
}
