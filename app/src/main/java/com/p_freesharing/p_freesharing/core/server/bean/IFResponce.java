package com.p_freesharing.p_freesharing.core.server.bean;

/**
 * Created by qianli.ma on 2018/4/17 0017.
 */

public class IFResponce {
    /**
     * id: 009688
     * ip_phone : 192.168.1.123
     * phonename : RED_MI_4
     * type : 0
     * path : /storage/0/mnt/smartlink/png
     * size : 10234746478
     * reback : 200
     * rebackcontent : success
     */
    
    private String id;
    private String ip;
    private String phonename;
    private int type;
    private long size;
    private String path;
    private int reback;
    private String rebackcontent;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
