package com.idea.anim.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * @Author: jijie
 * Description:三个球转圈
 * Date: Create at 21:31 2018/6/22 0022
 * Modified:
 */
public class CircleMotionDrawable extends SuperDrawable {

    private float[] translateX = new float[3];

    private float[] translateY = new float[3];

    @Override
    public void draw(Canvas canvas, Paint paint) {

        for (int i = 0; i < 3; i++) {
            canvas.save();
            canvas.translate(translateX[i], translateY[i]);
            canvas.drawCircle(0, 0, getWidth() / 3.5f, paint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> createAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        float startX = getWidth() / 2.5f;
        float startY = getHeight() / 2.5f;

        handleAnimatorsValue(animators, startX, startY);
        return animators;
    }


    //设置估值器和插值器数据
    //三个圈的移动范围不能超出drawable面积
    private void handleAnimatorsValue(ArrayList<ValueAnimator> animators, float startX, float startY) {

        ValueAnimator translateXAnim = null;

        ValueAnimator translateYAnim = null;
        for (int i = 0; i < 3; i++) {
            final int index = i;

            switch (i)
            {
              case 0:
                translateXAnim =
                        ValueAnimator.ofFloat(getWidth() >> 1, getWidth() - startX, startX, getWidth() >> 1);

                translateYAnim =
                        ValueAnimator.ofFloat(startY, getHeight() - startY, getHeight() - startY, startY);
                break;

              case 1:
                translateXAnim =
                        ValueAnimator.ofFloat(getWidth() - startX, startX, getWidth() >> 1, getWidth() - startX);

                translateYAnim = ValueAnimator.ofFloat(getHeight() - startY, getHeight() - startY, startY,
                        getHeight() - startY);
                break;

              case 2:
                translateXAnim = ValueAnimator.ofFloat(startX, getWidth() >> 1, getWidth() - startX, startX);

                translateYAnim = ValueAnimator.ofFloat(getHeight() - startY, startY, getHeight() - startY,
                        getHeight() - startY);
                break;


            }

            handleInterpolatorValue(animators, index, translateXAnim, translateYAnim);
        }
    }


    private void handleInterpolatorValue(ArrayList<ValueAnimator> animators, int index, ValueAnimator translateXAnim, ValueAnimator translateYAnim) {
        setAnimOption(index, translateXAnim, translateX);
        setAnimOption(index, translateYAnim, translateY);

        animators.add(translateXAnim);
        animators.add(translateYAnim);
    }

    //设置动画的基本参数：时长，插值器、和频次等等
    private void setAnimOption(int index, ValueAnimator translateYAnim, float[] translateY) {
        translateYAnim.setDuration(2500);
        translateYAnim.setInterpolator(new LinearInterpolator());
        translateYAnim.setRepeatCount(-1);
        addUpdateListener(translateYAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translateY[index] = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

}
