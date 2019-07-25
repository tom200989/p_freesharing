package com.p_freesharing.p_freesharing.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/5/14 0014.
 */

public class InterruptView extends RelativeLayout {

    private ImageView ivbg;
    private View tvCancel;
    private View tvOk;

    public InterruptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.freesharing_widget_interrupt_view, this);
        ivbg = (ImageView) findViewById(R.id.iv_interrupt_bg);
        tvCancel = findViewById(R.id.tv_interrupt_cancel);
        tvOk = findViewById(R.id.tv_interrupt_ok);
        ivbg.setOnClickListener(this::clickbgNext);
        tvCancel.setOnClickListener(this::clickCancelNext);
        tvOk.setOnClickListener(this::clickOkNext);
    }

    public InterruptView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterruptView(Context context) {
        this(context, null, 0);
    }

    private OnClickOkListener onClickOkListener;

    // 接口OnClickOkListener
    public interface OnClickOkListener {
        void clickOk(View view);
    }

    // 对外方式setOnClickOkListener
    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    // 封装方法clickOkNext
    private void clickOkNext(View attr) {
        if (onClickOkListener != null) {
            onClickOkListener.clickOk(attr);
        }
    }

    private OnClickCancelListener onClickCancelListener;

    // 接口OnClickCancelListener
    public interface OnClickCancelListener {
        void clickCancel(View view);
    }

    // 对外方式setOnClickCancelListener
    public void setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
    }

    // 封装方法clickCancelNext
    private void clickCancelNext(View attr) {
        if (onClickCancelListener != null) {
            onClickCancelListener.clickCancel(attr);
        }
    }

    private OnClickbgListener onClickbgListener;

    // 接口OnClickbgListener
    public interface OnClickbgListener {
        void clickbg(View view);
    }

    // 对外方式setOnClickbgListener
    public void setOnClickbgListener(OnClickbgListener onClickbgListener) {
        this.onClickbgListener = onClickbgListener;
    }

    // 封装方法clickbgNext
    private void clickbgNext(View attr) {
        if (onClickbgListener != null) {
            onClickbgListener.clickbg(attr);
        }
    }


}
