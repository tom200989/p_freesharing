package com.p_freesharing.p_freesharing.core.client;

import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeTranslist;
import com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler;
import com.p_freesharing.p_freesharing.utils.Logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/4/27 0027.
 */

public class FreeCacheUtils {
    private static String TAG = "FreeCacheUtils";

    /**
     * json--> freeTranslist[]
     *
     * @return freeTranslist[]
     */
    public static List<Freesharing_FreeTranslist> readCacheProperty() {
        List<Freesharing_FreeTranslist> freesharingFreeTranslists = new ArrayList<>();
        // 1.获取缓存json文件
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()// /store/0/mnt
                                     + File.separator + IBaseHandler.FILE_CACHE_DIR + // smartcache
                                     File.separator + IBaseHandler.FILE_CACHE_HISTORY);// smarthistory.json
        // 2.读取json
        try {
            int len = 0;
            byte[] b = new byte[1024];
            StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            String cacheJson = sb.toString();
            // 3.把json转换为freeTranslist(此处注意: 如果读取到的json字符串为空, 则使用fastjson解析后的集合为null
            freesharingFreeTranslists = !TextUtils.isEmpty(cacheJson) ? JSON.parseArray(cacheJson, Freesharing_FreeTranslist.class) : freesharingFreeTranslists;
            fis.close();
            Logs.t(TAG).ii("read cache history success ");
        } catch (IOException e) {
            Logs.t(TAG).ee("read cache history error: " + e.getMessage());
            e.printStackTrace();
        }
        return freesharingFreeTranslists;
    }

    /**
     * freesharingFreeTranslists[]--> json
     *
     * @param freesharingFreeTranslists
     */
    public static void writeCacheProperty(List<Freesharing_FreeTranslist> freesharingFreeTranslists) {
        // 1.获取缓存json文件
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()// /store/0/mnt
                                     + File.separator + IBaseHandler.FILE_CACHE_DIR + // smartcache
                                     File.separator + IBaseHandler.FILE_CACHE_HISTORY);// smarthistory.json
        // 2.把freeTranslist转换成json
        if (freesharingFreeTranslists.size() > 0) {
            String cacheJson = JSON.toJSONString(freesharingFreeTranslists);
            // 3.写入
            try {
                byte[] bytes = cacheJson.getBytes(StandardCharsets.UTF_8);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
                Logs.t(TAG).ii("write cache history success ");
            } catch (Exception e) {
                Logs.t(TAG).ee("write cache history error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否存在同名文件
     *
     * @param filename
     * @return
     */
    public static boolean isExistSameNameFile(String filename) {
        // 1.读取当前json--> fts
        List<Freesharing_FreeTranslist> fts = readCacheProperty();
        Logs.t(TAG).ii("translist size: " + fts.size());
        if (fts != null && fts.size() > 0) {
            for (Freesharing_FreeTranslist ta : fts) {
                if (ta.getFilename().contains(filename) || ta.getFilename().equalsIgnoreCase(filename)) {
                    // 2.存在同名
                    return true;
                }
            }
        } else {
            return false;
        }

        return false;
    }

}
