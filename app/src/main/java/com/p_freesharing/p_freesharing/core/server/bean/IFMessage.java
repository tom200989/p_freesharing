package com.p_freesharing.p_freesharing.core.server.bean;

/**
 * Created by wzhiqiang on 2018/4/2.
 */

public class IFMessage {

    private String fileName;       // 文件名  
    private int updateStatus;   // 状态   
    private int updateProgress;    // 进度   

    /**
     * @param fileName       文件名
     * @param updateStatus   状态
     * @param updateProgress 进度
     */
    public IFMessage(String fileName, int updateStatus, int updateProgress) {
        this.fileName = fileName;
        this.updateStatus = updateStatus;
        this.updateProgress = updateProgress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    public int getUpdateProgress() {
        return updateProgress;
    }

    public void setUpdateProgress(int updateProgress) {
        this.updateProgress = updateProgress;
    }
}
