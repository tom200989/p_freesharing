package com.p_freesharing.p_freesharing.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/4/23 0023.
 */

public class CancelView extends RelativeLayout {

    private View cancelView;
    private ImageView ivbg;
    private TextView tvCancel;
    private TextView tvOk;

    public CancelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.freesharing_widget_cancel_view, this);
        ivbg = (ImageView) findViewById(R.id.iv_cancelview_bg);
        tvCancel = (TextView) findViewById(R.id.tv_cancelview_cancel);
        tvOk = (TextView) findViewById(R.id.tv_cancelview_ok);
        ivbg.setOnClickListener(v -> bgClickNext());
        tvCancel.setOnClickListener(v -> cancelClickNext());
        tvOk.setOnClickListener(v -> okClickNext());
    }

    public CancelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CancelView(Context context) {
        this(context, null, 0);
    }

    private OnOkClickListener onOkClickListener;

    // 接口OnOkClickListener
    public interface OnOkClickListener {
        void okClick();
    }

    // 对外方式setOnOkClickListener
    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    // 封装方法okClickNext
    private void okClickNext() {
        if (onOkClickListener != null) {
            onOkClickListener.okClick();
        }
    }

    private OnCancelClickListener onCancelClickListener;

    // 接口OnCancelClickListener
    public interface OnCancelClickListener {
        void cancelClick();
    }

    // 对外方式setOnCancelClickListener
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    // 封装方法cancelClickNext
    private void cancelClickNext() {
        if (onCancelClickListener != null) {
            onCancelClickListener.cancelClick();
        }
    }

    private OnBgClickListener onBgClickListener;

    // 接口OnBgClickListener
    public interface OnBgClickListener {
        void bgClick();
    }

    // 对外方式setOnBgClickListener
    public void setOnBgClickListener(OnBgClickListener onBgClickListener) {
        this.onBgClickListener = onBgClickListener;
    }

    // 封装方法bgClickNext
    private void bgClickNext() {
        if (onBgClickListener != null) {
            onBgClickListener.bgClick();
        }
    }
}
