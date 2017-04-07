package com.applications.whazzup.yandextranslator.mvp.presenters;


import android.view.MenuItem;


public class MenuItemHolder {
    private final CharSequence itemTitle;
    private final int iconResId;
    private  MenuItem.OnMenuItemClickListener listener = null;

    public MenuItemHolder( CharSequence itemTitle, int iconResId,MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
        this.iconResId = iconResId;
        this.itemTitle = itemTitle;
    }

    public CharSequence getItemTitle() {
        return itemTitle;
    }

    public int getIconResId() {
        return iconResId;
    }

    public MenuItem.OnMenuItemClickListener getListener() {
        return listener;
    }
}
