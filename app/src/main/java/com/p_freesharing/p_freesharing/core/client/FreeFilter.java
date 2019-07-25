package com.p_freesharing.p_freesharing.core.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeImage;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeVideo;
import com.p_freesharing.p_freesharing.bean.Freesharing_Image;
import com.p_freesharing.p_freesharing.bean.Freesharing_Video;
import com.p_freesharing.p_freesharing.utils.FormatTools;
import com.p_freesharing.p_freesharing.utils.Logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/3/12 0012.
 */

public class FreeFilter {

    /**
     * 获取指定数量的freeImage(批量)
     *
     * @param context
     * @param freesharingImageOrises 图片集源
     * @param begin     开始索引
     * @param limit     获取数量
     * @return 处理后的缩略图集合
     */
    public static List<Freesharing_FreeImage> getImageFilter(Context context, List<Freesharing_Image> freesharingImageOrises, int begin, int limit) {
        List<Freesharing_FreeImage> freesharingFreeImages = new ArrayList<>();
        for (int i = begin; i < limit; i++) {
            Freesharing_FreeImage freesharingFreeImage = new Freesharing_FreeImage();
            String imagePath = freesharingImageOrises.get(i).getPath();
            Bitmap bitmap = FormatTools.getInstance().file2ThumboBitmap(context, imagePath, -1);
            freesharingFreeImage.setBitmap(bitmap);
            freesharingFreeImage.setPath(imagePath);
            freesharingFreeImages.add(freesharingFreeImage);
        }
        return freesharingFreeImages;
    }

    /**
     * 获取freeIamge(单个)
     *
     * @param context
     * @param freesharingImage   图片源
     * @return 处理后的缩略图集合
     */
    public static Freesharing_FreeImage getImageFilterSingle(Context context, Freesharing_Image freesharingImage) {
        Freesharing_FreeImage freesharingFreeImage = null;
        try {
            freesharingFreeImage = new Freesharing_FreeImage();
            String imagePath = freesharingImage.getPath();
            Bitmap bitmap = FormatTools.getInstance().file2ThumboBitmap(context, imagePath, -1);
            freesharingFreeImage.setBitmap(bitmap);
            freesharingFreeImage.setPath(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freesharingFreeImage;
    }

    /**
     * 获取指定数量的freeVideo(全部)
     *
     * @param context
     * @param freesharingVideo   视频源
     * @return 处理后的视频
     */
    public static Freesharing_FreeVideo getVideoFilterSingle(Context context, Freesharing_Video freesharingVideo) {
        Freesharing_FreeVideo freesharingFreeVideo = null;
        try {
            freesharingFreeVideo = new Freesharing_FreeVideo();
            String path = freesharingVideo.getPath();// 获取路径
            String duration = transferLong2Duration(freesharingVideo.getDuration());// 转换时长
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            File file = new File(path);
            if (file.exists()) {
                // 设置数据源为该文件对象指定的绝对路径
                Logs.t("ma_video").ii(file.getAbsolutePath());
                mmr.setDataSource(file.getAbsolutePath());
                //获得视频第一帧的Bitmap对象
                Bitmap bitmap = FormatTools.getInstance().bitmap2ThumboBitmap(context, mmr.getFrameAtTime(), -1);
                freesharingFreeVideo.setPath(path);
                freesharingFreeVideo.setFrame(bitmap != null ? bitmap : getDefaultFrame(context));
                freesharingFreeVideo.setDuration(duration);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return freesharingFreeVideo;
    }

    /**
     * 获取指定类型freeVideo(单个)
     *
     * @param context
     * @param freesharingVideoOrises 视频源
     * @param begin     开始索引
     * @param limit     获取数量
     * @return 处理后的视频集合
     */
    public static List<Freesharing_FreeVideo> getVideoFilter(Context context, List<Freesharing_Video> freesharingVideoOrises, int begin, int limit) {
        List<Freesharing_FreeVideo> freesharingFreeVideos = new ArrayList<>();
        for (int i = begin; i < limit; i++) {
            Freesharing_Video freesharingVideo_ori = freesharingVideoOrises.get(i);
            String path = freesharingVideo_ori.getPath();// 获取路径
            String duration = transferLong2Duration(freesharingVideo_ori.getDuration());// 转换时长
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            File file = new File(path);
            if (file.exists()) {
                // 设置数据源为该文件对象指定的绝对路径
                mmr.setDataSource(file.getAbsolutePath());
                //获得视频第一帧的Bitmap对象
                Bitmap bitmap = FormatTools.getInstance().bitmap2ThumboBitmap(context, mmr.getFrameAtTime(), -1);
                Freesharing_FreeVideo freesharingFreeVideo = new Freesharing_FreeVideo();
                freesharingFreeVideo.setPath(path);
                freesharingFreeVideo.setFrame(bitmap != null ? bitmap : getDefaultFrame(context));
                freesharingFreeVideo.setDuration(duration);
                freesharingFreeVideos.add(freesharingFreeVideo);
            }
        }
        return freesharingFreeVideos;
    }
    
    /* -------------------------------------------- method -------------------------------------------- */

    /**
     * 把毫秒转换成xx:yy形式表达
     *
     * @param duration
     * @return
     */
    public static String transferLong2Duration(long duration) {
        long sec = duration / 1000;// 总毫秒转总秒
        long min = sec / 60;// 分
        long remain_sec = sec % 60;// 剩余的秒
        String min_s = min < 10 ? "0" + min : min + "";
        String sec_s = remain_sec < 10 ? "0" + remain_sec : remain_sec + "";
        return min_s + ":" + sec_s;
    }

    /**
     * 获取一张默认的图片
     *
     * @param context
     * @return
     */
    private static Bitmap getDefaultFrame(Context context) {
        return FormatTools.getInstance().drawable2Bitmap(context.getResources().getDrawable(R.drawable.freesharing_test_feedback));
    }
}
