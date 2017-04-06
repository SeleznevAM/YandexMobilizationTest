package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;
import com.applications.whazzup.yandextranslator.mvp.views.ITranslateView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


public class TranslateView extends AbstractView<TranslateScreen.Presenter> implements ITranslateView{

    @BindView(R.id.lang_txt)
    TextView mLangTxt;
    @BindView(R.id.translate_text_et)
    EditText mTranslateText;


    public TranslateView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTranslateText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    Toast.makeText(getContext(), mTranslateText.getText().toString(), Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
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
        return mTranslateText.getText().toString();
    }


    @OnClick(R.id.lang_btn)
    void clickOnLangBtn(){
        mPresenter.clickOnLangBtn();
    }

    public void setTranslateTest(String text){
        mLangTxt.setText(text);
    }

    @OnClick(R.id.clear_btn)
    void onClick(){
        mTranslateText.setText("");
    }


}
