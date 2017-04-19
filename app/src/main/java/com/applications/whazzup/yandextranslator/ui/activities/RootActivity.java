package com.applications.whazzup.yandextranslator.ui.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.components.AppComponent;
import com.applications.whazzup.yandextranslator.di.modules.RootModule;
import com.applications.whazzup.yandextranslator.di.scopes.RootScope;
import com.applications.whazzup.yandextranslator.flow.TreeKeyDispatcher;
import com.applications.whazzup.yandextranslator.mvp.presenters.MenuItemHolder;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;
import com.applications.whazzup.yandextranslator.mvp.views.IActionBarView;
import com.applications.whazzup.yandextranslator.mvp.views.IRootView;
import com.applications.whazzup.yandextranslator.mvp.views.IView;
import com.applications.whazzup.yandextranslator.ui.screens.language.LanguageScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateView;
import com.applications.whazzup.yandextranslator.ui.screens.favorite.FavoriteScreen;
import com.applications.whazzup.yandextranslator.ui.screens.history.HistoryScreen;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;


public class RootActivity extends AppCompatActivity implements IRootView, BottomNavigationView.OnNavigationItemSelectedListener, IActionBarView {


    private static final int FROM = 0;
    private static final int TO = 1;


    @BindView(R.id.root_frame)
    FrameLayout mRootFrame;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.translate_from)
    TextView mTranslateFrom;

    @BindView(R.id.translate_to)
    TextView mTranslateTo;

    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.direction_wrapper)
    LinearLayout mDirectionWrapper;

    @BindView(R.id.root_container)
    CoordinatorLayout mRootContainer;

    ActionBar mActionBar;
    private List<MenuItemHolder> mActionBarMenuItems;


    @Inject
    RootPresenter mRootPresenter;


    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Flow.configure(newBase, this)
                .defaultKey(new TranslateScreen())
                .dispatcher(new TreeKeyDispatcher(this))
                .install();
        super.attachBaseContext(newBase);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        MortarScope rootActivityScope = MortarScope.findChild(getApplicationContext(), RootActivity.class.getName());
        return rootActivityScope.hasService(name) ? rootActivityScope.getService(name) : super.getSystemService(name);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        ButterKnife.bind(this);
        RootComponent rootComponent = DaggerService.getDaggerComponent(this);
        rootComponent.inject(this);


        initToolBar();
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch (v.getId()){
                    default: break;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
       if(getCurrentScreen() instanceof TranslateView){
           mRootPresenter.saveTranslateHash();
       }
    }

    public void updateBottomBarState(int actionId) {
        Menu menu = navigation.getMenu();

        for (int index = 0, size = menu.size(); index < size; index++) {
            MenuItem item = menu.getItem(index);
            if (item.getItemId() == actionId) {
                item.setChecked(true);
            }
        }
    }

    private void initToolBar() {
        setSupportActionBar(mToolBar);
        mActionBar = getSupportActionBar();
        if(mActionBar!=null){
        mActionBar.setTitle("");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRootPresenter.takeView(this);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentScreen() != null && !viewOnBackPressed() && !Flow.get(this).goBack()) {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Object key = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                key = new TranslateScreen();
                break;

            case R.id.navigation_history:
                key = new HistoryScreen();
                break;

            case R.id.navigation_favorite:
                key = new FavoriteScreen();
                break;
        }
        if (key != null) {
            Flow.get(this).set(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Flow.get(this).goBack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // region================IRootView==============

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mRootContainer, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(mRootContainer, e.toString(), BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void setBottomNavigationViewVisibility(boolean isVisible) {
        if(isVisible){
            navigation.setVisibility(View.VISIBLE);
        }else{
            navigation.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLanTo(LangRealm language) {
        if (language != null) {
            mTranslateTo.setText(language.getLang());
            mRootPresenter.setLanguageCodeTo(language.getId());
        } else {
            mTranslateTo.setText(R.string.choose_lang);
        }

    }

    @Override
    public void setLanFrom(LangRealm language) {
        if (language != null) {
            mTranslateFrom.setText(language.getLang());
            mRootPresenter.setLanguageCodeFrom(language.getId());
        } else {
            mTranslateFrom.setText(R.string.choose_lang);
        }
    }

    @Nullable
    @Override
    public IView getCurrentScreen() {
        return (IView) mRootFrame.getChildAt(0);
    }

    // endregion


    //region===============================Events==========================

    @OnClick(R.id.translate_from)
    void click() {
        Flow.get(this).set(new LanguageScreen(TO));
    }

    @OnClick(R.id.translate_to)
    void clickOn() {
        Flow.get(this).set(new LanguageScreen(FROM));
    }

    @OnClick(R.id.change_dir_img)
    void onClick() {
        String to = mTranslateTo.getText().toString();
        String from = mTranslateFrom.getText().toString();
        String codeTo = mRootPresenter.getLanguageCodeTo();
        String codeFrom = mRootPresenter.getLanguageCodeFrom();
        mTranslateTo.setText(from);
        mTranslateFrom.setText(to);
        mRootPresenter.setLanguageCodeTo(codeFrom);
        mRootPresenter.setLanguageCodeFrom(codeTo);
        if(getCurrentScreen()!=null) {
            ((TranslateView) getCurrentScreen()).getPresenter().translateText();
        }
    }
    //endregion


    //region===============================IActionBarView==========================

    @Override
    public void setBackArrow(boolean enable) {
        if (enable) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void directionVisible(boolean isVisible) {
        if (isVisible) {
            mDirectionWrapper.setVisibility(View.VISIBLE);
        } else {
            mDirectionWrapper.setVisibility(View.GONE);
        }
    }


    @Override
    public void setMenuItem(List<MenuItemHolder> items) {
        mActionBarMenuItems = items;
        supportInvalidateOptionsMenu();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mActionBarMenuItems != null && !mActionBarMenuItems.isEmpty()) {
            for (MenuItemHolder menuItem : mActionBarMenuItems) {
                MenuItem item = menu.add(menuItem.getItemTitle());
                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                        .setIcon(menuItem.getIconResId())
                        .setOnMenuItemClickListener(menuItem.getListener());
            }
        } else {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void setActionBarTitle(CharSequence title) {
        mActionBar.setTitle(title);
    }

    //endregion


    // region================DI==============


    @dagger.Component(dependencies = AppComponent.class, modules = RootModule.class)
    @RootScope
    public interface RootComponent {
        void inject(RootActivity activity);

        void inject(RootPresenter presenter);

        RootPresenter getRootPresenter();
    }

    // endregion

}
