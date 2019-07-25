package com.p_freesharing.p_freesharing.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.view.View;

import com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by yaodong.zhang on 2017/6/22.
 */

public class FileUtils {

    public static String TAG = "FileUtils";

    public static String createFilePath(String saveUrl) {
        return getSDPath() + saveUrl;
    }

    /**
     * 把Long转换成B KB MB GB格式
     *
     * @param bytes long总字节
     * @return
     */
    public static String formatLongToStr(long bytes) {
        DecimalFormat df = new DecimalFormat("#.00");
        String sizeString = "";
        if (bytes < 1024) {
            sizeString = df.format((double) bytes) + "B";
        } else if (bytes < 1048576) {
            sizeString = df.format((double) bytes / 1024) + "KB";
        } else if (bytes < 1073741824) {
            sizeString = df.format((double) bytes / 1048576) + "MB";
        } else {
            sizeString = df.format((double) bytes / 1073741824) + "GB";
        }
        return sizeString;
    }

    /**
     * 创建free sharing 保存文件夹
     */
    public static File createFreeSharingSaveDir() {
        String save = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + IBaseHandler.FILE_SAVE_DIR;
        File file = new File(save);
        if (!file.exists() | !file.isDirectory()) {
            boolean mkdir = file.mkdir();
            if (mkdir) {
                Logs.t(TAG).ii("create free sharing save dir success");
            } else {
                Logs.t(TAG).ee("create free sharing save dir failed");
            }
        }
        return file;
    }

    /**
     * 创建free sharing 缓存文件夹
     */
    public static File createFreeSharingCacheDir() {
        String cache = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + IBaseHandler.FILE_CACHE_DIR;
        File file = new File(cache);
        if (!file.exists() | !file.isDirectory()) {
            boolean mkdir = file.mkdir();
            if (mkdir) {
                Logs.t(TAG).ii("create free sharing cache dir success");
            } else {
                Logs.t(TAG).ee("create free sharing cache dir failed");
            }
        }
        return file;
    }

    /**
     * 创建free sharing 缓存配置文件
     */
    public static File createFreeSharingCacheHistory() {
        // 0.检测并创建缓存文件夹
        File cacheDir = createFreeSharingCacheDir();
        // 1.创建历史文件
        String cacheProperty = cacheDir + File.separator + IBaseHandler.FILE_CACHE_HISTORY;
        File file = new File(cacheProperty);
        if (!file.exists() | file.isDirectory()) {
            try {
                boolean isCreate = file.createNewFile();
                Logs.t(TAG).ii("create the cache property: " + isCreate);
            } catch (IOException e) {
                Logs.t(TAG).ee("create the cache property error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return file;
    }


    public static String getSDPath() {
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            return Environment.getExternalStorageDirectory().toString();
        } else
            return Environment.getDownloadCacheDirectory().toString();
    }

    /**
     * InputStrem 转byte[]
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] readStreamToBytes(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 8];
        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        out.flush();
        byte[] result = out.toByteArray();
        in.close();
        out.close();
        return result;
    }

    /**
     * 写入文件
     *
     * @param in
     * @param file
     */
    public static void writeFile(InputStream in, File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (file != null && file.exists())
            file.delete();

        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024 * 128];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();

    }

    /**
     * 得到Bitmap的byte
     *
     * @return
     * @author YOLANDA
     */
    public static byte[] bmpToByteArray(Bitmap bmp) {
        if (bmp == null)
            return null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /*
    * 根据view来生成bitmap图片，可用于截图功能
    */
    public static Bitmap getViewBitmap(View v) {

        v.clearFocus(); //

        v.setPressed(false); //
        // 能画缓存就返回false

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }

        v.buildDrawingCache();

        Bitmap cacheBitmap = v.getDrawingCache();

        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view

        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;

    }
}
