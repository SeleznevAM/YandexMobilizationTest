package com.applications.whazzup.yandextranslator.ui.screens.language;

import android.content.Context;
import android.util.AttributeSet;

import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;


public class LanguageView extends AbstractView<LanguageScreen.LabguagePresenter> {


    public LanguageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<LanguageScreen.Component>getDaggerComponent(context).inject(this);
    }
}
