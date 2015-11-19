package com.bison.app.ui.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import static com.bison.app.ui.swipeback.SwipeConfiguration.TOP;
import static com.bison.app.ui.swipeback.SwipeConfiguration.VERTICAL;

/**
 * Created by oeager on 2015/8/14 0014.
 * email: oeager@foxmail.com
 */
public class SwipePanelLayout extends FrameLayout {

    private static final int MIN_FLING_VELOCITY = 400; // dips per second
    private final View mContentView;
    private final View mDimView;
    private final ViewDragHelper mDragHelper;
    private final SwipeConfiguration mConfig;
    private OnPanelSlideListener mListener;
    private boolean mIsLocked = false;
    private int mEdgePosition;
//    private float mScrimOpacity;
    private boolean mIsEdgeTouched = false;

    private SwipePanelLayout(Context context, View contentView, SwipeConfiguration configuration) {
        super(context);
        mContentView = contentView;
        mConfig = configuration;
        mDragHelper = ViewDragHelper.create(this, mConfig.getSensitivity(), new ViewDragCallback());
        mEdgePosition = mConfig.getPosition();
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setEdgeTrackingEnabled(mEdgePosition);
        mDimView = new View(getContext());
        mDimView.setBackgroundColor(mConfig.getScrimColor());
        mDimView.setAlpha(mConfig.getScrimStartAlpha());
        addView(mDimView);
        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
    }

    public void setOnPanelSlideListener(OnPanelSlideListener listener) {
        mListener = listener;
    }

    public void lock() {
        mDragHelper.abort();
        mIsLocked = true;
    }

    public void unlock() {
        mDragHelper.abort();
        mIsLocked = false;
    }

