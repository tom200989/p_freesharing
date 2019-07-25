package com.p_freesharing.p_freesharing.bean;

/**
 * Created by qianli.ma on 2018/4/10 0010.
 */

public class Freesharing_FreeUser {

    private boolean isConnecting;// 是否正在连接
    private String ip;// 连接IP
    private String phoneName;// 连接手机名
    private boolean isCanClick;// 是否可点(当其中一个item被点击时, 把所有的item都设置为不可点)

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    public boolean isCanClick() {
        return isCanClick;
    }

    public void setCanClick(boolean canClick) {
        isCanClick = canClick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }
}
