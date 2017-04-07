package com.applications.whazzup.yandextranslator.mvp.views;



import com.applications.whazzup.yandextranslator.mvp.presenters.MenuItemHolder;

import java.util.List;

public interface IActionBarView {

    void setBackArrow(boolean enable);

    void directionVisible(boolean isVisible);

    void setMenuItem(List<MenuItemHolder> items);

}
