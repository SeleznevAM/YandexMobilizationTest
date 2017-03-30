package com.applications.whazzup.yandextranslator.ui.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.components.AppComponent;
import com.applications.whazzup.yandextranslator.di.modules.RootModule;
import com.applications.whazzup.yandextranslator.di.scopes.RootScope;
import com.applications.whazzup.yandextranslator.di.scopes.TranslateScope;
import com.applications.whazzup.yandextranslator.flow.TreeKeyDispatcher;
import com.applications.whazzup.yandextranslator.mvp.presenters.RootPresenter;
import com.applications.whazzup.yandextranslator.mvp.views.IRootView;
import com.applications.whazzup.yandextranslator.mvp.views.IView;
import com.applications.whazzup.yandextranslator.ui.screens.language.LanguageScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;


public class RootActivity extends AppCompatActivity implements IRootView, BottomNavigationView.OnNavigationItemSelectedListener {


    private int from = 0;
    private int to = 1;

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
    public void setLanTo(String lang) {
        mTranslateTo.setText(lang);
    }

    @Override
    public void setLanFrom(String lang) {
        mTranslateFrom.setText(lang);
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
                key = new LanguageScreen(0);
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
        Flow.get(this).set(new LanguageScreen(from));
    }

    @OnClick(R.id.translate_to)
        void clickOn(){
            Flow.get(this).set(new LanguageScreen(to));
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
