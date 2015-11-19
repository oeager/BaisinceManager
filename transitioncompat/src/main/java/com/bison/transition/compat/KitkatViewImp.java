package com.bison.transition.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Property;
import android.view.View;

import com.bison.transition.FloatProperty;

import java.lang.reflect.Method;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public class KitkatViewImp extends ViewCompat.JellyBeanMR2ViewImp {
    private static final Method METHOD_getTransitionAlpha = ReflectionTool.getMethod(View.class, "getTransitionAlpha");
    private static final Method METHOD_setTransitionAlpha = ReflectionTool.getMethod(View.class, "setTransitionAlpha",
            float.class);

    public static final Property<View, Float> VIEW_TRANSITION_ALPHA = new FloatProperty<View>("transitionAlpha") {
        @Override
        public void setValue(View object, float value) {
            ViewCompat.setTransitionAlpha(object, value);
        }

        @Override
        public Float get(View object) {
            return ViewCompat.getTransitionAlpha(object);
        }
    };

    @Override
    public float getTransitionAlpha(View v) {
        return (Float) ReflectionTool.invoke(v, 1, METHOD_getTransitionAlpha);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean isLaidOut(View v, boolean defaultValue) {
        return v.isLaidOut();
    }

    @Override
    public void setTransitionAlpha(View v, float alpha) {
        ReflectionTool.invoke(v, null, METHOD_setTransitionAlpha, alpha);
    }

    @Override
    public boolean isTransitionAlphaCompatMode() {
        return false;
    }

    @Override
    public Property<View, Float> getAlphaProperty() {
        return VIEW_TRANSITION_ALPHA;
    }
}