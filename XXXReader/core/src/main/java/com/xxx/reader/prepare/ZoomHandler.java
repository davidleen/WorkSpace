package com.xxx.reader.prepare;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.DecelerateInterpolator;


import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.xxx.reader.core.IDrawable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 点击缩放处理功能类
 * <p>
 * source code from  {@link  "https://github.com/ruzhan123/ScollZoomListView/edit/master/scrollzoomlist/src/main/java/zhan/scrollzoomlist/ScrollZoomListView.java" }
 * Created by ruzhan on 17/1/9.
 */
public class ZoomHandler implements IZoomHandler {

    private static final int INVALID_POINTER_ID = -1;

    private static final float DEFAULT_MIN_ZOOM_SCALE = 1f;
    private static final float DEFAULT_MAX_ZOOM_SCALE = 2.0f;

    private static final float DEFAULT_NORMAL_SCALE = 1.0f;
    private static final float DEFAULT_ZOOM_SCALE = 2.0f;

    private static final int DEFAULT_ZOOM_TO_SMALL_TIMES = 6;
    private static final int DEFAULT_ZOOM_SCALE_DURATION = 300;
    private static final int DEFAULT_ZOOM_TO_SMALL_SCALE_DURATION = 500;

    private static final int UN_LOADED_POINT = 10000;
    private static final int LOADED_POINT = 10001;

    private static int mActivePointerId = INVALID_POINTER_ID;
    private final Context context;
    private final IDrawable iDrawable;

    private float mScaleFactor = DEFAULT_NORMAL_SCALE;
    private float mLastScale = DEFAULT_NORMAL_SCALE;

    private int mLoadedPointFlag = UN_LOADED_POINT;

    private float mMinZoomScale;
    private float mMaxZoomScale;

    private float mNormalScale;
    private float mZoomScale;

    private int mZoomToSmallTimes;
    private int mZoomScaleDuration;
    private int mZoomToSmallScaleDuration;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetectorCompat mGestureDetectorCompat;

    private float maxWidth = 0.0f;
    private float maxHeight = 0.0f;

    private float mLastTouchX;
    private float mLastTouchY;

    private float mTranslateX;
    private float mTranslateY;

    private float mWidth;
    private float mHeight;

    private float mCenterX;
    private float mCenterY;

    private boolean isScaling = false;
    private boolean isPointerDown = false;

    private ValueAnimator mZoomValueAnimator;

    private ValueAnimator mResetPositionValueAnimator;

    //synchronous   Zoom ScaleGestureDetector
    private List<ScaleGestureDetector.SimpleOnScaleGestureListener> mOnScaleGestureListeners = new ArrayList<>();

    //synchronous   Zoom GestureDetector
    private List<GestureDetector.SimpleOnGestureListener> mSimpleOnGestureListeners = new ArrayList<>();

    //synchronous   Zoom Animation
    private List<OnZoomListener> mZoomListeners = new ArrayList<>();

    private LinkedList<PointF> mLinkPoints = new LinkedList<>();

    public ZoomHandler(Context context, IDrawable iDrawable) {
        this.context = context;
        this.iDrawable = iDrawable;

        init(context);
    }


    @Override
    public void zoom(Canvas canvas) {


        canvas.translate(mTranslateX, mTranslateY);
        canvas.scale(mScaleFactor, mScaleFactor);

    }

