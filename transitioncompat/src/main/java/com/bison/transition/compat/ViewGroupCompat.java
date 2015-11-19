package com.bison.transition.compat;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.ViewGroup;

import com.bison.transition.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public class ViewGroupCompat {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static class DefaultViewGroupImp {

        private static Field sFieldLayoutSuppressed;
        private static LayoutTransition sEmptyLayoutTransition;

        public void suppressLayout(final ViewGroup group, boolean suppress) {
            if (sEmptyLayoutTransition == null) {
                sEmptyLayoutTransition = new LayoutTransition() {
                    @Override
                    public boolean isChangingLayout() {
                        return true;
                    }
                };
                sEmptyLayoutTransition.setAnimator(LayoutTransition.APPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.CHANGING, null);
            }
            if (suppress) {
                LayoutTransition layoutTransition = group.getLayoutTransition();
                if (layoutTransition != null && layoutTransition != sEmptyLayoutTransition) {
                    group.setTag(R.id.group_layouttransition_cache, group.getLayoutTransition());
                }
                group.setLayoutTransition(sEmptyLayoutTransition);
            } else {
                group.setLayoutTransition(null);
                if (sFieldLayoutSuppressed == null) {
                    sFieldLayoutSuppressed = ReflectionTool.getPrivateField(ViewGroup.class,
                            "mLayoutSuppressed");
                }
                Boolean suppressed = (Boolean) ReflectionTool.getFieldValue(group,
                        Boolean.FALSE, sFieldLayoutSuppressed);
                if (!Boolean.FALSE.equals(suppressed)) {
                    ReflectionTool.setFieldValue(group, sFieldLayoutSuppressed, false);
                    group.requestLayout();
                }
                final LayoutTransition layoutTransition = (LayoutTransition)
                        group.getTag(R.id.group_layouttransition_cache);
                if (layoutTransition != null) {
                    group.setTag(R.id.group_layouttransition_cache, null);
                    group.post(new Runnable() {
                        @Override
                        public void run() {
                            group.setLayoutTransition(layoutTransition);
                        }
                    });
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMr2ViewGroupImp extends DefaultViewGroupImp {

        private static final Method METHOD_suppressLayout = ReflectionTool.getMethod(ViewGroup.class, "suppressLayout",
                boolean.class);

        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            ReflectionTool.invoke(group, null, METHOD_suppressLayout, suppress);
        }
    }

    private static final DefaultViewGroupImp IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMr2ViewGroupImp();
        } else {
            IMPL = new DefaultViewGroupImp();
        }
    }

    public static void suppressLayout(ViewGroup group, boolean suppress) {
        if (group != null) {
            IMPL.suppressLayout(group, suppress);
        }
    }
}
    