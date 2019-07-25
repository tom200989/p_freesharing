package com.p_freesharing.p_freesharing.core.server.bean;

import java.util.List;

/**
 * Created by qianli.ma on 2018/4/8 0008.
 */

public class IMResponse {
    
    /**
     * id: 009688
     * ip : 192.168.1.123
     * phonename : RED_MI_4
     * type : 0
     * reback : 200
     * rebackcontent : success
     * filename : ["aaa.jpg"\n"+""bbb.mp4"\n"+""ccc.png"]
     */

    private String id;// 会话ID
    private String ip;// IP地址
    private String phonename;// 手机名称
    private int type;// 接收到的类型
    private int reback;// 响应码
    private String rebackcontent;// 响应内容
    private List<String> filename;// 允许接收的文件名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPhonename() {
        return phonename;
    }

    public void setPhonename(String phonename) {
        this.phonename = phonename;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReback() {
        return reback;
    }

    public void setReback(int reback) {
        this.reback = reback;
    }

    public String getRebackcontent() {
        return rebackcontent;
    }

    public void setRebackcontent(String rebackcontent) {
        this.rebackcontent = rebackcontent;
    }

    public List<String> getFilename() {
        return filename;
    }

    public void setFilename(List<String> filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "IMResponse{" + "id='" + id + '\'' + "\n"+" ip_phone='" + ip + '\'' + "\n"+" phonename='" + phonename + '\'' + "\n"+" type=" + type + "\n"+" reback=" + reback + "\n"+" rebackcontent='" + rebackcontent + '\'' + "\n"+" filename=" + filename + '}';
    }
}
