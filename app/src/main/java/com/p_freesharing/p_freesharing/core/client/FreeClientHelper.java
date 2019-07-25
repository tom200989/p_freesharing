package com.p_freesharing.p_freesharing.core.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.text.format.Formatter;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeTranslist;
import com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler;
import com.p_freesharing.p_freesharing.utils.FormatTools;
import com.p_freesharing.p_freesharing.utils.Logs;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by qianli.ma on 2018/5/7 0007.
 */

public class FreeClientHelper {

    private static String TAG = "FreeClientHelper";

    /**
     * 获取size表达形式
     *
     * @param current 当前已经传输
     * @param total
     * @return
     */
    public static String getSize(Context context, long current, long total) {
        String currents = Formatter.formatFileSize(context, current);
        String totals = Formatter.formatFileSize(context, total);
        return currents + "/" + totals;
    }

    /**
     * @param file
     * @return
     */
    public static Bitmap getThumbBitmap(Context context,File file) {
        if (isPicType(file.getAbsolutePath())) {// 图片
            String path = file.getAbsolutePath();
            return FormatTools.getInstance().file2ThumboBitmap(context, path, -1);
        } else {// 视频
            return FormatTools.getInstance().InputStream2Bitmap(getFirstFrameFromVideo(context, file.getAbsolutePath()));
        }

    }

    /**
     * 获取视频第一帧
     *
     * @param filePath
     * @return
     */
    private static InputStream getFirstFrameFromVideo(Context context, String filePath) {
        Drawable defaultDraw = context.getResources().getDrawable(R.drawable.freesharing_item_video);
        InputStream input = FormatTools.getInstance().Drawable2InputStream(defaultDraw);
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(filePath);
            Bitmap bitmap = FormatTools.getInstance().bitmap2ThumboBitmap(context, mmr.getFrameAtTime(), 4);
            input = FormatTools.getInstance().Bitmap2InputStream(bitmap);
        } catch (Exception e) {
            Logs.t(TAG).ee("save thumbnail videoframe error\n" + e.getMessage());
            e.printStackTrace();
        }
        return input;
    }

    /**
     * 是否为图片
     *
     * @param filepath
     * @return
     */
    private static boolean isPicType(String filepath) {
        boolean ispic = false;
        for (String exe : IBaseHandler.TRANSLIST_FILE_PIC) {
            if (filepath.endsWith(exe)) {
                ispic = true;
                break;
            }
        }
        return ispic;
    }

    /**
     * 传输列表集合是否存在指定对象
     *
     * @param fileName 文件名
     * @param fts      传输列表集合
     * @return
     */
    public static boolean isExitFreeTranslist(String fileName,String ip, List<Freesharing_FreeTranslist> fts) {
        for (Freesharing_FreeTranslist ta : fts) {
            String currentName = ta.getFilename();
            String currentIp = ta.getIp();
            boolean isNameSame = currentName.contains(fileName) | currentName.equalsIgnoreCase(fileName);
            boolean isIpSame = currentIp.contains(ip) | currentIp.equalsIgnoreCase(ip);
            if (isNameSame & isIpSame) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取存在的传输对象
     *
     * @param fileName
     * @param fts
     * @return
     */
    public static Freesharing_FreeTranslist getExistTrans(String fileName, String ip, List<Freesharing_FreeTranslist> fts) {
        for (Freesharing_FreeTranslist ta : fts) {
            String currentName = ta.getFilename();
            String currentIp = ta.getIp();
            boolean isNameSame = currentName.contains(fileName) | currentName.equalsIgnoreCase(fileName);
            boolean isIpSame = currentIp.contains(ip) | currentIp.equalsIgnoreCase(ip);
            if (isNameSame & isIpSame) {
                return ta;
            }
        }
        return null;
    }

    /**
     * 获取进度百分比
     *
     * @param current
     * @param total
     * @return
     */
    public static int getPercent(long current, long total) {
        return (int) ((current * 1d / total) * 100);
    }

}
