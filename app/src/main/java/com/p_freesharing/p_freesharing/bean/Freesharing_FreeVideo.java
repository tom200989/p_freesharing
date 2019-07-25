package com.p_freesharing.p_freesharing.bean;

import android.graphics.Bitmap;

/**
 * Created by qianli.ma on 2018/3/9 0009.
 */

public class Freesharing_FreeVideo {
    public String path;// 视频路径
    public String duration;// 播放时长
    public Bitmap frame;// 第一帧
    public boolean isSelected;// 是否选中

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getFrame() {
        return frame;
    }

    public void setFrame(Bitmap frame) {
        this.frame = frame;
    }
}
