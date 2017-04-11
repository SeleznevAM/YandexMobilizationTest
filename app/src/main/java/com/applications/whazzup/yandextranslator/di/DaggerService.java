package com.applications.whazzup.yandextranslator.di;


import android.content.Context;

public class DaggerService {
    public static final String SERVICE_NAME = "SERVICE_NAME";

    @SuppressWarnings("unchecked")
    public static <T> T getDaggerComponent(Context context) {
        //noinspection ResourceType
        return (T) context.getSystemService(SERVICE_NAME);
    }
}
