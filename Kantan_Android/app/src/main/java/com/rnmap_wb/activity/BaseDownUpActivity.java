package com.rnmap_wb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.rnmap_wb.R;


/**
 * Created by Administrator on 2017/7/27.
 */

public abstract class BaseDownUpActivity extends Activity {

    private TranslateAnimation enterAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            , Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0.0f);
    private TranslateAnimation exitAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            , Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);

    private ViewGroup mainGroup;
    private boolean isExitAnimRunning = false;
    private long animDuration = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Translucent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_down_up_layout);
        initView();
        doOnCreate(savedInstanceState);
        initAnim();
    }


    private void initAnim() {
        mainGroup.setAnimation(enterAnimation);
        enterAnimation.setDuration(animDuration);
        enterAnimation.start();
        exitAnimation.setDuration(animDuration);
        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isExitAnimRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                isExitAnimRunning = false;
                BaseDownUpActivity.super.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void initView() {
        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mainGroup = (ViewGroup) findViewById(R.id.main_layout);
        mainGroup.addView(createView());

    }


    @Override
    public void finish() {
        if (exitAnimation != null) {
            if (!isExitAnimRunning) {
                exitAnimation.setDuration(animDuration);
                exitAnimation.setFillAfter(true);
                mainGroup.setAnimation(exitAnimation);
//                exitAnimation.start();
                mainGroup.startAnimation(exitAnimation);
            }
        } else {
            super.finish();
        }

    }

    protected void setDuration(long duration) {
        animDuration = duration;

    }

    protected abstract View createView();

    protected abstract void doOnCreate(Bundle savedInstanceState);




}
