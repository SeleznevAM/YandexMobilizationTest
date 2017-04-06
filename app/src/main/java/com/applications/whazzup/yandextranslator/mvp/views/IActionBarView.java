package com.applications.whazzup.yandextranslator.mvp.views;


import android.support.v4.view.ViewPager;

import com.applications.whazzup.yandextranslator.mvp.presenters.MenuItemHolder;

import java.util.List;

public interface IActionBarView {

    void setBackArrow(boolean enable);

    void setTabLayout(ViewPager viewPager);

    void removeTabLayout();

    void directionVisible(boolean isVisible);

    void actionBarVisible(boolean isVisible);

    void setMenuItem(List<MenuItemHolder> items);

}
