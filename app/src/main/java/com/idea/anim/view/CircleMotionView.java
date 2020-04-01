package com.idea.anim.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.idea.anim.R;


public class CircleMotionView extends View {

    private static final String TAG = "CircleMotionView";

    int mMinWidth = 50;
    int mMaxWidth = 400;
    int mMinHeight = 50;
    int mMaxHeight = 400;

    //三个圈的drawable
    private CircleMotionDrawable mMotionDrawable;

    //圈圈颜色
    private int mCircleColor;

    //动画开关
    private boolean mShouldStartAnimation;

    public CircleMotionView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public CircleMotionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, R.style.CircleMotionView);
    }

    public CircleMotionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.CircleMotionView);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {


        final TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.CircleMotionView, defStyleAttr,
                        defStyleRes);

        mMinWidth = a.getDimensionPixelSize(R.styleable.CircleMotionView_minWidth, mMinWidth);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.CircleMotionView_maxWidth, mMaxWidth);
        mMinHeight = a.getDimensionPixelSize(R.styleable.CircleMotionView_minHeight, mMinHeight);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.CircleMotionView_maxHeight, mMaxHeight);
        mCircleColor = a.getColor(R.styleable.CircleMotionView_CircleColor, Color.WHITE);
        if (mMotionDrawable == null) {
            mMotionDrawable = new CircleMotionDrawable();
            setDrawable();
        }
        a.recycle();
    }

    private void setDrawable() {

        setCircleColor(mCircleColor);
        if (mMotionDrawable != null) {
            mMotionDrawable.setCallback(this);
        }
        postInvalidate();

    }

    /**
     * 设置圆圈颜色
     *
     * @param color
     */
    public void setCircleColor(int color) {
        this.mCircleColor = color;
        mMotionDrawable.setColor(color);
    }


    //显示动画
    public void smoothToShow() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        setVisibility(VISIBLE);
    }

    //隐藏动画
    public void smoothToHide() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        setVisibility(GONE);
    }


    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mMotionDrawable || super.verifyDrawable(who);
    }

    public void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }

        if (mMotionDrawable != null) {
            mShouldStartAnimation = true;
        }
        postInvalidate();
    }

    public void stopAnimation() {
        if (mMotionDrawable != null) {
            mMotionDrawable.stop();
            mShouldStartAnimation = false;
        }
        postInvalidate();
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        //验证当前drawable
        if (verifyDrawable(drawable)) {
            final Rect bound = drawable.getBounds();
            final int scrollX = getScrollX() + getPaddingLeft();
            final int scrollY = getScrollY() + getPaddingTop();

            invalidate(bound.left + scrollX, bound.top + scrollY, bound.right + scrollX,
                    bound.bottom + scrollY);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {
        w -= getPaddingRight() + getPaddingLeft();
        h -= getPaddingTop() + getPaddingBottom();

        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;

        if (mMotionDrawable != null) {
            mMotionDrawable.setBounds(left, top, right, bottom);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    void drawView(Canvas canvas) {
        final Drawable drawable = mMotionDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
            if (mShouldStartAnimation) {
                ((Animatable) drawable).start();
                mShouldStartAnimation = false;
            }
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int drawableWidth = 0;
        int drawableHeight = 0;

        final Drawable drawable = mMotionDrawable;
        if (drawable != null) {
            drawableWidth = Math.max(mMinWidth, Math.min(mMaxWidth, drawable.getIntrinsicWidth()));
            drawableHeight = Math.max(mMinHeight, Math.min(mMaxHeight, drawable.getIntrinsicHeight()));
        }

        drawableWidth += getPaddingLeft() + getPaddingRight();
        drawableHeight += getPaddingTop() + getPaddingBottom();

        final int measuredWidth = resolveSizeAndState(drawableWidth, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(drawableHeight, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (mMotionDrawable != null) {
            mMotionDrawable.setHotspot(x, y);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }


}
