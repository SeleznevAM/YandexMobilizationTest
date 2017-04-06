package com.applications.whazzup.yandextranslator.ui.screens.translate_detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applications.whazzup.yandextranslator.di.DaggerService;
import com.applications.whazzup.yandextranslator.flow.AbstractScreen;
import com.applications.whazzup.yandextranslator.ui.screens.language.LanguageScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate.TranslateScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate_detail.favorite.FavoriteScreen;
import com.applications.whazzup.yandextranslator.ui.screens.translate_detail.history.HistoryScreen;

import mortar.MortarScope;


public class DetailAdpater extends PagerAdapter {
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AbstractScreen screen = null;

        switch (position){
            case  0:
                screen = new HistoryScreen();
                break;
            case 1:
                screen = new FavoriteScreen();
                break;
        }

        MortarScope screenScope = createScreenScopeFromContext(container.getContext(), screen);
        Context screenContext = screenScope.createContext(container.getContext());
        View newView = LayoutInflater.from(screenContext).inflate(screen.getLayoutResId(), container, false);
        container.addView(newView);
        return newView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "История";
                break;
            case 1:
                title = "Избранное";
                break;
        }
        return title;
    }

    private MortarScope createScreenScopeFromContext(Context context, AbstractScreen screen){
        MortarScope parentScope = MortarScope.getScope(context);
        MortarScope childScope = parentScope.findChild(screen.getScopeName());
        if(childScope==null){
            Object screenComponent = screen.createScreenComponent(parentScope.getService(DaggerService.SERVICE_NAME));
            if(screenComponent==null){
                throw new IllegalStateException("don't create screen component " + screen.getScopeName());
            }

            childScope = parentScope.buildChild().withService(DaggerService.SERVICE_NAME, screenComponent)
                    .build(screen.getScopeName());
        }
        return childScope;
    }
}
