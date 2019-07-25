package com.p_freesharing.p_freesharing.helper;

import com.p_freesharing.p_freesharing.bean.ConnectedList;
import com.p_freesharing.p_freesharing.bean.InteractiveResponceBean;

import static com.p_freesharing.p_freesharing.bean.InteractiveResponceBean.*;

/**
 * Created by qianli.ma on 2018/4/11 0011.
 */

public class DeviceHelper {

    private InteractiveResponceBean responceBean;

    public DeviceHelper(InteractiveResponceBean responceBean) {
        this.responceBean = responceBean;
    }

    public void getDevices() {
        int type = responceBean.getType();
        if (type == DEVICEHELPER_SUCCESS) {// 成功
            getDevicesSuccessNext(responceBean.getConnectedList());

        } else if (type == DEVICEHELPER_APP_ERROR) {// APP异常
            getDevicesErrorNext(responceBean.getErrorMsg());

        } else if (type == DEVICEHELPER_FW_ERROR) {// FW异常
            getDevicesResultErrorNext(responceBean.getErrorMsg());
        }
    }

    private OnGetDevicesSuccessListener onGetDevicesSuccessListener;

    // Inteerface--> 接口OnGetDevicesSuccessListener
    public interface OnGetDevicesSuccessListener {
        void getDevicesSuccess(ConnectedList attr);
    }

    // 对外方式setOnGetDevicesSuccessListener
    public void setOnGetDevicesSuccessListener(OnGetDevicesSuccessListener onGetDevicesSuccessListener) {
        this.onGetDevicesSuccessListener = onGetDevicesSuccessListener;
    }

    // 封装方法getDevicesSuccessNext
    private void getDevicesSuccessNext(ConnectedList attr) {
        if (onGetDevicesSuccessListener != null) {
            onGetDevicesSuccessListener.getDevicesSuccess(attr);
        }
    }

    private OnGetDevicesErrorListener onGetDevicesErrorListener;

    // Inteerface--> 接口OnGetDevicesErrorListener
    public interface OnGetDevicesErrorListener {
        void getDevicesError(String err);
    }

    // 对外方式setOnGetDevicesErrorListener
    public void setOnGetDevicesErrorListener(OnGetDevicesErrorListener onGetDevicesErrorListener) {
        this.onGetDevicesErrorListener = onGetDevicesErrorListener;
    }

    // 封装方法getDevicesErrorNext
    private void getDevicesErrorNext(String err) {
        if (onGetDevicesErrorListener != null) {
            onGetDevicesErrorListener.getDevicesError(err);
        }
    }

    private OnGetDevicesResultErrorListener onGetDevicesResultErrorListener;

    // Inteerface--> 接口OnGetDevicesResultErrorListener
    public interface OnGetDevicesResultErrorListener {
        void getDevicesResultError(String fwErr);
    }

    // 对外方式setOnGetDevicesResultErrorListener
    public void setOnGetDevicesResultErrorListener(OnGetDevicesResultErrorListener onGetDevicesResultErrorListener) {
        this.onGetDevicesResultErrorListener = onGetDevicesResultErrorListener;
    }

    // 封装方法getDevicesResultErrorNext
    private void getDevicesResultErrorNext(String fwErr) {
        if (onGetDevicesResultErrorListener != null) {
            onGetDevicesResultErrorListener.getDevicesResultError(fwErr);
        }
    }
}
