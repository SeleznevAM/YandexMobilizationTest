package com.applications.whazzup.yandextranslator.ui.screens.history;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;
import com.applications.whazzup.yandextranslator.ui.screens.history.HistoryAdapter;
import com.applications.whazzup.yandextranslator.ui.screens.history.HistoryScreen;

import butterknife.BindView;


public class HistoryView extends AbstractView<HistoryScreen.HistoryPresenter> {

    @BindView(R.id.history_recycler)
    RecyclerView mRecyclerView;


    HistoryAdapter mAdapter;


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
        mAdapter = new HistoryAdapter(new HistoryAdapter.onHistoryClickListener() {
            @Override
            public void onHistoryItemClick(int position, View v) {
                if(((CheckBox) v).isChecked()){
                    mPresenter.clickFavorite(mAdapter.getTranslateFromPostition(position));
                }else{
                    mPresenter.deleteFromFavorite(mAdapter.getTranslateFromPostition(position));
                }
            }
        });
    }

    public HistoryAdapter getAdapter() {
        return mAdapter;
    }

    public void initView(){
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Вы уверены, что хотите очистить историю?");
        builder.setCancelable(true);
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.clearHistory();
            }
        });
        builder.show();
    }

}
