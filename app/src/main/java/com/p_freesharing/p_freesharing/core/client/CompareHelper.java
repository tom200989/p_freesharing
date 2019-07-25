package com.p_freesharing.p_freesharing.core.client;

import com.p_freesharing.p_freesharing.bean.Freesharing_Audio;
import com.p_freesharing.p_freesharing.bean.Freesharing_Image;
import com.p_freesharing.p_freesharing.bean.Freesharing_Video;

import java.util.Comparator;

/**
 * Created by qianli.ma on 2018/5/15 0015.
 */

public class CompareHelper<T> implements Comparator<T> {


    @Override
    public int compare(T t1, T t2) {
        if (t1 instanceof Freesharing_Image) {
            return imageComapre(t1, t2);
        } else if (t1 instanceof Freesharing_Video) {
            return videoComapre(t1, t2);
        } else if (t1 instanceof Freesharing_Audio) {
            return audioComapre(t1, t2);
        } else {
            return 0;
        }
    }

    /**
     * 图片对象比较
     *
     * @param t1
     * @param t2
     * @return
     */
    private int imageComapre(T t1, T t2) {
        Freesharing_Image freesharingImage1 = (Freesharing_Image) t1;
        Freesharing_Image freesharingImage2 = (Freesharing_Image) t2;
        long date1 = freesharingImage1.getDate();
        long date2 = freesharingImage2.getDate();
        return compareDate(date1, date2);
    }

    /**
     * 视频对象比较
     *
     * @param t1
     * @param t2
     * @return
     */
    private int videoComapre(T t1, T t2) {
        Freesharing_Video freesharingVideo1 = (Freesharing_Video) t1;
        Freesharing_Video freesharingVideo2 = (Freesharing_Video) t2;
        long date1 = freesharingVideo1.getDate();
        long date2 = freesharingVideo2.getDate();
        return compareDate(date1, date2);
    }

    /**
     * 视频对象比较
     *
     * @param t1
     * @param t2
     * @return
     */
    private int audioComapre(T t1, T t2) {
        Freesharing_Audio freesharingAudio1 = (Freesharing_Audio) t1;
        Freesharing_Audio freesharingAudio2 = (Freesharing_Audio) t2;
        long date1 = freesharingAudio1.getDate();
        long date2 = freesharingAudio2.getDate();
        return compareDate(date1, date2);
    }


    /**
     * 比较两个日期
     *
     * @param date1
     * @param date2
     * @return
     */
    private int compareDate(long date1, long date2) {
        if (date1 == date2) {
            return 0;
        } else if (date1 > date2) {
            return -1;
        } else {
            return 1;
        }
    }
}
