package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;
import com.applications.whazzup.yandextranslator.mvp.views.ITranslateView;

import butterknife.BindView;
import butterknife.OnClick;


public class TranslateView extends AbstractView<TranslateScreen.Presenter> implements ITranslateView{

    @BindView(R.id.lang_txt)
    TextView mLangTxt;


    public TranslateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<TranslateScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public String getTranslateText() {
        return null;
    }


    @OnClick(R.id.lang_btn)
    void clickOnLangBtn(){
        mPresenter.clickOnLangBtn();
    }
}
