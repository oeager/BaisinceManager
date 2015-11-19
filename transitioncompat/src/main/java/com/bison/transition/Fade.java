/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bison.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bison.transition.compat.AnimatorUtils;
import com.bison.transition.compat.ViewCompat;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Fade extends Visibility {

    private static final String LOG_TAG = "Fade";

    /**
     * Fading mode used in {@link #Fade(int)} to make the transition
     * operate on targets that are appearing. Maybe be combined with
     * {@link #OUT} to fade both in and out. Equivalent to
     * {@link Visibility#MODE_IN}.
     */
    public static final int IN = Visibility.MODE_IN;

    /**
     * Fading mode used in {@link #Fade(int)} to make the transition
     * operate on targets that are disappearing. Maybe be combined with
     * {@link #IN} to fade both in and out. Equivalent to
     * {@link Visibility#MODE_OUT}.
     */
    public static final int OUT = Visibility.MODE_OUT;

    /**
     * Constructs a Fade transition that will fade targets in and out.
     */
    public Fade() {
    }

    /**
     * Constructs a Fade transition that will fade targets in
     * and/or out, according to the value of fadingMode.
     *
     * @param fadingMode The behavior of this transition, a combination of
     *                   {@link #IN} and {@link #OUT}.
     */
    public Fade(int fadingMode) {
        setMode(fadingMode);
    }

    public Fade(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fade);
        int fadingMode = a.getInt(R.styleable.Fade_fadingMode, getMode());
        a.recycle();
        setMode(fadingMode);
    }

    /**
     * Utility method to handle creating and running the Animator.
     */
    private Animator createAnimation(final View view, float startAlpha, float endAlpha) {
        final float endListenerAlpha;
        if (ViewCompat.isTransitionAlphaCompatMode()) {
            float curAlpha = view.getAlpha();
            startAlpha = curAlpha * startAlpha;
            endAlpha = curAlpha * endAlpha;
            endListenerAlpha = curAlpha;
        } else {
            endListenerAlpha = 1f;
        }
        if (startAlpha == endAlpha) {
            return null;
        }
        ViewCompat.setTransitionAlpha(view, startAlpha);
        final ObjectAnimator anim = ObjectAnimator.ofFloat(view, ViewCompat.getAlphaProperty(), endAlpha);
        if (DBG) {
            Log.d(LOG_TAG, "Created animator " + anim);
        }
        final FadeAnimatorListener listener = new FadeAnimatorListener(view, endListenerAlpha);
        anim.addListener(listener);
        addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                ViewCompat.setTransitionAlpha(view, endListenerAlpha);
            }
        });
        AnimatorUtils.addPauseListener(anim, listener);
        return anim;
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view,
                             TransitionValues startValues,
                             TransitionValues endValues) {
        if (DBG) {
            View startView = (startValues != null) ? startValues.view : null;
            Log.d(LOG_TAG, "Fade.onAppear: startView, startVis, endView, endVis = " +
                    startView + ", " + view);
        }
        return createAnimation(view, 0, 1);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                                TransitionValues endValues) {
        return createAnimation(view, 1, 0);
    }

    private static class FadeAnimatorListener extends AnimatorListenerAdapter {
        private final View mView;
        private float mPausedAlpha = -1;
        private float mEndListenerAlpha;
        private boolean mLayerTypeChanged = false;

        public FadeAnimatorListener(View view, float endListenerAlpha) {
            mView = view;
            mEndListenerAlpha = endListenerAlpha;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            if (AnimatorUtils.hasOverlappingRendering(mView) &&
                    mView.getLayerType() == View.LAYER_TYPE_NONE) {
                mLayerTypeChanged = true;
                mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            ViewCompat.setTransitionAlpha(mView, mEndListenerAlpha);
            if (mLayerTypeChanged) {
                mView.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        }

        @Override
        public void onAnimationPause(Animator animator) {
            mPausedAlpha = ViewCompat.getTransitionAlpha(mView);
            ViewCompat.setTransitionAlpha(mView, mEndListenerAlpha);
        }

        @Override
        public void onAnimationResume(Animator animator) {
            ViewCompat.setTransitionAlpha(mView, mPausedAlpha);
        }
    }

}