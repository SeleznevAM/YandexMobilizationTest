package com.applications.whazzup.yandextranslator.utils;


import android.view.View;

public class ViewHelper {
    public static void waitForMeasure(View view, OnMeasureCallback callback) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width > 0 && height > 0) {
            callback.onMeasure(view, width, height);
            return;
        }
    }

    public interface OnMeasureCallback {
        void onMeasure(View view, int width, int height);
    }
}
