package com.p_freesharing.p_freesharing.bean;

import android.graphics.Bitmap;

/**
 * Created by qianli.ma on 2018/3/9 0009.
 */

public class Freesharing_FreeImage {
    public String path;
    public Bitmap bitmap;
    public boolean isSelected;// 是否被选择

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
