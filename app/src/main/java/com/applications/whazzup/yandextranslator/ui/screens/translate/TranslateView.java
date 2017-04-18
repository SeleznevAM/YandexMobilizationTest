package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;
import com.applications.whazzup.yandextranslator.mvp.views.ITranslateView;
import com.applications.whazzup.yandextranslator.utils.NetworkStatusChecker;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


public class TranslateView extends AbstractView<TranslateScreen.Presenter> implements ITranslateView {

    @BindView(R.id.translate_txt)
    TextView mLangTxt;
    @BindView(R.id.original_text_et)
    EditText mTranslateText;
    @BindView(R.id.translate_screen_favorite_chb)
    CheckBox mChangeFavorite;


    public TranslateView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTranslateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private Timer timer = new Timer();
            private final long DELAY = 1500; // milliseconds

            @Override
            public void afterTextChanged(Editable s) {
                if (!(String.valueOf(s).isEmpty())) {
                    final Handler handler = new Handler();
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(NetworkStatusChecker.isNetworkAvalible(getContext())) {
                                                mChangeFavorite.setVisibility(GONE);
                                                mPresenter.translateText();
                                            }else{
                                                Toast.makeText(getContext(), R.string.network_not_available_string, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            },
                            DELAY
                    );
                }else{
                    mLangTxt.setText("");
                    mChangeFavorite.setVisibility(GONE);
                }
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
    public String getOriginalText() {
        return mTranslateText.getText().toString();
    }

    public String getTranslateText(){
        return mLangTxt.getText().toString();
    }

    public boolean isFavorite(){
        return mChangeFavorite.isChecked();
    }

    public void setTranslateTest(String text) {
        mLangTxt.setText(text);
        mChangeFavorite.setVisibility(VISIBLE);
        mChangeFavorite.setChecked(mPresenter.checkFavorite());
    }

    public void setTranslateTest(String text, boolean isFavorite) {
        mLangTxt.setText(text);
        mChangeFavorite.setVisibility(VISIBLE);
        mChangeFavorite.setChecked(isFavorite);
    }

    //region===============================Events==========================

    @OnClick(R.id.clear_btn)
    void onClick() {
        mTranslateText.setText("");
        mLangTxt.setText("");
        mChangeFavorite.setVisibility(GONE);
    }

    @OnClick(R.id.translate_screen_favorite_chb)
    void onFavoriteClick(){
        if(mChangeFavorite.isChecked()){
            mPresenter.saveFavorite();
        }else{
            mPresenter.deleteFromFavorite();
        }
    }



    public void initView(TranslateRealm translateRealm) {
        if(!(translateRealm.getTranslateText().isEmpty())){
        mTranslateText.setText(translateRealm.getOriginalText());
        mLangTxt.setText(translateRealm.getTranslateText());
        mChangeFavorite.setVisibility(VISIBLE);
        mChangeFavorite.setChecked(mPresenter.checkFavorite());
        }
    }

    //endregion
public TranslateScreen.Presenter getPresenter(){
        return mPresenter;
    }

}
