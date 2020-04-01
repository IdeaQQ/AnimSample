package com.idea.anim.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: jijie
 * Description:
 * Date: Create at 21:31 2018/6/22 0022
 * Modified:
 */
public abstract class SuperDrawable extends Drawable implements Animatable {

    private HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mListenerHashMap =
            new HashMap<>();

    private ArrayList<ValueAnimator> mAnimators;
    private int mAlpha = 255;
    private static final Rect RECT = new Rect();
    private Rect mBoundsRect = RECT;

    private boolean mHasAnimators;

    private Paint mPaint = new Paint();

    SuperDrawable() {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    void setColor(int color) {
        mPaint.setColor(color);
    }

    void setStrokeWidth(int width) {
        mPaint.setStrokeWidth(width);
    }

    @Override
    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void draw(Canvas canvas) {
        draw(canvas, mPaint);
    }

    public abstract void draw(Canvas canvas, Paint paint);

    //创建动画集
    public abstract ArrayList<ValueAnimator> createAnimators();

    @Override
    public void start() {
        initAnimators();

        if (mAnimators == null) {
            return;
        }

        // 动画未结束，不继续
        if (isStarted()) {
            return;
        }
        startAnimators();
        invalidateSelf();
    }


    //开始动画。如果动画是重新运行的，需要重新设置监听
    private void startAnimators() {
        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);
            ValueAnimator.AnimatorUpdateListener updateListener = mListenerHashMap.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }

            animator.start();
        }
    }

    private void stopAnimators() {
        if (mAnimators != null) {
            for (ValueAnimator animator : mAnimators) {
                if (animator != null && animator.isStarted()) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
    }

    //初始化动画集
    private void initAnimators() {
        if (!mHasAnimators) {
            mAnimators = createAnimators();
            mHasAnimators = true;
        }
    }

    @Override
    public void stop() {
        stopAnimators();
    }

    private boolean isStarted() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isStarted();
        }
        return false;
    }

    @Override
    public boolean isRunning() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isRunning();
        }
        return false;
    }


    void addUpdateListener(ValueAnimator animator,
                           ValueAnimator.AnimatorUpdateListener updateListener) {
        mListenerHashMap.put(animator, updateListener);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        setBoundsRect(bounds);
    }

    public void setBoundsRect(Rect boundsRect) {
        setDrawBounds(boundsRect.left, boundsRect.top, boundsRect.right, boundsRect.bottom);
    }

    public void setDrawBounds(int left, int top, int right, int bottom) {
        this.mBoundsRect = new Rect(left, top, right, bottom);
    }

    public void postInvalidate() {
        invalidateSelf();
    }


    public int getWidth() {
        return mBoundsRect.width();
    }

    public int getHeight() {
        return mBoundsRect.height();
    }


}
