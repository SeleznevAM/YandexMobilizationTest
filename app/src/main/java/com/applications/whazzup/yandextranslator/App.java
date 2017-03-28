package com.applications.whazzup.yandextranslator;


import android.app.Application;

import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.di.components.AppComponent;
import com.applications.whazzup.yandextranslator.di.components.DaggerAppComponent;
import com.applications.whazzup.yandextranslator.di.modules.AppModule;
import com.applications.whazzup.yandextranslator.di.modules.RootModule;
import com.applications.whazzup.yandextranslator.flow.Screen;
import com.applications.whazzup.yandextranslator.mortar.ScreenScoper;
import com.applications.whazzup.yandextranslator.ui.activities.DaggerRootActivity_RootComponent;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import okhttp3.internal.http.RealInterceptorChain;

public class App extends Application {

    private static AppComponent sAppComponent;
    private static RootActivity.RootComponent rootActivityComponent;
    private MortarScope mRootScope;
    private MortarScope mRootActivityScope;

    @Override
    public Object getSystemService(String name) {
        return mRootScope.hasService(name) ? mRootScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build()).build());
        createDaggerAppComponent();
        createRootActivityComponent();

        mRootScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, sAppComponent)
                .build("Root");
        mRootActivityScope = mRootScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, rootActivityComponent)
                .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                .build(RootActivity.class.getName());

        ScreenScoper.registerScope(mRootScope);
        ScreenScoper.registerScope(mRootActivityScope);
    }

    private void createDaggerAppComponent(){
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    private void createRootActivityComponent(){
        rootActivityComponent = DaggerRootActivity_RootComponent.builder()
                .appComponent(sAppComponent)
                .rootModule(new RootModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    public static RootActivity.RootComponent getRootActivityComponent() {
        return rootActivityComponent;
    }
}
