package com.p_freesharing.p_freesharing.bean;

import android.graphics.Bitmap;

/**
 * Created by qianli.ma on 2018/4/24 0024.
 */

public class Freesharing_FreeTranslist {

    private String picUrl;// 图片连接
    private Bitmap picBitmap;// 图元
    private String thumbUrl;// 缩略图路径
    private String filename;// 文件名
    private String ip;// 192.168.1.132
    private String phone;// MEIZU
    private String size;// 123MB/300MB
    private int state;// 0:传输中 -1:失败 1:成功
    private int progress;// 80
    private boolean isSend;// 发送 or 接收
    private int fileType;// 文件类型 1:图片 2:视频 3:未知
    
    public Freesharing_FreeTranslist() {
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public Bitmap getPicBitmap() {
        return picBitmap;
    }

    public void setPicBitmap(Bitmap picBitmap) {
        this.picBitmap = picBitmap;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
