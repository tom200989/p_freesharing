package com.p_freesharing.p_freesharing.bean;

/*
 * Created by qianli.ma on 2019/7/24 0024.
 */
public class InteractiveRequestBean {

    public static final int REQ =1;

    private int recevie;// 接收字符: 如果为1, 要求业务马上请求

    public InteractiveRequestBean(int recevie) {
        this.recevie = recevie;
    }

    public int getRecevie() {
        return recevie;
    }

    public void setRecevie(int recevie) {
        this.recevie = recevie;
    }
}
