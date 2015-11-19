package com.bison.app.ui.swipeback;

import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.v4.widget.ViewDragHelper;

/**
 * Created by oeager on 2015/8/14 0014.
 * email: oeager@foxmail.com
 */
public final class SwipeConfiguration {

    private float sensitivity = 1f;
    private int scrimColor = 0x99000000;
    private float velocityThreshold = 5f;
    private float distanceThreshold = 0.25f;
    private float edgeSize = 0.18f;//1f fullscreen or edgeSize
    private float scrimStartAlpha = 0.8f;
    private float scrimEndAlpha = 0f;
    @SwipePosition
    private int position = LEFT;
    private SwipePanelLayout.OnPanelSlideListener listener;

    public final static int LEFT = ViewDragHelper.EDGE_LEFT;
    public final static int RIGHT = ViewDragHelper.EDGE_RIGHT;
    public final static int TOP = ViewDragHelper.EDGE_TOP;
    public final static int BOTTOM = ViewDragHelper.EDGE_BOTTOM;
    public final static int HORIZONTAL = ViewDragHelper.EDGE_LEFT|ViewDragHelper.EDGE_RIGHT;
    public final static int VERTICAL = ViewDragHelper.EDGE_TOP|ViewDragHelper.EDGE_BOTTOM;

    @IntDef({LEFT,RIGHT,TOP,BOTTOM,HORIZONTAL,VERTICAL})
    public @interface SwipePosition{

    }


    private SwipeConfiguration() {
    }

    public int getScrimColor() {
        return scrimColor;
    }

    public int getPosition() {
        return position;
    }

    public float getVelocityThreshold() {
        return velocityThreshold;
    }

    public float getDistanceThreshold() {
        return distanceThreshold;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public SwipePanelLayout.OnPanelSlideListener getListener() {
        return listener;
    }
    public boolean isCaptureFullScreen() {
        return edgeSize==1f;
    }

    public float getEdgeSize(float size) {
        return edgeSize * size;
    }
    public float getScrimStartAlpha(){
        return scrimStartAlpha;
    }
    public float getScrimEndAlpha(){
        return scrimEndAlpha;
    }
    public static class Builder {

        private SwipeConfiguration config;

        public Builder() {
            config = new SwipeConfiguration();
        }

        public Builder position(@SwipePosition int position) {
            config.position = position;
            return this;
        }

        public Builder sensitivity(float sensitivity) {
            config.sensitivity = sensitivity;
            return this;
        }

        public Builder scrimColor(@ColorInt int color) {
            config.scrimColor = color;
            return this;
        }

        public Builder velocityThreshold(float threshold) {
            config.velocityThreshold = threshold;
            return this;
        }

        public Builder distanceThreshold(@FloatRange(from = .1f, to = .9f) float threshold) {
            config.distanceThreshold = threshold;
            return this;
        }

        public Builder edgeSize(@FloatRange(from = 0f, to = 1f) float edgeSize) {
            config.edgeSize = edgeSize;
            return this;
        }

        public Builder scrimStartAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha){
            config.scrimStartAlpha = alpha;
            return this;
        }

        public Builder scrimEndAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha){
            config.scrimEndAlpha = alpha;
            return this;
        }

        public Builder listener(SwipePanelLayout.OnPanelSlideListener listener) {
            config.listener = listener;
            return this;
        }

        public SwipeConfiguration build() {
            return config;
        }

    }
}
