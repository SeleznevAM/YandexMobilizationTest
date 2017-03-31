package com.applications.whazzup.yandextranslator.mvp.views;


import android.support.v4.view.ViewPager;

import java.util.List;

public interface IActionBarView {

    void setBackArrow(boolean enable);

    void setTabLayout(ViewPager viewPager);

    void removeTabLayout();

    void directionVisible(boolean isVisible);

}
