package com.p_freesharing.p_freesharing.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by qianli.ma on 2017/7/10.
 */

public class OtherUtils {

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param sClass   服务的类名
     * @return true:代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, Class sClass) {
        String serviceName = sClass.getName();// 获取服务的全类名
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);// 获取系统服务对象
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(1000);// 获取正在运行中的服务集合 -->1000为可能获取到的个数
        if (myList.size() <= 0) {// 判断集合是否有对象
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {// 遍历每一个服务对象
            String mName = myList.get(i).service.getClassName().toString();// 获取服务的类名
            if (mName.equalsIgnoreCase(serviceName) || mName.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断recycle
     *
     * @param recyclerView
     * @return true:到底了
     */
    public static boolean isVisBottom(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个完全可见子项的 position (注意:是完全可见)
        int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前 RecyclerView 的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView 的滑动状态
        int state = recyclerView.getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == RecyclerView.SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取一个随机会话id
     *
     * @return
     */
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取元数据
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    /**
     * 获取后缀
     *
     * @param file
     * @return
     */
    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }
    /**
     * 获取rcv满屏展示item个数
     *
     * @param context
     * @param rcvParent
     * @return
     */
    public static int getRcvMaxCount(Context context, RelativeLayout rcvParent) {
        ScreenSize.SizeBean size = ScreenSize.getSize(context);
        int itemHeight = (int) (size.width * 0.25f);// 单个item的高度(注意:要与配置文件填入的百分比相等)
        int rcvHeight = rcvParent.getMeasuredHeight();
        int rcvqHeight = rcvParent.getHeight();
        int line = rcvHeight / itemHeight;
        return rcvHeight % itemHeight > 0 ? (line + 1) * 4 : line * 4;
    }

    /**
     * 获取当前时间20180618181818
     *
     * @return
     */
    public static String getCurrentDateForString() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 获取文件后缀名 (.png .mp4 ...)
     *
     * @param filename
     */
    public static String getExe(String filename) {
        return filename.substring(filename.lastIndexOf("."), filename.length());
    }
}
