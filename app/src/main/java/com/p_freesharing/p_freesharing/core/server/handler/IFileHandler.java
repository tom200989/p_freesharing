package com.p_freesharing.p_freesharing.core.server.handler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.text.format.Formatter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeTranslist;
import com.p_freesharing.p_freesharing.core.client.FreeCacheUtils;
import com.p_freesharing.p_freesharing.core.server.bean.IFRequest;
import com.p_freesharing.p_freesharing.core.server.bean.IFResponce;
import com.p_freesharing.p_freesharing.core.server.core.NetUtils;
import com.p_freesharing.p_freesharing.utils.FileUtils;
import com.p_freesharing.p_freesharing.utils.FormatTools;
import com.p_freesharing.p_freesharing.utils.Logs;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.upload.HttpFileUpload;
import com.yanzhenjie.andserver.upload.HttpUploadContext;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import upload.corehttp.org.apache.http.Header;
import upload.corehttp.org.apache.http.HttpEntityEnclosingRequest;
import upload.corehttp.org.apache.http.HttpRequest;
import upload.corehttp.org.apache.http.HttpResponse;
import upload.corehttp.org.apache.http.entity.StringEntity;
import upload.corehttp.org.apache.http.protocol.HttpContext;


/**
 * Created by wzhiqiang on 2018/3/30.
 */

public class IFileHandler extends IBaseHandler implements RequestHandler {

    public final String id = IBaseHandler.id;// 会话ID
    public final String ip = IBaseHandler.ip;// 请求IP
    public final String phonename = IBaseHandler.phonename;// 请求手机名称
    public final String requesttype = IBaseHandler.requesttype;// 请求类型
    public final String path = IBaseHandler.path;// 请求文件路径
    public final String size = IBaseHandler.size;// 请求文件大小
    public final String filetype = IBaseHandler.filetype;// 请求文件类型
    public final String filename = IBaseHandler.filename;// 请求文件名
    public final String system = IBaseHandler.system;// 系统
    public final String mimetype = IBaseHandler.mimetype;// 元类型

    private static final String TAG = "ifilehandler";
    private File sdcard = Environment.getExternalStorageDirectory();
    private File saveDir = new File(sdcard, FILE_SAVE_DIR);// 保存文件的文件夹
    private File cacheDir = new File(sdcard, FILE_CACHE_DIR);// 缓存文件的文件夹
    private File cacheHistory = new File(cacheDir, FILE_CACHE_HISTORY);// 历史文件
    public static HashSet<String> uploadingFileSet = new HashSet<>();// 正在上传的文件集合
    private Context context;

