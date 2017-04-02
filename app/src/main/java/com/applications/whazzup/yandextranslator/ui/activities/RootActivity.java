package com.applications.whazzup.yandextranslator.ui.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.components.AppComponent;
import com.applications.whazzup.yandextranslator.di.modules.RootModule;
import com.applications.whazzup.yandextranslator.di.scopes.RootScope;
import com.applications.whazzup.yandextranslator.di.scopes.TranslateScope;
import com.applications.whazzup.yandextranslator.flow.TreeKeyDispatcher;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;
import com.applications.whazzup.yandextranslator.mvp.views.IActionBarView;
import com.applications.whazzup.yandextranslator.mvp.views.IRootView;
import com.applications.whazzup.yandextranslator.mvp.views.IView;
import com.applications.whazzup.yandextranslator.ui.screens.language.LanguageScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate_detail.DetailScreen;

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

    ActionBar mActionBar;


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
    public Object getSystemService(String name) {
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
    }

    private void initToolBar(){
        setSupportActionBar(mToolBar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
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


    // region================IRootView==============

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showLoad() {

    }

    @Override
    public void hideLoad() {

    }

    @Override
    public void setLanTo(LangRealm language) {
        mTranslateTo.setText(language.getLang());
        mRootPresenter.setLanguageCodeTo(language.getId());
    }

    @Override
    public void setLanFrom(LangRealm language) {
        mTranslateFrom.setText(language.getLang());
        mRootPresenter.setLanguageCodeFrom(language.getId());
        //translateFrom = language.getId();
    }


    @Nullable
    @Override
    public IView getCurrentScreen() {
        return (IView) mRootFrame.getChildAt(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Object key = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                key  = new TranslateScreen();
                break;

            case R.id.navigation_dashboard:
                key = new DetailScreen();
                break;

            case R.id.navigation_notifications:
                key = new TranslateScreen();
                break;
        }
        if(key!=null){
            Flow.get(this).set(key);
            return true;
        }
        return false;
    }

    // endregion


    @OnClick(R.id.translate_from)
    void click(){
        Flow.get(this).set(new LanguageScreen(TO));
    }

    @OnClick(R.id.translate_to)
        void clickOn(){
            Flow.get(this).set(new LanguageScreen(FROM));
        }

        @OnClick(R.id.change_dir_img)
            void onClick(){
             String to = mTranslateTo.getText().toString();
            String from = mTranslateFrom.getText().toString();
            String codeTo = mRootPresenter.getLanguageCodeTo();
            String codeFrom = mRootPresenter.getLanguageCodeFrom();
            mTranslateTo.setText(from);
            mTranslateFrom.setText(to);
            mRootPresenter.setLanguageCodeTo(codeFrom);
            mRootPresenter.setLanguageCodeFrom(codeTo);
        }


    //region===============================IActionBArView==========================

    @Override
    public void setBackArrow(boolean enable) {
        if(enable){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void setTabLayout(ViewPager viewPager) {
        TabLayout tabView = new TabLayout(this);
        tabView.setupWithViewPager(viewPager);
        mAppBarLayout.addView(tabView);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabView));
    }

    @Override
    public void removeTabLayout() {
        View tabView = mAppBarLayout.getChildAt(1);
        if (tabView != null && tabView instanceof TabLayout) {
            mAppBarLayout.removeView(tabView);
        }
    }

    @Override
    public void directionVisible(boolean isVisible) {
        if(isVisible){
            mDirectionWrapper.setVisibility(View.VISIBLE);
        }else{
            mDirectionWrapper.setVisibility(View.GONE);
        }
    }

    // region================DI==============



    @dagger.Component(dependencies = AppComponent.class, modules = RootModule.class)
    @RootScope
    public interface RootComponent{
        void inject(RootActivity activity);
        void inject (RootPresenter presenter);
        RootPresenter getRootPresenter();
    }

    // endregion
}