    /**
     * 缩小时,调整位置为可能会出现被平移出页面的情况,再缩放完成后做一次平移处理
     */
    public void resetPosition(){
        final float lastX = mTranslateX;
        final float lastY = mTranslateY;

        if (mResetPositionValueAnimator == null) {

            mResetPositionValueAnimator =  ValueAnimator.ofFloat(1.f, 0);
            mResetPositionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    Object animatedValue = valueAnimator.getAnimatedValue();
                    float factor = valueAnimator.getAnimatedFraction();
                    float realFactor = 1 - factor;

                    if (lastY != 0){
                        mTranslateY = lastY * realFactor;
                    }
                    if (lastX != 0){
                        mTranslateX = lastX * realFactor;
                    }
                    invalidate();
                }
            });
        }else{
            if (mResetPositionValueAnimator.isRunning()) {
                mResetPositionValueAnimator.cancel();
            }
        }
        mResetPositionValueAnimator.start();
    }

    private void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener()){
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return super.onTouchEvent(event);
            }
        };
        mGestureDetectorCompat =
                new GestureDetectorCompat(context, new ScrollReaderViewGestureListener());


        mMinZoomScale = DEFAULT_MIN_ZOOM_SCALE;
        mMaxZoomScale =
                DEFAULT_MAX_ZOOM_SCALE;
        mNormalScale = DEFAULT_NORMAL_SCALE;
        mZoomScale = DEFAULT_ZOOM_SCALE;

        mZoomToSmallTimes =
                DEFAULT_ZOOM_TO_SMALL_TIMES;
        mZoomScaleDuration = DEFAULT_ZOOM_SCALE_DURATION;
        mZoomToSmallScaleDuration = DEFAULT_ZOOM_TO_SMALL_SCALE_DURATION;


    }


    @Override
    public void clear() {
        if (mZoomValueAnimator != null) {
            mZoomValueAnimator.cancel();
        }

        //remove all listener
        removeOnScaleGestureListeners();
        removeOnSimpleOnGestureListeners();
        removeOnZoomListeners();
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {

        boolean b = mScaleDetector.onTouchEvent(ev);


        boolean b1 = mGestureDetectorCompat.onTouchEvent(ev);

        int action = ev.getAction();
        if (( isPointerDown && (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE)) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

                isPointerDown = false;

                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();

                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {

                isPointerDown = true;
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                int pointerIndex = ev.findPointerIndex(mActivePointerId);

                float x, y;

                try {

                    x = ev.getX(pointerIndex);
                    y = ev.getY(pointerIndex);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();

                    return false;
                }

                float dx = (x - mLastTouchX);
                float dy = (y - mLastTouchY);

                //ACTION_POINTER_DOWN   more distance * 6
                if (isPointerDown) {
                    dx = dx * mZoomToSmallTimes;
                    dy = dy * mZoomToSmallTimes;
                }

                if (isScaling) {  //  status is scaling
                    float offsetX = mCenterX * (mLastScale - mScaleFactor);
                    float offsetY = mCenterY * (mLastScale - mScaleFactor);

                    //mTranslateX += offsetX;
                    //mTranslateY += offsetY;

                    //checkPointF(UN_LOADED_POINT, offsetX, offsetY);

                    //mLastScale = mScaleFactor;
                } else if (mScaleFactor > mNormalScale) {   //  not scaling, move ...
                    if (mResetPositionValueAnimator != null && mResetPositionValueAnimator.isRunning()) {

                    }else{
                        mTranslateX += dx;
                        mTranslateY += dy;
                        checkPointF(UN_LOADED_POINT, dx, dy);

                        correctTranslateValue();
                    }

                }

                mLastTouchX = x;
                mLastTouchY = y;
                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {

                mActivePointerId = INVALID_POINTER_ID;
                if (mScaleFactor == DEFAULT_NORMAL_SCALE && (mTranslateX != 0 || mTranslateY != 0)){
                    resetPosition();
                }

                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = ev.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId) {
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }

                break;
            }
        }

        return false;
    }

    private void invalidate() {

        if (iDrawable != null) iDrawable.updateView();

    }

    private void correctTranslateValue() {
        if (mTranslateX > 0.0f) {
            mTranslateX = 0.0f;
        } else if (mTranslateX < maxWidth) {
            mTranslateX = maxWidth;
        }

        if (mTranslateY > 0.0f) {
            mTranslateY = 0.0f;
        } else if (mTranslateY < maxHeight) {
            mTranslateY = maxHeight;
        }
    }

    @Override
    public void setSize(int width, int height) {

        this.mWidth = width;
        this.mHeight = height;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            for (ScaleGestureDetector.OnScaleGestureListener listener : mOnScaleGestureListeners) {
                listener.onScaleBegin(detector);
            }

            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float mScale = mScaleFactor;
            mScale *= detector.getScaleFactor();
            if(mScale <1){
                mScaleFactor = DEFAULT_MIN_ZOOM_SCALE;
                return true;
            }else{
                if (Math.abs(mScaleFactor - mScale)<0.01) {
                    return true;
                }
                mScaleFactor = mScale;
            }

            float minFactor = Math.min(mScaleFactor, mMaxZoomScale);
            mScaleFactor = Math.max(mMinZoomScale, minFactor);

            maxWidth = mWidth - (mWidth * mScaleFactor);
            maxHeight = mHeight - (mHeight * mScaleFactor);

            mCenterX = detector.getFocusX();
            mCenterY = detector.getFocusY();

            float offsetX = mCenterX * (mLastScale - mScaleFactor);
            float offsetY = mCenterY * (mLastScale - mScaleFactor);


            mTranslateX += offsetX;
            mTranslateY += offsetY;
            checkPointF(UN_LOADED_POINT, offsetX, offsetY);

            mLastScale = ZoomHandler.this.mScaleFactor;

            isScaling = true;
            invalidate();

            for (ScaleGestureDetector.OnScaleGestureListener listener : mOnScaleGestureListeners) {
                listener.onScale(detector);
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (mScaleFactor < mNormalScale) {
                zoomList(mScaleFactor, mNormalScale, mZoomToSmallScaleDuration, LOADED_POINT);
            }

            isScaling = false;




            for (ScaleGestureDetector.OnScaleGestureListener listener : mOnScaleGestureListeners) {
                listener.onScaleEnd(detector);
            }

        }
    }

    private class ScrollReaderViewGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            //list view scroll call back... to outside
            for (GestureDetector.SimpleOnGestureListener listener : mSimpleOnGestureListeners) {
                listener.onScroll(e1, e2, distanceX, distanceY);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {  // single click event,double call no call single

            for (GestureDetector.SimpleOnGestureListener listener : mSimpleOnGestureListeners) {
                listener.onSingleTapConfirmed(e);
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {  //double click event

            if (mNormalScale < mScaleFactor) {
                zoomList(mScaleFactor, mNormalScale, mZoomScaleDuration, LOADED_POINT);

            } else if (mScaleFactor == mNormalScale) {

                mCenterX = e.getX();
                mCenterY = e.getY();

                zoomList(mScaleFactor, mZoomScale, mZoomScaleDuration, UN_LOADED_POINT);
            }

            for (GestureDetector.SimpleOnGestureListener listener : mSimpleOnGestureListeners) {
                listener.onDoubleTap(e);
            }
            return super.onDoubleTap(e);
        }
    }

    //let   zoom func
    private void zoomList(float startValue, float endValue, int duration, final int loadedPointFlag) {
        if (mZoomValueAnimator == null) {
            mZoomValueAnimator = new ValueAnimator();
            mZoomValueAnimator.setInterpolator(new DecelerateInterpolator());

            mZoomValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    mScaleFactor = (Float) animation.getAnimatedValue();

                    float dx = mCenterX * (mLastScale - mScaleFactor);
                    float dy = mCenterY * (mLastScale - mScaleFactor);

                    PointF pointF = checkPointF(loadedPointFlag, dx, dy);

                    if (pointF != null) {
                        dx = -pointF.x;
                        dy = -pointF.y;
                    }

                    mTranslateX += dx;
                    mTranslateY += dy;

                    maxWidth = mWidth - (mWidth * mScaleFactor);
                    maxHeight = mHeight - (mHeight * mScaleFactor);

                    correctZoomTranslateValue();
                    invalidate();

                    mLastScale = mScaleFactor;

                    for (OnZoomListener listener : mZoomListeners) {
                        listener.onViewZoomUpdate(animation, mTranslateX, mTranslateY,
                                mScaleFactor, mScaleFactor);
                    }
                }
            });

            mZoomValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isScaling = true;

                    for (OnZoomListener listener : mZoomListeners) {
                        listener.onViewStart();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isScaling = false;

                    mLoadedPointFlag = loadedPointFlag == UN_LOADED_POINT ? LOADED_POINT : UN_LOADED_POINT;

                    for (OnZoomListener listener : mZoomListeners) {
                        listener.onViewCancel();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isScaling = false;

                    for (OnZoomListener listener : mZoomListeners) {
                        listener.onViewCancel();
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        if (!mZoomValueAnimator.isRunning()) {
            mZoomValueAnimator.setFloatValues(startValue, endValue);
            mZoomValueAnimator.setDuration(duration);
            mZoomValueAnimator.start();
        }
    }

    private void correctZoomTranslateValue() {
        if (mTranslateX > 0.0f) { //zoom +

            if (mScaleFactor >= mNormalScale) {  //params correct
                mTranslateX = 0.0f;
            }
        } else if (mTranslateX < maxWidth) { //zoom -

            if (mScaleFactor >= mNormalScale) { //params correct
                mTranslateX = maxWidth;
            }
        }

        if (mTranslateY > 0.0f) { //zoom +

            if (mScaleFactor >= mNormalScale) {  //params correct
                mTranslateY = 0.0f;
            }
        } else if (mTranslateY < maxHeight) { //zoom -

            if (mScaleFactor >= mNormalScale) { //params correct
                mTranslateY = maxHeight;
            }
        }
    }

    private void putPointF(float dx, float dy) {
        if (mLoadedPointFlag == UN_LOADED_POINT) {
            PointF pointF = new PointF(dx, dy);
            mLinkPoints.addFirst(pointF);
        }
    }

    private PointF getPointF() {
        if (mLoadedPointFlag == LOADED_POINT) {
            return mLinkPoints.getLast();
        }

        return null;
    }

    private PointF checkPointF(int loadedPointFlag, float dx, float dy) {
        PointF pointF = null;

        if (loadedPointFlag == UN_LOADED_POINT) {
            putPointF(dx, dy);

        } else if (loadedPointFlag == LOADED_POINT) {
            pointF = getPointF();

        } else {
            throw new RuntimeException("Zoom  loaded points error ! ! !");
        }
        return pointF;
    }

    public float getMinZoomScale() {
        return mMinZoomScale;
    }

    public void setMinZoomScale(float mMinZoomScale) {
        this.mMinZoomScale = mMinZoomScale;
    }

    public float getMaxZoomScale() {
        return mMaxZoomScale;
    }

    public void setMaxZoomScale(float mMaxZoomScale) {
        this.mMaxZoomScale = mMaxZoomScale;
    }

    public float getNormalScale() {
        return mNormalScale;
    }

    public void setNormalScale(float mNormalScale) {
        this.mNormalScale = mNormalScale;
    }

    public float getZoomScale() {
        return mZoomScale;
    }

    public void setZoomScale(float mZoomScale) {
        this.mZoomScale = mZoomScale;
    }

    public int getZoomToSmallTimes() {
        return mZoomToSmallTimes;
    }

    public void setZoomToSmallTimes(int mZoomToSmallTimes) {
        this.mZoomToSmallTimes = mZoomToSmallTimes;
    }

    public int getZoomScaleDuration() {
        return mZoomScaleDuration;
    }

    public void setZoomScaleDuration(int mZoomScaleDuration) {
        this.mZoomScaleDuration = mZoomScaleDuration;
    }

    @Override
    public void addOnScaleGestureListener(ScaleGestureDetector.SimpleOnScaleGestureListener listener) {
        if (listener != null) {
            if (!mOnScaleGestureListeners.contains(listener)) {
                mOnScaleGestureListeners.add(listener);
            }
        }
    }

    @Override
    public void removeOnScaleGestureListener(ScaleGestureDetector.SimpleOnScaleGestureListener listener) {
        if (listener != null) {
            if (mOnScaleGestureListeners.contains(listener)) {
                mOnScaleGestureListeners.remove(listener);
            }
        }
    }

    @Override
    public void removeOnScaleGestureListeners() {
        while (!mOnScaleGestureListeners.isEmpty()) {
            mOnScaleGestureListeners.remove(0);
        }
    }

    @Override
    public void setSimpleOnGestureListener(GestureDetector.SimpleOnGestureListener listener) {
        if (listener != null) {
            if (!mSimpleOnGestureListeners.contains(listener)) {
                mSimpleOnGestureListeners.add(listener);
            }
        }
    }

    @Override
    public void removeOnSimpleOnGestureListener(GestureDetector.SimpleOnGestureListener listener) {
        if (listener != null) {
            if (mSimpleOnGestureListeners.contains(listener)) {
                mSimpleOnGestureListeners.remove(listener);
            }
        }
    }

    @Override
    public void removeOnSimpleOnGestureListeners() {
        while (!mSimpleOnGestureListeners.isEmpty()) {
            mSimpleOnGestureListeners.remove(0);
        }
    }

    @Override
    public void setOnZoomListener(IZoomHandler.OnZoomListener listener) {
        if (listener != null) {
            if (!mZoomListeners.contains(listener)) {
                mZoomListeners.add(listener);
            }
        }
    }

    @Override
    public void removeOnZoomListener(IZoomHandler.OnZoomListener listener) {
        if (listener != null) {
            if (mZoomListeners.contains(listener)) {
                mZoomListeners.remove(listener);
            }
        }
    }



    public void removeOnZoomListeners() {
        while (!mZoomListeners.isEmpty()) {
            mZoomListeners.remove(0);
        }
    }




}