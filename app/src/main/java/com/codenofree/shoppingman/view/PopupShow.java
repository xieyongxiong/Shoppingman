package com.codenofree.shoppingman.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;



/**
 * Created by xieyongxiong on 2018/4/11.
 */

public class PopupShow {
    public static void show(Context context, final View view,
                            long from, long to, final int duration, final String direction) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofInt(view, "bottomMargin",
                -dip2px(context, from), dip2px(context, to));
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                switch (direction) {
                    case "left":
                        params.leftMargin = value;
                        break;
                    case "right":
                        params.rightMargin = value;
                        break;
                    case "top":
                        params.topMargin = value;
                        break;
                    case "bottom":
                        params.bottomMargin = value;
                        break;
                    default:
                        break;
                }
                view.setLayoutParams(params);
                view.invalidate();
            }
        });
        animation.setDuration(duration);
        animation.start();
    }

    public static void hide(Context context, final View view, long from,
                            long to, final int duration, final String direction) {
        ObjectAnimator reverseanimation = ObjectAnimator.ofInt(view, "bottomMargin",
                dip2px(context, from), -dip2px(context, to));
        reverseanimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                switch (direction) {
                    case "left":
                        params.leftMargin = value;
                        break;
                    case "right":
                        params.rightMargin = value;
                        break;
                    case "top":
                        params.topMargin = value;
                        break;
                    case "bottom":
                        params.bottomMargin = value;
                        break;
                    default:
                        break;
                }
                view.setLayoutParams(params);
                view.invalidate();
            }
        });
        reverseanimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reverseanimation.setDuration(duration);
        reverseanimation.start();
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
