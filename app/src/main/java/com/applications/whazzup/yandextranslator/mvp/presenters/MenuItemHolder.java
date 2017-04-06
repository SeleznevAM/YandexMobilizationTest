package com.applications.whazzup.yandextranslator.mvp.presenters;


import android.view.MenuItem;
import android.view.View;

public class MenuItemHolder {
    private final CharSequence itemTitle;
    private final int iconResId;
    private  MenuItem.OnMenuItemClickListener listener = null;
    private  View.OnClickListener viewListener = null;
    private int title;

    public MenuItemHolder( CharSequence itemTitle, int iconResId,MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
        this.iconResId = iconResId;
        this.itemTitle = itemTitle;
    }

    public MenuItemHolder(CharSequence itemTitle, int iconResId, View.OnClickListener viewListener) {
        this.itemTitle = itemTitle;
        this.iconResId = iconResId;
        this.viewListener = viewListener;
    }

    public View.OnClickListener getViewListener() {
        return viewListener;
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
