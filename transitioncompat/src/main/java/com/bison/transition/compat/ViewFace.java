package com.bison.transition.compat;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public interface ViewFace {

    void setLeftTopRightBottom(View view, int left, int top, int right, int bottom);

    void setTransitionAlpha(View v, float alpha);

    float getTransitionAlpha(View v);

    boolean isLaidOut(View v, boolean defaultValue);

    Rect getClipBounds(View v);

    boolean isTransitionAlphaCompatMode();

    Property<View,Float> getAlphaProperty();

    void setTransitionName(View v, String name);

    String getTransitionName(View v);

    float getTranslationZ(View view);

    void setTranslationZ(View view, float z);

    void transformMatrixToGlobal(View view, Matrix matrix);

    void transformMatrixToLocal(View view, Matrix matrix);

    void setAnimationMatrix(View view, Matrix matrix);

    View addGhostView(View view, ViewGroup viewGroup, Matrix matrix);

    void removeGhostView(View view);

    Object getWindowId(View view);

    boolean isRtl(View view);

    boolean hasTransientState(View view);

    void setHasTransientState(View view, boolean hasTransientState);

    void setTransitionVisibility(View v, int visibility);

    void setClipBounds(View v, Rect clipBounds);
}
