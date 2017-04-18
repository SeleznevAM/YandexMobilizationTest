package com.applications.whazzup.yandextranslator.ui.screens.favorite;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.mvp.views.AbstractView;

import butterknife.BindView;

public class FavoriteView extends AbstractView<FavoriteScreen.FavoritePresenter> {

    @BindView(R.id.favorite_recycler)
    RecyclerView mFavoriteRecycler;

    FavoriteAdapter mAdapter;

    public FavoriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new FavoriteAdapter(new FavoriteAdapter.CustomClickListener() {
            @Override
            public void clickOnFavorite(int position) {

                mPresenter.clickFavorite(mAdapter.getFavoriteFromPosition(position));
                mAdapter.deleteItem(position);
            }
        });
    }

    @Override
    public boolean viewOnBackPressed() {
        return true;
    }

    public FavoriteAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<FavoriteScreen.FavoriteComponent>getDaggerComponent(context).inject(this);
    }

    public void initView(){
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mFavoriteRecycler.setLayoutManager(llm);
        mFavoriteRecycler.setAdapter(mAdapter);
    }

    public void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Вы уверены, что хотите очистить избранное?");
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
