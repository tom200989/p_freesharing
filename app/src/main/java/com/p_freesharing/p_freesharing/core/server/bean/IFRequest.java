package com.p_freesharing.p_freesharing.core.server.bean;

/**
 * Created by qianli.ma on 2018/4/17 0017.
 */

public class IFRequest {
    
    /**
     * id: 009688
     * ip : 192.168.1.123
     * phonename : RED_MI_4
     * requestType : 0
     * size : 100254223365
     * path : /storage/0/mnt/smartlink/png (客户端)
     * filetype : 1:图片 2:视频 3:未知
     * fileName : 文件名
     * system : 系统 安卓=0 苹果=1
     * mimetype : 元类型 video/mp4 jpeg/jpg...
     */
    
    private String id;// 会话ID
    private String ip;// 客户端IP
    private String phonename;// 客户端手机名
    private int requestType;// 请求类型: 会话,OK,NOK...
    private long size;// 请求上传的文件大小
    private String path;// 客户端文件路径
    private int fileType;// 客户端文件类型  1:图片 2:视频 3:未知
    private String fileName;// 文件名(注意: 这个不一定和path相同)
    private int system;// 手机类型:Android--> 0; IOS:1
    private String mimetype;// 元类型 video/mp4 jpeg/jpg...

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
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

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "IFRequest{" + "id='" + id + '\'' + ", ip='" + ip + '\'' + ", phonename='" + phonename + '\'' + ", requestType=" + requestType + ", size=" + size + ", path='" + path + '\'' + ", fileType=" + fileType + ", fileName='" + fileName + '\'' + ", system=" + system + ", mimetype='" + mimetype + '\'' + '}';
    }
}
