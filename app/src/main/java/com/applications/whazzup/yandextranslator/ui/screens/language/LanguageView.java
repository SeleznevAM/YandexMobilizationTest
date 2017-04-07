package com.applications.whazzup.yandextranslator.ui.screens.language;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;

import butterknife.BindView;


public class LanguageView extends AbstractView<LanguageScreen.LanguagePresenter> {

    @BindView(R.id.recycler)
            RecyclerView mRecyclerView;

    LanguageAdapter mAdapter;

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

    public void initView(){
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mAdapter = new LanguageAdapter(new LanguageAdapter.CustomClickListener() {
            @Override
            public void onLanguageClickListener(int position) {
                mPresenter.selectLang(mAdapter.getLangFromPosition(position));
            }
        });
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);

    }

    public LanguageAdapter getAdapter() {
        return mAdapter;
    }
}
