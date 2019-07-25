package com.p_freesharing.p_freesharing.core.server.bean;

import java.util.List;

/**
 * Created by qianli.ma on 2018/4/8 0008.
 */

public class IMRequest {

    /**
     * id: 009688
     * ip : 192.168.1.123
     * phonename : RED_MI_4
     * type : 0
     * pathList : ["/storage/mmt/0/aaa.jpg","/storage/mmt/0/bbb.mp4","/storage/mmt/0/ccc.png"]
     * attach : 0
     */

    private String id;// 本次会话ID
    private String ip;// IP地址
    private String phonename;// 手机名称
    private int type;// 请求类型
    private List<String> pathList;// 预备传输的文件名集合
    private String attach;// 附加字段(用于鉴别)

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
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

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
