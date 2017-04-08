package com.applications.whazzup.yandextranslator.ui.screens.translate;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;
import com.applications.whazzup.yandextranslator.mvp.views.ITranslateView;
import com.applications.whazzup.yandextranslator.utils.NetworkStatusChecker;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


public class TranslateView extends AbstractView<TranslateScreen.Presenter> implements ITranslateView {

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
                                                mPresenter.clickOnLangBtn();
                                            }else{
                                                Toast.makeText(getContext(), R.string.network_not_available_string, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    // TODO: do what you need here (refresh list)
                                    // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                }
                            },
                            DELAY
                    );
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
    public String getTranslateText() {
        return mTranslateText.getText().toString();
    }

    public void setTranslateTest(String text) {
        mLangTxt.setText(text);
    }

    //region===============================Events==========================

    @OnClick(R.id.clear_btn)
    void onClick() {
        mTranslateText.setText("");
    }

    //endregion

}
