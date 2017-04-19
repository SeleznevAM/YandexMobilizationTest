package com.applications.whazzup.yandextranslator.mvp.presenters;



import android.support.annotation.Nullable;

import com.applications.whazzup.yandextranslator.App;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;
import com.applications.whazzup.yandextranslator.mvp.models.RootModel;
import com.applications.whazzup.yandextranslator.mvp.views.IRootView;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import io.realm.Realm;
import mortar.MortarScope;
import mortar.Presenter;
import mortar.bundler.BundleService;

public class RootPresenter extends Presenter<IRootView> {

    private static RootPresenter ourInstance = null;
    private String languageCodeTo;
    private String languageCodeFrom;
    private String lastDirection;

    @Inject
    RootModel mRootModel;

    public static RootPresenter getInstance() {
        if (ourInstance == null) {
            ourInstance = new RootPresenter();
        }
        return ourInstance;
    }

    /*
    Данный метод отрабатывает самым первым при загрузке Рут Активити, еще до момента отображения наэкране.
    Здесь мы получаем коды языков направления перевод из ШП, а так же получив последний сохраненный перевод, сохраняем поледне направление перевода,
    так как выставленные коды и направление перевода сохранненного текста могут различаться.
     */

    @Override
    protected void onEnterScope(MortarScope scope) {
        super.onEnterScope(scope);
        languageCodeTo = mRootModel.getLanguageTo();
        languageCodeFrom = mRootModel.getLanguageFrom();
        Realm realm = Realm.getDefaultInstance();
        TranslateRealm translateRealm =  realm.where(TranslateRealm.class).equalTo("id", mRootModel.loadTranslateByHash()).findFirst();
        if(translateRealm!=null) {
            lastDirection =translateRealm.getDirection();
        }
    }


    private RootPresenter() {
        App.getRootActivityComponent().inject(this);
        mRootModel.getAllLang();
    }

    @Nullable
    public IRootView getRootView() {
        return getView();
    }

    @Override
    protected BundleService extractBundleService(IRootView view) {
        return BundleService.getBundleService((RootActivity) view);
    }

    public ActionBarBuilder newActionBarBuilder() {
        return this.new ActionBarBuilder();
    }

    /*
    Сохраняем id(hashCode) перевода в ШП
     */
    public void saveTranslateHash() {
        if(getView().getCurrentScreen()!=null) {
            TranslateRealm translateRealm = mRootModel.getTranslateRealmFromDb(((TranslateView) getView().getCurrentScreen()).getOriginalText(), languageCodeFrom + "-" + languageCodeTo);
            mRootModel.saveTranslateHash(translateRealm);
        }

    }

    //region===============================ActionBarBuilder==========================

    /**
     * Класс для формирования и отображения различных ActionBar на различных скринах
     */
   public class ActionBarBuilder {

        private boolean isGoBack;
        private boolean directionVisible;
        private List<MenuItemHolder> item = new ArrayList<>();
        private CharSequence title = "";


        public ActionBarBuilder setBackArrow(boolean enable) {
            this.isGoBack = enable;
            return this;
        }

        public ActionBarBuilder setDirectionVisible(boolean isVisible) {
            this.directionVisible = isVisible;
            return this;
        }

        public ActionBarBuilder setTitle(String title){
            this.title = title;
            return this;
        }

        public ActionBarBuilder addAction(MenuItemHolder itemMenu) {
            this.item.add(itemMenu);
            return this;
        }


        public void build() {
            if (getView() != null) {
                RootActivity activity = (RootActivity) getView();
                activity.setBackArrow(isGoBack);
                activity.setMenuItem(item);
                activity.directionVisible(directionVisible);
                activity.setActionBarTitle(title);
            }
        }
    }

    //endregion


    //region===============================Getters and setters==========================

    public String getLanguageCodeTo() {
        return languageCodeTo;
    }

    public void setLanguageCodeTo(String languageCodeTo) {
        this.languageCodeTo = languageCodeTo;
        mRootModel.saveLanguageTo(this.languageCodeTo);
    }

    public String getLanguageCodeFrom() {
        return languageCodeFrom;
    }

    public void setLanguageCodeFrom(String languageCodeFrom) {
        this.languageCodeFrom = languageCodeFrom;
        mRootModel.saveLanguageFrom(this.languageCodeFrom);
    }

    public String getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(String lastDirection) {
        this.lastDirection = lastDirection;
    }

    //endregion
}