    public IFileHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) {
        toHandler(request, response, headToParams(request));
    }


    /* 开始处理 */
    private void toHandler(HttpRequest request, HttpResponse response, Map<String, String> headers) {
        // 创建保存的目录 & 创建缓存文件夹 & 创建历史记录文件 
        if (!saveDir.exists() | !saveDir.isDirectory()) {
            boolean mkdir = saveDir.mkdir();
            Logs.t(TAG).ii("create save dir : " + mkdir);
        }

        if (!cacheDir.exists() | !cacheDir.isDirectory()) {
            boolean mkdir = cacheDir.mkdir();
            Logs.t(TAG).ii("create cache dir : " + mkdir);
        }

        if (!cacheHistory.exists() | cacheHistory.isDirectory()) {
            try {
                boolean mkdir = cacheHistory.createNewFile();
                Logs.t(TAG).ii("create cache history : " + mkdir);
            } catch (IOException e) {
                Logs.t(TAG).ii("create cache history error: " + e.getMessage());
                e.printStackTrace();
            }

        }

        try {
            // 0.获取context
            context = context;
            // 1.获取头集合
            // 2.获取imRequest
            IFRequest ifRequest = getIFRequest(headers);
            Logs.t(TAG).ii(ifRequest.toString());
            // 3.判断
            if (!HttpRequestParser.isMultipartContentRequest(request)) {// 不支持多文件
                response.setEntity(new StringEntity(getNoMultipartResponce(ifRequest)));
            } else {/* 支持多文件 */
                //  开始读写文件
                if (ifRequest.getSystem() == IBaseHandler.ANDROID) {// Android的处理
                    wirteFileByAndroid(getHttpUploadContext(request), ifRequest, request, response);
                } else {// IOS的处理
                    wirteFileByIOS(getHttpUploadContext(request), ifRequest, request, response);
                }

                response.setEntity(new StringEntity(getSupportResponce(ifRequest)));
            }
            Logs.t(TAG).ii("end to write file to local");
        } catch (Exception e) {
            try {
                // 出错时反馈
                Logs.t(TAG).ee("error: " + e.getMessage());
                response.setEntity(new StringEntity(getErrorResponce(e)));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            Logs.t(TAG).ee("error: " + e.getMessage());
        }
    }


    /**
     * 获取imrequest
     *
     * @return
     */
    private IFRequest getIFRequest(Map<String, String> maps) {
        IFRequest iFRequest = new IFRequest();
        for (String key : maps.keySet()) {
            if (key.equalsIgnoreCase(id)) {// id
                iFRequest.setId(maps.get(key));
            } else if (key.equalsIgnoreCase(ip)) {// ip
                iFRequest.setIp(maps.get(key));
            } else if (key.equalsIgnoreCase(phonename)) {// phone name 
                iFRequest.setPhonename(maps.get(key));
            } else if (key.equalsIgnoreCase(requesttype)) {// request type 
                iFRequest.setRequestType(Integer.valueOf(maps.get(key)));
            } else if (key.equalsIgnoreCase(path)) {// path
                iFRequest.setPath(maps.get(key));
            } else if (key.equalsIgnoreCase(size)) {// size
                iFRequest.setSize(Long.valueOf(maps.get(key)));
            } else if (key.equalsIgnoreCase(filetype)) {// file type 
                iFRequest.setFileType(Integer.valueOf(maps.get(key)));
            } else if (key.equalsIgnoreCase(filename)) {// file name 
                iFRequest.setFileName(maps.get(key));
            } else if (key.equalsIgnoreCase(system)) {// system
                iFRequest.setSystem(Integer.valueOf(maps.get(key)));
            } else if (key.equalsIgnoreCase(mimetype)) {// mimetype
                iFRequest.setMimetype(maps.get(key));
            }
        }
        return iFRequest;
    }

    /**
     * 保存文件到本地(安卓手机)
     *
     * @param context
     * @param ifRequest
     * @param response
     */
    private void wirteFileByAndroid(HttpUploadContext context, IFRequest ifRequest, HttpRequest request, HttpResponse response) {

        /* 读流思路:
         *  由于在文件传递过来后, apache的 httpcore.jar 以及 fileupload.jar 对文件内容进行了处理(如下)
         *
         *  {
         *    headinfo start ...
         *       文件的有效内容
         *    headinfo end ...
         *  }
         *
         *  导致了文件即使写入之后, 会因为多了这些头部和尾部的内容而导致显示失败
         *  因此需要对这部分头和尾进行剔除才能得到真正的文件内容流
         *
         *  从5.1开始, 为剔除操作
         *
         *  流段操作示例:
         *
         *       |--------------|-----------------------------------|----------|     传递过来的文件长度
         *       ↑     head     ↑           real file               ↑    end   ↑     头 + 实际有效的文件内容 + 尾
         *       P0             F1                                  F2         P1
         *
         * */

        // 0.获取ip
        String ip = ifRequest.getIp();
        // 1.获取文件名
        String fileName = ifRequest.getFileName();
        // 2.获取带IP的文件名(用于临时存储)
        String ip_filename = ip + FILE_SPLIT + fileName;// 拼接: 192.168.1.103#smartlink#aaa.png
        // 3.获取文件内容实际大小
        long fileSize = ifRequest.getSize();
        long contentLength = context.getContentLength();
        // 3.1.开始读取写入
        File uploadedFile = new File(saveDir, fileName);
        if (!uploadedFile.exists() | uploadedFile.isDirectory()) {
            try {
                boolean newFile = uploadedFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // 4. 限定最大可输送数为18个文件
            if (uploadingFileSet.size() > MAX_FILE_COUNT) { // 请求线程超出
                response.setEntity(new StringEntity(getIFileCountOverResponce(ifRequest)));
                return;
            } else if (uploadingFileSet.contains(ip_filename)) {// 当前文件正在上传
                response.setEntity(new StringEntity(getIFileUploadingResponce(ifRequest)));
                return;
            } else {
                uploadingFileSet.add(ip_filename);
                Logs.t("tongyi").ii("ifhandler add ip_filename: " + ip_filename);
            }

            // 5.设置输入输出流软引用
            InputStream input = context.getInputStream();
            BufferedInputStream bi = new BufferedInputStream(input);
            BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(uploadedFile));

            long startTime = System.currentTimeMillis();
            byte[] b = new byte[1024 * 1024];
            int length;
            long current = 0l;
            long last = 0l;
            boolean isFirst = true;
            // 跳出条件: 
            // 1--> 文件上传完毕;
            // 2--> 服务器端的用户主动取消了上传(通过操作文件名集合的元素进行控制)
            long end = 0;

            /* -------------------------------------------- 读写流 start-------------------------------------------- */
            /* -------------------------------------------- 读写流 start-------------------------------------------- */
            /* -------------------------------------------- 读写流 start-------------------------------------------- */
            while ((length = bi.read(b)) != -1 && uploadingFileSet.contains(ip_filename)) {

                // 5.1.初始化流的起始位以及末位
                long start = 0;


                // 5.2.若首次读取1MB时
                if (isFirst) {

                    // 5.2.1.起始位标
                    start = getStartIndex(b, length);
                    Logs.t(TAG).ii("TA start index: " + start);
                    // 5.2.2.末标 = 起始位标 + 文件实际有效内容的长度
                    end = fileSize + start;
                    Logs.t(TAG).ii("TA end index: " + fileSize);
                    // 5.2.3.停止
                    isFirst = false;

                }

                // 5.3.累积已经读取的长度
                current += length;

                // 5.4.若当前长度 >= 末标 --> 获取本次读取中有效的长度为实际长度
                // 5.4.若当前长度 <  末标 --> 继续沿用当前长度为实际长度
                int reallLength = current >= end ? (int) (length - (current - end)) : length;

                /* 5.5.显示进度 */
                Logs.t(TAG).ii("each size: " + reallLength + " ;current size: " + current);
                if (current - last >= 1024l * 1024l) {
                    Logs.t(TAG).ii("filename: " + fileName + "\ncurrent progress: " + current);
                    last = current;
                    EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_UPLOADING, current));
                }

                // 5.6.末流处理
                // (1) length > 0 : 用于处理读取最后一截流时, 装载byte[]的大小已经小于1MB, 此时 length 必定小于 current - end 的长度
                if (reallLength > 0) {
                    bo.write(b, (int) start, (int) (reallLength - start));
                    bo.flush();
                }

            }

            //打印总共传输了多长时间
            long endTime = System.currentTimeMillis();
            Logs.t(TAG).ii("total time: " + Math.abs(endTime - startTime));

            // 5.7.清除imhander里的currentMap的IP存值状态--> 以避免下次传输时客户端状态混乱
            IMHandler.currentIps.remove(ip);

            /* -------------------------------------------- 读写流 end-------------------------------------------- */
            /* -------------------------------------------- 读写流 end-------------------------------------------- */
            /* -------------------------------------------- 读写流 end-------------------------------------------- */


            // 6.文件正常上传完成
            if (uploadingFileSet.contains(ip_filename)) {
                if (!uploadedFile.exists()) {// 如果文件不存在, 这不做缓存
                    Logs.t(TAG).ee("uploadfile write state: not exist, write failed ");
                    return;
                } else {
                    // 6.1.获取缩略图并保存到缓存文件夹
                    File cacheThumbPic = createCacheThumbPic(uploadedFile.getAbsolutePath());
                    // 6.2.保存上传完成的信息到缓存历史文件夹
                    saveCacheHistory(ifRequest, uploadedFile, cacheThumbPic.getAbsolutePath());
                }

                // 6.3.删除内存里的文件名
                uploadingFileSet.remove(ip_filename);
                EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_SUCCESS, current));

                // 6.文件是中途被中断
            } else {

                // 6.1.删除已经上传的文件
                deleteFile(fileName);
                // 6.2.发送给UI更新
                EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_FAILED, current));
            }

            // bis.close();// 不需要关输入流
            BufferedOutputStream bos = bo;
            if (bos != null) {
                bos.close();
            }

        } catch (Exception e) {
            Logs.t(TAG).ee("uploadfile write state: show error \n" + e.getMessage());
            // 1.删除已经上传的文件
            deleteFile(fileName);
            // 2.更新UI标记为上传取消
            EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_FAILED, 0));
            e.printStackTrace();
        } finally {
            // 垃圾回收
            System.gc();
        }
    }

    /**
     * 保存文件到本地(IOS手机)
     *
     * @param context
     * @param ifRequest
     * @param response
     */
    private void wirteFileByIOS(HttpUploadContext context, IFRequest ifRequest, HttpRequest request, HttpResponse response) {

        // 0.获取ip
        String ip = ifRequest.getIp();
        // 1.获取文件名
        String fileName = ifRequest.getFileName();
        // 2.获取带IP的文件名
        String ip_filename = ip + FILE_SPLIT + fileName;// 拼接: 192.168.1.103#smartlink#aaa.png
        // 3.获取文件大小
        long fileSize = ifRequest.getSize();
        // 3.1.开始读取写入
        File uploadedFile = new File(saveDir, fileName);
        if (!uploadedFile.exists() | uploadedFile.isDirectory()) {
            try {
                boolean newFile = uploadedFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // 4. 限定最大可输送数为18个文件
            if (uploadingFileSet.size() > MAX_FILE_COUNT) { // 请求线程超出
                response.setEntity(new StringEntity(getIFileCountOverResponce(ifRequest)));
                return;
            } else if (uploadingFileSet.contains(ip_filename)) {// 当前文件正在上传
                response.setEntity(new StringEntity(getIFileUploadingResponce(ifRequest)));
                return;
            } else {
                uploadingFileSet.add(ip_filename);
            }

            // 5.设置输入输出流软引用
            InputStream input = context.getInputStream();
            BufferedInputStream bi = new BufferedInputStream(input);
            BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(uploadedFile));

            long startTime = System.currentTimeMillis();
            byte[] b = new byte[1024 * 1024];
            int length;
            long current = 0l;
            long last = 0l;
            // 跳出条件: 
            // 1--> 文件上传完毕;
            // 2--> 服务器端的用户主动取消了上传(通过操作文件名集合的元素进行控制)
            while ((length = bi.read(b)) != -1 && uploadingFileSet.contains(ip_filename)) {
                current += length;
                Logs.t(TAG).ii("total size: " + context.getContentLength() + " ;each size: " + length + " ;current size: " + current);
                if (current - last >= 1024l * 1024l) {
                    Logs.t(TAG).ii("filename: " + fileName + "\ncurrent progress: " + current);
                    last = current;
                    EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_UPLOADING, current));
                }
                bo.write(b, 0, length);
            }
            bo.flush();
            //打印总共传输了多长时间
            long endTime = System.currentTimeMillis();
            Logs.t(TAG).ii("total time: " + Math.abs(endTime - startTime));

            // 6.文件正常上传完成
            if (uploadingFileSet.contains(ip_filename)) {
                if (!uploadedFile.exists()) {
                    Logs.t(TAG).ee("uploadfile write state: not exist, write failed ");
                    return;
                } else {
                    // 6.1.获取缩略图并保存到缓存文件夹
                    File cacheThumbPic = createCacheThumbPic(uploadedFile.getAbsolutePath());
                    // 6.2.保存上传完成的信息到缓存历史文件夹
                    saveCacheHistory(ifRequest, uploadedFile, cacheThumbPic.getAbsolutePath());
                }

                // 6.3.删除内存里的文件名
                uploadingFileSet.remove(ip_filename);
                EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_SUCCESS, current));

                // 6.文件是中途被中断
            } else {

                // 6.1.删除已经上传的文件
                deleteFile(fileName);
                // 6.2.发送给UI更新
                EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_FAILED, current));
            }

            // bis.close();// 不需要关输入流
            BufferedOutputStream bos = bo;
            if (bos != null) {
                bos.close();
            }

        } catch (Exception e) {
            Logs.t(TAG).ee("uploadfile write state: show error \n" + e.getMessage());
            // 1.删除已经上传的文件
            deleteFile(fileName);
            // 2.更新UI标记为上传取消
            EventBus.getDefault().postSticky(toTranslist(ifRequest, TRANSLIST_STATE_FAILED, 0));
            e.printStackTrace();
        } finally {
            // 垃圾回收
            System.gc();
        }
    }
    /* -------------------------------------------- method -------------------------------------------- */

    /**
     * 反馈: 不支持多文件
     *
     * @param ifRequest
     */
    private String getNoMultipartResponce(IFRequest ifRequest) {
        IFResponce response = new IFResponce();
        response.setId(ifRequest.getId());
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_UPLOAD_ERROR);
        response.setPhonename(ifRequest.getPhonename());
        response.setPath(ifRequest.getPath());
        response.setSize(ifRequest.getSize());
        response.setReback(STATE_404);
        response.setRebackcontent(context.getString(R.string.freesharing_equipment_does_not_support_multi_file_transmission));
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 出错信息
     *
     * @param e
     * @return
     */
    private String getErrorResponce(Exception e) {
        IFResponce response = new IFResponce();
        response.setId(SERVER_ERROR_ID);
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_ERROR);
        response.setPhonename(Build.BRAND);
        response.setPath("");
        response.setSize(0);
        response.setReback(STATE_503);
        response.setRebackcontent(e.getMessage());
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 支持文件上传
     *
     * @param ifRequest
     * @return
     */
    private String getSupportResponce(IFRequest ifRequest) {
        IFResponce response = new IFResponce();
        response.setId(ifRequest.getId());
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_UPLOAD_PERMIT);
        response.setPhonename(ifRequest.getPhonename());
        response.setPath(ifRequest.getPath());
        response.setSize(ifRequest.getSize());
        response.setReback(STATE_200);
        response.setRebackcontent(context.getString(R.string.freesharing_start_processing_uploaded_files));
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 上传线程数超过限制
     *
     * @param ifRequest
     * @return
     */
    private String getIFileCountOverResponce(IFRequest ifRequest) {
        IFResponce response = new IFResponce();
        response.setId(ifRequest.getId());
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_UPLOAD_THREAD_OVER_COUNT);
        response.setPhonename(Build.BRAND);
        response.setPath(ifRequest.getPath());
        response.setSize(ifRequest.getSize());
        response.setReback(STATE_200);
        response.setRebackcontent(context.getString(R.string.freesharing_the_number_of_lines_uploaded_to_the_limit));
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 当前文件正在上传
     *
     * @param ifRequest
     * @return
     */
    private String getIFileUploadingResponce(IFRequest ifRequest) {
        IFResponce response = new IFResponce();
        response.setId(ifRequest.getId());
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_UPLOADING);
        response.setPhonename(Build.BRAND);
        response.setPath(ifRequest.getPath());
        response.setSize(ifRequest.getSize());
        response.setReback(STATE_200);
        response.setRebackcontent(context.getString(R.string.freesharing_the_current_file_is_uploading));
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 服务端取消上传
     *
     * @param ifRequest
     * @return
     */
    public String getIFileUploadCancelResponce(IFRequest ifRequest) {
        IFResponce response = new IFResponce();
        response.setId(ifRequest.getId());
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_UPLOAD_SERVER_CANCEL);
        response.setPhonename(Build.BRAND);
        response.setPath(ifRequest.getPath());
        response.setSize(ifRequest.getSize());
        response.setReback(STATE_200);
        response.setRebackcontent(context.getString(R.string.freesharing_the_other_side_terminates_the_upload));
        return JSONObject.toJSONString(response);
    }

    /**
     * 获取httpUploadContext
     */
    public HttpUploadContext getHttpUploadContext(HttpRequest request) {
        FileItemFactory factory = new DiskFileItemFactory(1024 * 1024, saveDir);
        HttpFileUpload fileUpload = new HttpFileUpload(factory);
        return new HttpUploadContext((HttpEntityEnclosingRequest) request);
    }

    /**
     * 删除文件
     */
    public void deleteFile(String fileName) {
        File uploadedFile = new File(saveDir, fileName);
        if (uploadedFile.exists()) {
            uploadedFile.delete();
        }
    }


    /**
     * 保存已上传的文件信息到缓存历史文件「smarthistory.json」
     *
     * @param ifRequest 请求信息
     * @param upload    保存的文件(用于获取路径和文件大小)
     * @param cachePath 缓存路径
     */
    private void saveCacheHistory(IFRequest ifRequest, File upload, String cachePath) {
        /* 加同步锁以保证文件读写步骤唯一 */
        synchronized (IFileHandler.class) {
            // 1.读取原有的json--> 转换为freetranslist[]
            List<Freesharing_FreeTranslist> freesharingFreeTranslists = FreeCacheUtils.readCacheProperty();
            // 3.封装
            Freesharing_FreeTranslist ta = new Freesharing_FreeTranslist();
            ta.setFileType(ifRequest.getFileType());
            ta.setFilename(ifRequest.getFileName());
            ta.setIp(ifRequest.getIp());
            ta.setPhone(ifRequest.getPhonename());
            ta.setPicBitmap(null);
            ta.setThumbUrl(cachePath);
            ta.setPicUrl(upload.getAbsolutePath());
            ta.setProgress(0);
            ta.setSend(false);
            ta.setSize(FileUtils.formatLongToStr(upload.length()));
            ta.setState(IBaseHandler.TRANSLIST_STATE_SUCCESS);
            // 4.加入集合
            freesharingFreeTranslists.add(ta);
            // 5.生成json
            String cacheJson = JSON.toJSONString(freesharingFreeTranslists);
            // 6.写入文件
            FreeCacheUtils.writeCacheProperty(freesharingFreeTranslists);
        }
    }

    /**
     * 生成并保存缩略图到缓存文件夹
     */
    private File createCacheThumbPic(String filepath) {
        Logs.t(TAG).ii("filepath: " + filepath);
        // P0.得到缩减文件名(aaa.png , bbb.mp4)
        String simpleName = new File(filepath).getName();
        /* 存入缓存文件夹的名称: thumb_aaa.png */
        // P1.是否为图片
        if (isPicType(filepath)) {
            // P2.获取默认的图片
            Drawable defaultDraw = context.getResources().getDrawable(R.drawable.freesharing_item_photo);
            InputStream input = FormatTools.getInstance().Drawable2InputStream(defaultDraw);
            // P3.是否为常用图片格式
            if (isPng(filepath)) {
                // P4.替换默认的图片
                try {
                    Bitmap bitmap = FormatTools.getInstance().file2ThumboBitmap(context, filepath, 4);
                    input = FormatTools.getInstance().Bitmap2InputStream(bitmap);
                } catch (Exception e) {
                    Logs.t(TAG).ee("save thumbnail pic error\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
            // P4.1.处理simplename--> png格式
            simpleName = getPngName(simpleName);
            // P5.写入缓存文件夹
            return writeToCacheDir(IBaseHandler.FILE_CACHE_PRE + simpleName, input);

        } else {// V1.是否为视频

            // V2.获取视频第一帧
            Drawable defaultDraw = context.getResources().getDrawable(R.drawable.freesharing_item_video);
            InputStream input = FormatTools.getInstance().Drawable2InputStream(defaultDraw);
            input = getFirstFrameFromVideo(filepath);
            // V2.1.处理simplename--> png格式
            simpleName = getPngName(simpleName);
            // V3.写入缓存文件夹
            return writeToCacheDir(IBaseHandler.FILE_CACHE_PRE + simpleName, input);
        }


    }

    /**
     * 写入缓存文件夹
     *
     * @param thumbname 缩略图名称
     * @param input     图元流
     */
    private File writeToCacheDir(String thumbname, InputStream input) {
        // 缓存文件全路径 /storage/0/mnt/smartcache/smartthumb_aaa.png
        String thumbPath = cacheDir.getAbsolutePath() + File.separator + thumbname;
        File file = new File(thumbPath);
        try {
            FileOutputStream fio = new FileOutputStream(file);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = input.read(b)) != -1) {
                fio.write(b, 0, len);
            }
            fio.flush();
            fio.close();
            input.close();
            Logs.t(TAG).ii("save thumbnail pic success: " + thumbname);
        } catch (Exception e) {
            Logs.t(TAG).ee("save thumbnail pic error\n" + e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 是否为图片
     *
     * @param filepath
     * @return
     */
    private boolean isPicType(String filepath) {
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
     * 是否为PNG JPG BMP等常见格式
     *
     * @param filepath
     * @return
     */
    private boolean isPng(String filepath) {
        boolean ispng = false;
        for (String exe : IBaseHandler.TRANSLIST_NORMAL_PIC) {
            if (filepath.endsWith(exe)) {
                ispng = true;
                break;
            }
        }
        return ispng;
    }

    /**
     * 获取视频第一帧
     *
     * @param filePath
     * @return
     */
    private InputStream getFirstFrameFromVideo(String filePath) {
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
     * 获取缩略图保存名 aaa.png--> aaa
     *
     * @param filename
     * @return
     */
    private String getPngName(String filename) {
        return filename.substring(0, filename.lastIndexOf(".")) + FILE_CACHE_EXE;
    }


    /**
     * 把头部KEY和VALUE转换成params形式
     *
     * @param request
     * @return
     */
    private Map<String, String> headToParams(HttpRequest request) {
        HashMap<String, String> params = new HashMap<>();
        for (Header head : request.getAllHeaders()) {
            params.put(head.getName(), head.getValue());
        }
        return params;
    }

    /**
     * 获取需要读取的起始坐标
     *
     * @param b
     * @param length
     * @return
     */
    private long getStartIndex(byte[] b, int length) {

        int index = 0;
        BufferedReader reader = new BufferedReader(new StringReader(new String(b, 0, length)));
        String content;
        StringBuffer sb = new StringBuffer();
        try {
            while ((content = reader.readLine()) != null) {
                index += (content.length() + "\r\n".length());
                if (content.length() == 0) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * 封装传输对象传递给UI
     *
     * @param ifRequest 请求
     * @param state     传输状态
     * @param current   当前进度
     * @return 传输对象
     */
    private Freesharing_FreeTranslist toTranslist(IFRequest ifRequest, int state, long current) {
        // 1.准备要素
        String picUrl = saveDir.getAbsolutePath() + File.separator + ifRequest.getFileName();
        Bitmap picBitmap = getDefaultItemBitmap(R.drawable.freesharing_item_unknown);
        // 获取缩略图路径 (/storage/0/mnt/aaa.png)
        String thumbUrl = cacheDir.getAbsolutePath() + File.separator + IBaseHandler.FILE_CACHE_PRE + getPngName(ifRequest.getFileName());
        if (state == IBaseHandler.TRANSLIST_STATE_UPLOADING | state == IBaseHandler.TRANSLIST_STATE_FAILED) {
            // 传输中 | 传输失败--> 获取默认
            if (ifRequest.getFileType() == IBaseHandler.TRANSLIST_FILETYPE_PIC) {
                picBitmap = getDefaultItemBitmap(R.drawable.freesharing_item_photo);
            } else if (ifRequest.getFileType() == IBaseHandler.TRANSLIST_FILETYPE_VIDEO) {
                picBitmap = getDefaultItemBitmap(R.drawable.freesharing_item_video);
            } else {
                picBitmap = getDefaultItemBitmap(R.drawable.freesharing_item_unknown);
            }
        } else {
            // 传输成功--> 获取缩略图
            picBitmap = FormatTools.getInstance().Bytes2Bitmap(FormatTools.File2byte(thumbUrl));
        }
        String filename = ifRequest.getFileName();
        String ip = ifRequest.getIp();
        String phone = ifRequest.getPhonename();
        String size = state == TRANSLIST_STATE_SUCCESS ? // 成功状态--> 显示原大小
                              Formatter.formatFileSize(context, ifRequest.getSize()) : // 原大小
                              getSize(current, ifRequest.getSize());// 进度
        int progress = getProgress(current, ifRequest.getSize());
        boolean isSend = false;
        int fileType = ifRequest.getFileType();

        // 2.整合freeTranslist对象
        Freesharing_FreeTranslist ta = new Freesharing_FreeTranslist();
        ta.setPicUrl(picUrl);
        ta.setPicBitmap(picBitmap);
        ta.setThumbUrl(thumbUrl);
        ta.setFilename(filename);
        ta.setIp(ip);
        ta.setPhone(phone);
        ta.setSize(size);
        ta.setState(state);
        ta.setProgress(progress);
        ta.setSend(isSend);
        ta.setFileType(fileType);

        return ta;
    }

    /**
     * 获取整型进度值
     *
     * @param current
     * @param total
     * @return
     */
    private int getProgress(long current, long total) {
        return (int) (current * 100d / total);
    }

    /**
     * 获取进度字符
     *
     * @param current
     * @param total
     * @return
     */
    private String getSize(long current, long total) {
        String currents = Formatter.formatFileSize(context, current);
        String totals = Formatter.formatFileSize(context, total);
        return currents + "/" + totals;
    }

    /**
     * 获取默认的item图
     *
     * @param id
     * @return
     */
    private Bitmap getDefaultItemBitmap(int id) {
        return FormatTools.getInstance().drawable2Bitmap(context.getResources().getDrawable(id));
    }
}
