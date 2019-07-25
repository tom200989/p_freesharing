package com.p_freesharing.p_freesharing.core.server.handler;


/**
 * Created by qianli.ma on 2018/4/17 0017.
 */

public class IBaseHandler {

    /* request: message */
    public static final int TYPE_DIALOG = 0;// 请求DIALOG
    public static final int TYPE_OK = 1;// 请求回复OK
    public static final int TYPE_NOK = 2;// 请求回复 NOK
    public static final int TYPE_CANCEL = 3;// 发送方取消
    public static final int TYPE_PHONE = 4;// 请求本机手机名称
    public static final int TYPE_ERROR = 5;// 服务器反馈错误
    public static final int TYPE_UNKNOWN = 6;// 服务器反馈未知状态
    public static final int TYPE_OVER = 7;// 会话结束
    public static final int TYPE_PRE_START = 8;// 准备发送文件
    public static final int TYPE_PRE_STOP = 9;// 取消准备发送文件
    public static final int TYPE_MAX_PHONE = 10;// 连接手机数量达到最大(最大只允许两部手机同时连接)
    public static final int TYPE_NO_FILE_REMAINED = 11;// 单个IP可上传的剩余数量为0--> 没有剩余空间
    public static final int TYPE_LESS_FILE_REMAINED = 12;// 单个IP可上传的剩余数量不足--> 有剩余空间, 但请求数大于该空间
    public static final int TYPE_NO_REPLY = 13;// 上次请求对方没有回复
    /* request message */
    public static final String REQUEST_DIALOG = "dialog";// 附加字段1
    public static final String REQUEST_RETRY = "retry";// 附加字段2
    public static final String REQUEST_INTERRUPT = "interrupt";// 附加字段3
    /* request: file */
    public static final int TYPE_UPLOAD_PERMIT = 14;// 允许上传文件
    public static final int TYPE_UPLOAD_ERROR = 15;// 不支持上传
    public static final int TYPE_UPLOAD_THREAD_OVER_COUNT = 16;// 超过线程数
    public static final int TYPE_UPLOADING = 17;// 正在上传
    public static final int TYPE_UPLOAD_SERVER_CANCEL = 18;// 服务器取消上传
    public static final int TYPE_UPLOAD_CLIENT_CANCEL = 19;// 客户端取消上传
    public static final int TYPE_UPLOAD_SUCCESS = 20;// 上传成功
    public static final int TYPE_UPLOAD_NOW = 21;// 正式上传
    public static final int TYPE_CLIENT_INTERRUPT = 22;// 客户端中断上传
    /* request file */
    public static final String id = "id";// 会话ID
    public static final String ip = "ip";// 请求IP
    public static final String phonename = "phonename";// 请求手机名称
    public static final String requesttype = "requesttype";// 请求类型
    public static final String path = "path";// 请求文件路径
    public static final String size = "size";// 请求文件大小
    public static final String filetype = "filetype";// 请求文件类型
    public static final String filename = "filename";// 请求文件类型
    public static final String system = "system";// 系统
    public static final String mimetype = "mimetype";// 元类型
   

    /* response: server */
    public static final int STATE_200 = 200;// 成功
    public static final int STATE_404 = 404;// 未知状态
    public static final int STATE_503 = 503;// 服务器错误
    /* response: server */
    public static final String SERVER_ERROR_ID = "-1";// 服务器出错
    public static final String SERVER_UNKNOWN_ID = "-2";// 服务器接收到未知请求类型
    public static final String SERVER_OVER_ID = "-3";// 服务器接收到会话结束
    /* response: server */
    public static final int MAX_PHONE_COUNT = 2;// 最大设备连接数m
    public static final int MAX_SINGLE_COUNT = 9;// 最大单次连接数(最多单次可以上传多少个文件)n
    public static final int MAX_FILE_COUNT = MAX_SINGLE_COUNT * MAX_PHONE_COUNT;// m * n个文件
    public static final String FILE_SPLIT = "#smartlink#";// 切割符
    public static final String FILE_SAVE_DIR = "smartlink";// 文件保存的目录
    public static final String FILE_CACHE_DIR = "smartcache";// 缓存文件夹
    public static final String FILE_CACHE_HISTORY = "smarthistory.txt";// 缓存历史记录
    public static final String FILE_CACHE_PRE = "smartthumb_";// 缓存文件名前缀
    public static final String FILE_CACHE_EXE = ".png";// 缓存文件后缀(全部以图片显示)
    /* response: server */
    public static final int ANDROID = 0;// android系统
    public static final int IOS = 1;// IOS系统
    /* response: server */
    public static final String HEAD_BOUNDARY = "boundary";// boundary
    public static final String HEAD_MIMETYPE = "mimetype";// mimetype
    public static final String HEAD_CONTENT_TYPE = "Content-Type";// Content-Type
    public static final String HEAD_FILENAME = "filename";// filename
    public static final String HEAD_SYSTEM = "system";// system

    /* translist state */
    public static final int TRANSLIST_STATE_UPLOADING = 0;// 状态: 传输中
    public static final int TRANSLIST_STATE_FAILED = -1;// 状态: 传输失败
    public static final int TRANSLIST_STATE_SUCCESS = 1;// 状态: 传输完成
    public static final int TRANSLIST_FILETYPE_PIC = 1;// 图片类型
    public static final int TRANSLIST_FILETYPE_VIDEO = 2;// 视频类型
    public static final int TRANSLIST_FILETYPE_UNKNOWN = 3;//  未知类型

    /* file type */
    public static final String[] TRANSLIST_FILE_VIDEO = {// 视频
            ".rm",//
            ".rmvb",//
            ".mpeg1",//
            ".mpeg2",//
            ".mpeg3",//
            ".mpeg4",//
            ".mpeg-1/2",//
            ".divx",//
            ".divx",//
            ".dv-avi",//
            ".mov",//
            ".mtv",//
            ".mkv",//
            ".mts",//
            ".dat",//
            ".wmv",//
            ".avi",//
            ".3gp",//
            ".amv",//
            ".dmv",//
            ".navi",//
            ".webm",//
            ".hddvd",//
            ".qsv",//
            ".swf",//
            ".vob",//
            ".flv",//
            ".f4v"//
    };

    public static final String[] TRANSLIST_FILE_PIC = {// 图片
            ".webp",//
            ".bmp",//
            ".pcx",//
            ".tiff",//
            ".gif",//
            ".jpeg",//
            ".jpg",//
            ".tga",//
            ".exif",//
            ".fpx",//
            ".svg",//
            ".psd",//
            ".cdr",//
            ".pcd",//
            ".dxf",//
            ".ufo",//
            ".eps",//
            ".ai",//
            ".png",//
            ".hdri",//
            ".raw",//
            ".wmf",//
            ".flic",//
            ".emf",//
            ".ico"//
    };

    public static final String[] TRANSLIST_NORMAL_PIC = {".png", ".jpg", ".jpeg", ".bmp"};// 常用的图片格式

}
