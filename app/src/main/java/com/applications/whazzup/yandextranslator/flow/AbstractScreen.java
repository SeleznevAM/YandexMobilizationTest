package com.applications.whazzup.yandextranslator.flow;


import android.util.Log;

import com.applications.whazzup.yandextranslator.mortar.ScreenScoper;

import flow.ClassKey;

public abstract class AbstractScreen<T> extends ClassKey {

    public static final String TAG = "ABSTRACT_SCREEN";

    public String getScopeName(){
        return getClass().getName();
    }

    public abstract Object createScreenComponent(T parentComponent);

    public void unregisterScope(){
        Log.e(TAG, "unregisterScope: "+this.getScopeName());
        ScreenScoper.destroyScreenScope(getScopeName());
    }

    public int getLayoutResId(){
        int layout;

        Screen screen;
        screen = this.getClass().getAnnotation(Screen.class);
        if(screen == null){
            throw new IllegalStateException("@ScreenAnnotations is missing on screen " + getScopeName());
        }else{
            layout = screen.value();
        }
        return layout;
    }
}
