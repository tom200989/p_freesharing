package com.p_freesharing.p_freesharing.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/4/12 0012.
 */

public class ConnView extends RelativeLayout {

    private ImageView ivConnbg;
    private TextView tvConnbg;

    public ConnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.freesharing_widget_free_user_conn, this);
        ivConnbg = (ImageView) findViewById(R.id.iv_view_conn_bg);
        tvConnbg = (TextView) findViewById(R.id.tv_view_conn_text);
    }

    public ConnView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConnView(Context context) {
        this(context, null, 0);
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(String text) {
        tvConnbg.setText(text);
    }

    /**
     * 启动动画(仅动画)
     *
     * @param duration 动画时长
     */
    public void startAnimations(int duration) {
        setVisibility(VISIBLE);
        RotateAnimation ra = new RotateAnimation(0, 360, 1, 0.5f, 1, 0.5f);
        ra.setDuration(duration);
        ra.setFillAfter(true);
        ra.setRepeatMode(Animation.INFINITE);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setInterpolator(new AccelerateDecelerateInterpolator());
        ivConnbg.setAnimation(ra);
        ivConnbg.startAnimation(ra);
    }

    /**
     * 启动动画(动画 + 提示)
     *
     * @param duration 动画时长
     * @param text     提示文本
     */
    public void startAnimations(int duration, String text) {
        setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            tvConnbg.setText(text);
        }
        RotateAnimation ra = new RotateAnimation(0, 360, 1, 0.5f, 1, 0.5f);
        ra.setDuration(duration);
        ra.setFillAfter(true);
        ra.setRepeatMode(Animation.INFINITE);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setInterpolator(new AccelerateDecelerateInterpolator());
        ivConnbg.setAnimation(ra);
        ivConnbg.startAnimation(ra);
    }

    /**
     * 设置自定义view隐藏
     */
    public void setGone() {
        stopAnimation();
        setVisibility(GONE);
    }

    /**
     * 停止动画
     */
    private void stopAnimation() {
        ivConnbg.clearAnimation();
    }
}