    public static SwipeController attachToActivity(final Activity activity, final SwipeConfiguration configuration) {
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        final SwipePanelLayout panelLayout = new SwipePanelLayout(activity, decorChild, configuration);
        panelLayout.addView(decorChild);
        decor.addView(panelLayout);
        panelLayout.setOnPanelSlideListener(new OnPanelSlideListener() {
            @Override
            public void onStateChanged(int state) {
                if (configuration.getListener() != null)
                    configuration.getListener().onStateChanged(state);
            }

            @Override
            public void onClosed() {
                if (configuration.getListener() != null) configuration.getListener().onClosed();
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {
                if (configuration.getListener() != null) configuration.getListener().onOpened();
            }

            @Override
            public void onSlideChange(float percent) {
                if (configuration.getListener() != null)
                    configuration.getListener().onSlideChange(percent);
            }
        });
        return new SwipeController() {
            @Override
            public void lock() {
                panelLayout.lock();
            }

            @Override
            public void unlock() {
                panelLayout.unlock();
            }
        };
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptForDrag;

        if (mIsLocked) {
            return false;
        }

        if (!mConfig.isCaptureFullScreen()) {
            mIsEdgeTouched = canDragFromEdge(ev);
        }

        // Fix for pull request #13 and issue #12
        try {
            interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            interceptForDrag = false;
        }

        return interceptForDrag && !mIsLocked;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsLocked) {
            return false;
        }

        try {
            mDragHelper.processTouchEvent(event);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

//    @Override
//    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        final boolean drawContent = child == mContentView;
//        boolean ret = super.drawChild(canvas, child, drawingTime);
//        if (mScrimOpacity > 0 && drawContent
//                && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
//            drawScrim(canvas, child);
//        }
//        return ret;
//    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private boolean canDragFromEdge(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        switch (mConfig.getPosition()) {
//            case LEFT:
//                return x < mConfig.getEdgeSize(getWidth());
//            case RIGHT:
//                return x > getWidth() - mConfig.getEdgeSize(getWidth());
//            case BOTTOM:
//                return y > getHeight() - mConfig.getEdgeSize(getHeight());
            case TOP:
                return y < mConfig.getEdgeSize(getHeight());
//            case HORIZONTAL:
//                return x < mConfig.getEdgeSize(getWidth()) || x > getWidth() - mConfig.getEdgeSize(getWidth());
            case VERTICAL:
                return y < mConfig.getEdgeSize(getHeight()) || y > getHeight() - mConfig.getEdgeSize(getHeight());
        }
        return false;
    }

//    private void drawScrim(Canvas canvas, View child) {
//        final int baseAlpha = (mConfig.getScrimColor() & 0xff000000) >>> 24;
//        final int alpha = (int) (baseAlpha * mScrimOpacity);
//        final int color = alpha << 24 | (mConfig.getScrimColor() & 0xffffff);
//
//        if (mEdgePosition == SwipeConfiguration.LEFT) {
//            canvas.clipRect(0, 0, child.getLeft(), getHeight());
//        } else if (mEdgePosition == SwipeConfiguration.RIGHT) {
//            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
//        } else if (mEdgePosition == SwipeConfiguration.TOP) {
//            canvas.clipRect(child.getLeft(), 0, getRight(), child.getTop());
//        } else if (mEdgePosition == SwipeConfiguration.BOTTOM) {
//            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
//        } else if (mEdgePosition == SwipeConfiguration.VERTICAL) {
//            if (child.getTop() > 0) {
//                canvas.clipRect(child.getLeft(), 0, getRight(), child.getTop());
//            } else {
//                canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
//            }
//        } else if (mEdgePosition == SwipeConfiguration.HORIZONTAL) {
//            if (child.getLeft() > 0) {
//                canvas.clipRect(0, 0, child.getLeft(), getHeight());
//            } else {
//                canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
//            }
//        }
//        canvas.drawColor(color);
//    }
//private void drawShadow(Canvas canvas, View child) {
//    final Rect childRect = mTmpRect;
//    child.getHitRect(childRect);
//
//    if ((mEdgeFlag & EDGE_LEFT) != 0) {
//        mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top,
//                childRect.left, childRect.bottom);
//        mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
//        mShadowLeft.draw(canvas);
//    }
//
//    if ((mEdgeFlag & EDGE_RIGHT) != 0) {
//        mShadowRight.setBounds(childRect.right, childRect.top,
//                childRect.right + mShadowRight.getIntrinsicWidth(), childRect.bottom);
//        mShadowRight.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
//        mShadowRight.draw(canvas);
//    }
//
//    if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
//        mShadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right,
//                childRect.bottom + mShadowBottom.getIntrinsicHeight());
//        mShadowBottom.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
//        mShadowBottom.draw(canvas);
//    }
//}
    static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public void applyScrim(float percent){
        float alpha = (percent * (mConfig.getScrimStartAlpha() - mConfig.getScrimEndAlpha())) + mConfig.getScrimEndAlpha();
        mDimView.setAlpha(alpha);
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            boolean ret = mConfig.isCaptureFullScreen() ||
                    ((mEdgePosition==SwipeConfiguration.VERTICAL||mEdgePosition==SwipeConfiguration.TOP)
                    ?mIsEdgeTouched:mDragHelper.isEdgeTouched(mEdgePosition, pointerId));
            return ret&&mContentView==child;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            int width = child.getWidth();
            if (mEdgePosition == SwipeConfiguration.LEFT) {
                ret = clamp(left, 0, width);
            } else if (mEdgePosition == SwipeConfiguration.RIGHT) {
                ret = clamp(left, -width, 0);
            } else if (mEdgePosition == SwipeConfiguration.HORIZONTAL) {
                ret = clamp(left, -width, width);
            }
            return ret;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int ret = 0;
            int height = child.getHeight();
            if (mEdgePosition == SwipeConfiguration.BOTTOM) {
                ret = clamp(top, -height, 0);
            } else if (mEdgePosition == SwipeConfiguration.TOP) {
                ret = clamp(top, 0, height);
            } else if (mEdgePosition == SwipeConfiguration.VERTICAL) {
                ret = clamp(top, -height, height);
            }
            return ret;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mEdgePosition & (ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mEdgePosition & (ViewDragHelper.EDGE_BOTTOM | ViewDragHelper.EDGE_TOP);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float percent = 0f;
            if (mEdgePosition == SwipeConfiguration.LEFT) {
                percent = 1f - ((float) left / (float) changedView.getWidth());
            } else if (mEdgePosition == SwipeConfiguration.RIGHT) {
                percent = 1f - ((float) Math.abs(left) / (float) changedView.getWidth());
            } else if (mEdgePosition == SwipeConfiguration.TOP) {
                percent = 1f - ((float) Math.abs(top) / (float) changedView.getHeight());
            } else if (mEdgePosition == SwipeConfiguration.BOTTOM) {
                percent = 1f - ((float) Math.abs(top) / (float) changedView.getHeight());
            } else if (mEdgePosition == SwipeConfiguration.VERTICAL) {//Vertical
                percent = 1f - ((float) Math.abs(top) / (float) changedView.getHeight());
            } else if (mEdgePosition == SwipeConfiguration.HORIZONTAL) {//horizatal
                percent = 1f - ((float) Math.abs(left) / (float) changedView.getWidth());
            }
//            mScrimOpacity = percent;
            applyScrim(percent);
            if (mListener != null) mListener.onSlideChange(percent);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            final int childHeight = releasedChild.getHeight();
            final float velThreshold = mConfig.getVelocityThreshold();
            final int releaseLeft = releasedChild.getLeft();
            final int releaseTop = releasedChild.getTop();
            float distanceHorizontalShold = childWidth * mConfig.getDistanceThreshold();
            float distanceVerticalShold = childHeight * mConfig.getDistanceThreshold();
            int left = 0, top = 0;
            if (mEdgePosition == SwipeConfiguration.LEFT) {
                boolean session1 = xvel > 0 && ((Math.abs(xvel) > velThreshold && Math.abs(yvel) < velThreshold) || releaseLeft > distanceHorizontalShold);
                boolean session2 = xvel == 0 && releaseLeft > distanceHorizontalShold;
                left = session1 || session2 ? childWidth : 0;
            } else if (mEdgePosition == SwipeConfiguration.RIGHT) {
                boolean session1 = xvel < 0 && ((Math.abs(xvel) > velThreshold && Math.abs(yvel) < velThreshold) || releaseLeft < -distanceHorizontalShold);
                boolean session2 = xvel == 0 && releaseLeft < -distanceHorizontalShold;
                left = session1 || session2 ? -childWidth : 0;
            } else if (mEdgePosition == SwipeConfiguration.HORIZONTAL) {
                if (xvel > 0) {
                    boolean session1 = (Math.abs(xvel) > velThreshold && Math.abs(yvel) < velThreshold) || releaseLeft > distanceHorizontalShold;
                    left = session1 ? childWidth : 0;
                } else if (xvel < 0) {
                    boolean session1 = (Math.abs(xvel) > velThreshold && Math.abs(yvel) < velThreshold) || releaseLeft < -distanceHorizontalShold;
                    left = session1 ? -childWidth : 0;
                } else {
                    boolean session1 = releaseLeft > distanceHorizontalShold;
                    boolean session2 = releaseLeft < -distanceHorizontalShold;
                    left = session1 ? childWidth : (session2 ? -childWidth : 0);
                }
            } else if (mEdgePosition == SwipeConfiguration.BOTTOM) {
                boolean session1 = yvel < 0 && ((Math.abs(yvel) > velThreshold && Math.abs(xvel) < velThreshold) || releaseTop < -distanceVerticalShold);
                boolean session2 = yvel == 0 && releaseTop < -distanceVerticalShold;
                top = session1 || session2 ? -childHeight : 0;
            } else if (mEdgePosition == SwipeConfiguration.TOP) {
                boolean session1 = yvel > 0 && ((Math.abs(yvel) > velThreshold && Math.abs(xvel) < velThreshold) || releaseTop > distanceVerticalShold);
                boolean session2 = yvel == 0 && releaseTop > distanceVerticalShold;
                top = session1 || session2 ? childHeight : 0;
            } else if (mEdgePosition == SwipeConfiguration.VERTICAL) {
                if (yvel > 0) {
                    boolean session1 = (Math.abs(yvel) > velThreshold && Math.abs(xvel) < velThreshold) || releaseTop > distanceVerticalShold;
                    top = session1 ? childHeight : 0;
                } else if (yvel < 0) {
                    boolean session1 = (Math.abs(yvel) > velThreshold && Math.abs(xvel) < velThreshold) || releaseTop < -distanceVerticalShold;
                    top = session1 ? -childHeight : 0;
                } else {
                    boolean session1 = releaseTop > distanceVerticalShold;
                    boolean session2 = releaseTop < -distanceVerticalShold;
                    top = session1 ? childHeight : (session2 ? -childHeight : 0);
                }
            }
            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mListener != null) mListener.onStateChanged(state);
            int position = -1;
            if (mEdgePosition == SwipeConfiguration.LEFT || mEdgePosition == SwipeConfiguration.RIGHT || mEdgePosition == SwipeConfiguration.HORIZONTAL) {
                position = mContentView.getLeft();
            } else if (mEdgePosition == SwipeConfiguration.TOP || mEdgePosition == SwipeConfiguration.BOTTOM || mEdgePosition == SwipeConfiguration.VERTICAL) {
                position = mContentView.getTop();
            }
            switch (state) {
                case ViewDragHelper.STATE_IDLE:
                    if (position == 0) {
                        if (mListener != null) mListener.onOpened();
                    } else {
                        if (mListener != null) mListener.onClosed();
                    }
                    break;
                case ViewDragHelper.STATE_DRAGGING:

                    break;
                case ViewDragHelper.STATE_SETTLING:

                    break;
            }
        }
    }


    public interface OnPanelSlideListener {
        void onStateChanged(int state);

        void onClosed();

        void onOpened();

        void onSlideChange(float percent);
    }

}
