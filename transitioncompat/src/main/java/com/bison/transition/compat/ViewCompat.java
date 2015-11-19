package com.bison.transition.compat;

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import com.bison.transition.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public final class ViewCompat {

    private static final ViewFace IMPL;

    static {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            IMPL = new LollipopViewImp();
        } else if (version >= Build.VERSION_CODES.KITKAT) {
            IMPL = new KitkatViewImp();
        } else if (version >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewImp();
        } else if (version >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            IMPL = new JellyBeanMR1ViewImp();
        } else if (version >= Build.VERSION_CODES.JELLY_BEAN) {
            IMPL = new JellyBeanViewImp();
        } else {
            IMPL = new DefaultViewImp();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static class JellyBeanMR2ViewImp extends JellyBeanMR1ViewImp{


        @Override
        public void setClipBounds(View v, Rect clipBounds) {
            v.setClipBounds(clipBounds);
        }

        @Override
        public Rect getClipBounds(View v) {
            return v.getClipBounds();
        }

        @Override
        public Object getWindowId(View view) {
            return view.getWindowId();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    static class JellyBeanMR1ViewImp extends JellyBeanViewImp{
        @Override
        public boolean isRtl(View view) {
            return view != null && view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static class JellyBeanViewImp extends DefaultViewImp{

        @Override
        public void setHasTransientState(View view, boolean hasTransientState) {
            view.setHasTransientState(hasTransientState);
        }

        @Override
        public boolean hasTransientState(View view) {
            return view.hasTransientState();
        }
    }


    static class DefaultViewImp implements ViewFace{

        private static final Method METHOD_SET_FRAME =
                ReflectionTool.getPrivateMethod(View.class, "setFrame", int.class, int.class,
                        int.class, int.class);

        private static final Field FIELD_VIEW_FLAGS =
                ReflectionTool.getPrivateField(View.class, "mViewFlags");

        private static final int VIEW_VISIBILITY_MASK = 0x0000000C;

        @Override
        public void setLeftTopRightBottom(View v, int left, int top, int right, int bottom) {
            ReflectionTool.invoke(v, null, METHOD_SET_FRAME, left, top, right, bottom);
        }

        @Override
        public void setTransitionAlpha(View v, float alpha) {
            v.setAlpha(alpha);
        }

        @Override
        public float getTransitionAlpha(View v) {
            return v.getAlpha();
        }

        @Override
        public boolean isLaidOut(View v, boolean defaultValue) {
            return defaultValue;
        }

        public void setClipBounds(View v, Rect clipBounds) {
            // TODO: Implement support behavior
        }

        public Rect getClipBounds(View v) {
            // TODO: Implement support behavior
            return null;
        }

        public void setTransitionName(View v, String name) {
            v.setTag(R.id.transitionName, name);
        }

        public String getTransitionName(View v) {
            return (String) v.getTag(R.id.transitionName);
        }

        public boolean isTransitionAlphaCompatMode() {
            return true;
        }

        public Property<View, Float> getAlphaProperty() {
            return View.ALPHA;
        }

        public void setTranslationZ(View view, float z) {
            // do nothing
        }

        public float getTranslationZ(View view) {
            return 0;
        }

        public View addGhostView(View view, ViewGroup viewGroup, Matrix matrix) {
            return null;
        }

        public void removeGhostView(View view) {
            // do nothing
        }

        public void transformMatrixToGlobal(View view, Matrix matrix) {
            // TODO: Implement support behavior
        }

        public void transformMatrixToLocal(View v, Matrix matrix) {
            // TODO: Implement support behavior
        }

        public void setAnimationMatrix(View view, Matrix matrix) {
            // TODO: Implement support behavior
        }

        public Object getWindowId(View view) {
            return null;
        }

        public boolean isRtl(View view) {
            return false;
        }

        public void setHasTransientState(View view, boolean hasTransientState) {
            // do nothing; API doesn't exist
        }

        public boolean hasTransientState(View view) {
            return false;
        }

        public void setTransitionVisibility(View v, int visibility) {
            int value = (Integer) ReflectionTool.getFieldValue(v, 0, FIELD_VIEW_FLAGS);
            value = (value & ~VIEW_VISIBILITY_MASK) | visibility;
            ReflectionTool.setFieldValue(v, FIELD_VIEW_FLAGS, value);
        }


    }


    public static void setLeftTopRightBottom(View view, int left, int top, int right, int bottom) {
        IMPL.setLeftTopRightBottom(view, left, top, right, bottom);
    }

    public static void setTransitionAlpha(View view,float alpha){
        IMPL.setTransitionAlpha(view, alpha);
    }

    public static float getTransitionAlpha(View view){
        return IMPL.getTransitionAlpha(view);
    }

    public static boolean isLaidOut(View v, boolean defaultValue) {
        return IMPL.isLaidOut(v, defaultValue);
    }

    public static Rect getClipBounds(View v) {
        return IMPL.getClipBounds(v);
    }

    public static boolean isTransitionAlphaCompatMode() {
        return IMPL.isTransitionAlphaCompatMode();
    }

    public static Property<View, Float> getAlphaProperty() {
        return IMPL.getAlphaProperty();
    }

    public static void setTransitionName(View v, String name) {
        IMPL.setTransitionName(v, name);
    }

    public static String getTransitionName(View v) {
        return IMPL.getTransitionName(v);
    }

    public static float getTranslationZ(View view) {
        return IMPL.getTranslationZ(view);
    }

    public static void setTranslationZ(View view, float z) {
        IMPL.setTranslationZ(view, z);
    }

    public static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    public static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

    public static void setAnimationMatrix(View view, Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    public static View addGhostView(View view, ViewGroup viewGroup, Matrix matrix) {
        return IMPL.addGhostView(view, viewGroup, matrix);
    }

    public static void removeGhostView(View view) {
        IMPL.removeGhostView(view);
    }

    public static Object getWindowId(View view) {
        return IMPL.getWindowId(view);
    }

    public static boolean isRtl(View view) {
        return IMPL.isRtl(view);
    }

    public static boolean hasTransientState(View view) {
        return IMPL.hasTransientState(view);
    }

    public static void setHasTransientState(View view, boolean hasTransientState) {
        IMPL.setHasTransientState(view, hasTransientState);
    }

    /**
     * Change the visibility of the View without triggering any other changes. This is
     * important for transitions, where visibility changes should not adjust focus or
     * trigger a new layout. This is only used when the visibility has already been changed
     * and we need a transient value during an animation. When the animation completes,
     * the original visibility value is always restored.
     *
     * @param visibility One of View.VISIBLE, View.INVISIBLE, or View.GONE.
     */
    public static void setTransitionVisibility(View v, int visibility) {
        IMPL.setTransitionVisibility(v, visibility);
    }

    public static void setClipBounds(View v, Rect clipBounds) {
        IMPL.setClipBounds(v, clipBounds);
    }

}
    