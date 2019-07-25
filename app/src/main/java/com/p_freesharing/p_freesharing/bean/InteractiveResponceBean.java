package com.p_freesharing.p_freesharing.bean;

/*
 * Created by qianli.ma on 2019/7/24 0024.
 */
public class InteractiveResponceBean {

    public static final int DEVICEHELPER_SUCCESS = 1;
    public static final int DEVICEHELPER_APP_ERROR = 2;
    public static final int DEVICEHELPER_FW_ERROR = 3;

    private int type = -1;
    private ConnectedList connectedList;
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ConnectedList getConnectedList() {
        return connectedList;
    }

    public void setConnectedList(ConnectedList connectedList) {
        this.connectedList = connectedList;
    }
}
