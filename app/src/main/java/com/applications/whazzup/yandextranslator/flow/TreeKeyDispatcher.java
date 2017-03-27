package com.applications.whazzup.yandextranslator.flow;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.mortar.ScreenScoper;
import com.applications.whazzup.yandextranslator.ui.activities.RootActivity;

import java.util.Collections;
import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.KeyChanger;
import flow.State;
import flow.Traversal;
import flow.TraversalCallback;
import flow.TreeKey;

public class TreeKeyDispatcher extends KeyChanger implements Dispatcher {
    private Activity mActivity;

    private Object inKey;

    @Nullable
    private Object outKey;

    public FrameLayout mRootFrame;

    public TreeKeyDispatcher(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void dispatch(Traversal traversal, TraversalCallback callback) {
        Map<Object, Context> mConexts;
        State inState = traversal.getState(traversal.destination.top());
        inKey = inState.getKey();
        State outState = traversal.origin == null ? null : traversal.getState(traversal.origin.top());
        outKey = outState == null ? null : outState.getKey();

        mRootFrame = (FrameLayout) mActivity.findViewById(R.id.root_frame); //Заменить на руутфрейм

        if (inKey.equals(outKey)) {
            callback.onTraversalCompleted();
            return;
        }

        if (inKey instanceof TreeKey) {
            // TODO: 27.11.2016 implements treeKey case
        }
        // TODO: 27.11.2016 create mortar context for screen
        Context flowContext = traversal.createContext(inKey, mActivity);
        Context mortarContext = ScreenScoper.getScreenScope((AbstractScreen)inKey).createContext(flowContext);
        mConexts = Collections.singletonMap(inKey, mortarContext);
        changeKey(outState, inState, traversal.direction, mConexts, callback);
    }

    @Override
    public void changeKey(@Nullable State outgoingState, State incomingState, Direction direction, Map<Object, Context> incomingContexts, TraversalCallback callback) {
        Context context = incomingContexts.get(inKey);

        /**
         * Сохраняем состояние экрана
         */

        if (outgoingState != null) {
            outgoingState.save(mRootFrame.getChildAt(0));
        }

        /**
         * Создаем новый экран
         */
        Screen screen;
        screen = inKey.getClass().getAnnotation(Screen.class);
        if (screen == null) {
            throw new IllegalStateException("@Screen annotation is missing in screen " + ((AbstractScreen) inKey).getClass());
        } else {
            int layouyt = screen.value();
            LayoutInflater inflater = LayoutInflater.from(context);
            View newView = inflater.inflate(layouyt, mRootFrame, false);

            /**
             * restore state new view
             */

            incomingState.restore(newView);
            // TODO: 27.11.2016 Unregister screen scope
            /**
             * delete old view
             */

            if (outKey != null && !(inKey instanceof TreeKey)) {
                ((AbstractScreen) outKey).unregisterScope();
            }

            if (mRootFrame.getChildAt(0) != null) {
                mRootFrame.removeView(mRootFrame.getChildAt(0));
            }
            mRootFrame.addView(newView);
            callback.onTraversalCompleted();
        }
    }
}
