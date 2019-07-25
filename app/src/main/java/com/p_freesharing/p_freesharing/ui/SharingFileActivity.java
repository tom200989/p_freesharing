package com.p_freesharing.p_freesharing.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.R2;
import com.p_freesharing.p_freesharing.adapter.FreeChoiceUserAdapter;
import com.p_freesharing.p_freesharing.adapter.FreeImageAdapter;
import com.p_freesharing.p_freesharing.adapter.FreeTranslistAdapter;
import com.p_freesharing.p_freesharing.adapter.FreeTranslistDialogAdapter;
import com.p_freesharing.p_freesharing.adapter.FreeVideoAdapter;
import com.p_freesharing.p_freesharing.bean.ConnectedList;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeImage;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeTranslist;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeUser;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeVideo;
import com.p_freesharing.p_freesharing.bean.Freesharing_Image;
import com.p_freesharing.p_freesharing.bean.Freesharing_Video;
import com.p_freesharing.p_freesharing.bean.InteractiveRequestBean;
import com.p_freesharing.p_freesharing.bean.InteractiveResponceBean;
import com.p_freesharing.p_freesharing.core.client.CompareHelper;
import com.p_freesharing.p_freesharing.core.client.FreeCacheUtils;
import com.p_freesharing.p_freesharing.core.client.FreeClientHelper;
import com.p_freesharing.p_freesharing.core.client.FreeFilter;
import com.p_freesharing.p_freesharing.core.provider.AbstructProvider;
import com.p_freesharing.p_freesharing.core.provider.ImageProvider;
import com.p_freesharing.p_freesharing.core.provider.VideoProvider;
import com.p_freesharing.p_freesharing.core.server.bean.IMRequest;
import com.p_freesharing.p_freesharing.core.server.bean.IMResponse;
import com.p_freesharing.p_freesharing.core.server.core.FreeServerHelper;
import com.p_freesharing.p_freesharing.core.server.core.NetUtils;
import com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler;
import com.p_freesharing.p_freesharing.core.server.handler.IFileHandler;
import com.p_freesharing.p_freesharing.core.server.handler.IMHandler;
import com.p_freesharing.p_freesharing.helper.DeviceHelper;
import com.p_freesharing.p_freesharing.helper.FreeShareService;
import com.p_freesharing.p_freesharing.helper.RequestParamHelper;
import com.p_freesharing.p_freesharing.utils.CA;
import com.p_freesharing.p_freesharing.utils.FileUtils;
import com.p_freesharing.p_freesharing.utils.FormatTools;
import com.p_freesharing.p_freesharing.utils.Logs;
import com.p_freesharing.p_freesharing.utils.OtherUtils;
import com.p_freesharing.p_freesharing.utils.SPUtils;
import com.p_freesharing.p_freesharing.utils.TimerHelper;
import com.p_freesharing.p_freesharing.utils.ToastUtil_m;
import com.p_freesharing.p_freesharing.widget.CancelView;
import com.p_freesharing.p_freesharing.widget.ConnView;
import com.p_freesharing.p_freesharing.widget.InterruptView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.FILE_CACHE_HISTORY;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.FILE_SPLIT;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.REQUEST_DIALOG;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.REQUEST_INTERRUPT;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_FILETYPE_PIC;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_FILETYPE_VIDEO;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_STATE_FAILED;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_STATE_SUCCESS;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_STATE_UPLOADING;

public class SharingFileActivity extends Activity {

    @BindView(R2.id.iv_free_back)
    ImageView ivFreeBack;// 返回
    @BindView(R2.id.tv_free_Title)
    TextView tvFreeTitle;// 标题
    @BindView(R2.id.tv_free_done)
    TextView tvFreeDone;// done

    @BindView(R2.id.tv_free_clearHistory)
    TextView tvFreeClearHistory;// 清空历史

    @BindView(R2.id.rl_free_images_tag)
    RelativeLayout rlFreeImagesTag;// 图片tab
    @BindView(R2.id.v_free_under_images_tag)
    View vFreeUnderImagesTag;// 图片tab下划线
    @BindView(R2.id.rl_free_videos_tag)
    RelativeLayout rlFreeVideosTag;// 视频tab
    @BindView(R2.id.v_free_under_videos_tag)
    View vFreeUnderVideosTag;// 视频tab下划线
    @BindView(R2.id.rl_free_transferList_tag)
    RelativeLayout rlFreeTransferListTag;// 传输列表tab
    @BindView(R2.id.v_free_under_transferList_tag)
    View vFreeUnderTransferListTag;// 传输列表tab下划线

    @BindView(R2.id.rl_free_recycle)
    RelativeLayout rlFreeRecycle;// 列表父布局

    @BindView(R2.id.rl_free_image)
    RelativeLayout rlFreeImage;// 图片列表布局
    @BindView(R2.id.rcv_free_Imageitems)
    RecyclerView rcvFreeImageItems;// 图片显示列表
    @BindView(R2.id.rl_free_noImage)
    RelativeLayout rlFreeNoImage;// no image

    @BindView(R2.id.rl_free_video)
    RelativeLayout rlFreeVideo;// 视频列表布局
    @BindView(R2.id.rcv_free_Videoitems)
    RecyclerView rcvFreeVideoitems;// 视频显示列表
    @BindView(R2.id.rl_free_noVideo)
    RelativeLayout rlFreeNoVideo; // no video

    @BindView(R2.id.rl_free_transferList)
    RelativeLayout rlFreeTransferList;// 传输列表布局
    @BindView(R2.id.rcv_free_transferListItems)
    RecyclerView rcvFreeTransferListItems;// 传输列表
    @BindView(R2.id.rl_free_noTransferList)
    RelativeLayout rlFreeNoTransferList;// no transfer list

    @BindView(R2.id.rl_free_noDevice)
    RelativeLayout rlFreeNoDevice;// 没有检测到设备

    @BindView(R2.id.rl_free_detailImage)
    RelativeLayout rlFreeDetailImage;// 大图总布局
    @BindView(R2.id.rl_free_detailImage_banner)
    RelativeLayout rlFreeDetailImageBanner;// 标题所在的banner
    @BindView(R2.id.iv_free_detailImage_back)
    ImageView ivFreeDetailImageBack;// 点击返回
    @BindView(R2.id.tv_free_detailImage_title)
    TextView tvFreeDetailImageTitle;// 标题
    @BindView(R2.id.iv_free_detailImage)
    ImageView ivFreeDetailImage;// 显示大图

    @BindView(R2.id.rl_free_transferList_dialog)
    RelativeLayout rlFreeTransferListDialog;// 待接收面板
    @BindView(R2.id.rcv_free_transferList_dialog)
    RecyclerView rcvFreeTransferListDialog;// 待接收列表

    @BindView(R2.id.rl_free_choice_user)
    RelativeLayout rlFreeChoiceUser;// 已联网的用户群组
    @BindView(R2.id.rl_free_choice_user_hide)
    RelativeLayout btFreeChoiceUserHide;// 收缩点击面板按钮
    @BindView(R2.id.rcv_free_choice_user)
    RecyclerView rcvFreeChoiceUser;// 用户列表

    // @BindView(R2.id.cv_free_user)
    ConnView cvFreeUser;// 扫描等待
    // @BindView(R2.id.cancel_free_user)
    CancelView cancelFreeUser;// 取消连接
    // @BindView(R2.id.interrupt_free_user)
    InterruptView interruptFreeUser;// 中断传输

    private String TAG = "FreeSA";
    private String TAG_PROGRESS = "FreeSharingActivity_progress";
    private View[] underLines;// tab下划线集
    private View[] noViews;// 没有数据视图集
    private final int TAB_IMAGES = 0;
    private final int TAB_VIDEO = 1;
    private final int TAB_TRANSFER = 2;
    private List<Freesharing_FreeImage> freesharingFreeImages = new ArrayList<>();
    private List<Freesharing_FreeVideo> freesharingFreeVideos = new ArrayList<>();
    private List<Freesharing_FreeTranslist> freesharingFreeTranslistList = new ArrayList<>();
    private List<IMRequest> freeTranslistDialogList = new ArrayList<>();
    private List<Freesharing_FreeUser> freesharingFreeUsers = new ArrayList<>();
    private List<Object> temps = new ArrayList<>();// 临时集合(存放用户组时的线程完毕标记)
    private FreeImageAdapter freeImageAdapter;// 图片显示适配器
    private FreeVideoAdapter freeVideoAdapter;// 视频显示适配器
    private FreeTranslistAdapter freeTranslistAdapter;// 传输列表适配器
    private FreeTranslistDialogAdapter freeTranslistDialogAdapter;// 接收文件会话框适配器
    private FreeChoiceUserAdapter freeChoiceUserAdapter;// 用户组弹窗适配器
    private int maxCount;// 应该显示的图片|视频个数
    private boolean isImageLoading = false;// 是否正在加载图片
    private boolean isVideoLoading = false;// 是否正在加载视频
    private boolean isScanUser = false;// true: 正在扫描
    private List<Freesharing_Image> images_resource = new ArrayList<>();// 手机图库读取出来的对象集合
    private List<Freesharing_Video> videos_resource = new ArrayList<>();// 手机媒体库读取的对象集合
    public List<Freesharing_FreeImage> selectImages = new ArrayList<>();// 选中的FreeImage
    public List<Freesharing_FreeVideo> selectVideos = new ArrayList<>();// 选中的FreeVideo
    private Thread imageThread;// 图片子线程
    private Thread videoThread;// 视频子线程
    private Thread translistThread;// 视频子线程
    private String freeSharingTitle;
    private TimerHelper requestTimer;// 请求检测连接后启动该定时器
    private Handler handler;
    private File saveDir;// 保存文件夹
    private File cacheDir;// 缓存文件夹
    private File cacheHistory;// 缓存历史记录
    private TimerHelper transTimerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freesharing_activity_free_sharing);
        ButterKnife.bind(this);
        cvFreeUser = findViewById(R.id.cv_free_user);
        cancelFreeUser = findViewById(R.id.cancel_free_user);
        interruptFreeUser = findViewById(R.id.interrupt_free_user);
        // 创建缓存文件夹
        createNeedDir();
        // 启动服务器
        startServer();
        resetAll();// 重置所有数据
        getInitShowNum();// 获取rcv初始应显示的数量
        // 定时器--> 用于检测是否有文件在传输
        TransTimer();
    }

    /**
     * 定时器--> 用于检测是否有文件在传输
     */
    private void TransTimer() {
        transTimerHelper = new TimerHelper(this) {
            @Override
            public void doSomething() {
                runOnUiThread(() -> {
                    boolean isTrans = false;
                    // 遍历当前的传输集合--> 检测是否有文件正在传输
                    for (Freesharing_FreeTranslist ta : freesharingFreeTranslistList) {
                        if (ta.getState() == IBaseHandler.TRANSLIST_STATE_UPLOADING) {
                            isTrans = true;
                            break;
                        }
                    }
                    if (isTrans) {// 如果有--> 则隐藏[清空历史]面板
                        tvFreeClearHistory.setVisibility(View.GONE);
                        rlFreeNoTransferList.setVisibility(View.GONE);
                    } else {// 如果没有--> 则判断是否有缓存历史
                        List<Freesharing_FreeTranslist> tas = FreeCacheUtils.readCacheProperty();
                        tvFreeClearHistory.setVisibility(freesharingFreeTranslistList.size() > 0 &// 传输列表个数 > 0 & 当前处于传输布局
                                                                 rlFreeTransferList.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                        rlFreeNoTransferList.setVisibility(tas.size() > 0 | freesharingFreeTranslistList.size() > 0 ? View.GONE : View.VISIBLE);
                    }
                });
            }
        };
        transTimerHelper.start(2000);
    }

    /**
     * 创建必要文件夹
     */
    private void createNeedDir() {
        saveDir = FileUtils.createFreeSharingSaveDir();
        cacheDir = FileUtils.createFreeSharingCacheDir();
        cacheHistory = FileUtils.createFreeSharingCacheHistory();
    }

    @Override
    public void onBackPressed() {
        boolean isDetailImageShow = rlFreeDetailImage.getVisibility() == View.VISIBLE;
        boolean isFreeTranslistDialogShow = rlFreeTransferListDialog.getVisibility() == View.VISIBLE;
        boolean isFreeUserShow = rlFreeChoiceUser.getVisibility() == View.VISIBLE;
        boolean isFreeUserWaitConn = cvFreeUser.getVisibility() == View.VISIBLE;
        boolean isFreeUserCancel = cancelFreeUser.getVisibility() == View.VISIBLE;
        // 1.文件接收对话框显示(优先级1)
        if (isFreeTranslistDialogShow) {
            toast(R.string.freesharing_please_choose_to_receice_or_reject);
        } else if (isDetailImageShow) { // 大图显示则消隐(优先级2)
            rlFreeDetailImage.setVisibility(View.GONE);
        } else if (isFreeUserCancel) {// 取消上次请求
            cancelFreeUser.setVisibility(View.GONE);
        } else if (isFreeUserShow | isFreeUserWaitConn) {// 用户面板显示(优先级3)
            clearFreeUsers();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        transTimerHelper.stop();
    }

    /* progress : 服务器的进度回调 */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getIFhandlerProgress(Freesharing_FreeTranslist ta) {
        rlFreeNoTransferList.setVisibility(View.GONE);
        Logs.t(TAG).ii("state : " + ta.getState());
        // 显示服务器传输进度
        Freesharing_FreeTranslist nowTa = FreeServerHelper.isFreeTransExist(ta, freesharingFreeTranslistList);
        if (nowTa == null) {// 该元素是首次出现--> 加到传输集合里
            freesharingFreeTranslistList.add(0, ta);
        } else {
            nowTa.setSize(ta.getSize());
            nowTa.setProgress(ta.getProgress());
            nowTa.setState(ta.getState());
        }
        freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
    }

    /* state : 服务器的状态回调 */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getIMhandlerState(IMRequest request) {
        int requestType = request.getType();
        Logs.t(TAG).ii("request type: " + requestType);
        Logs.t(TAG).ii("request attach: " + request.getAttach());
        switch (requestType) {
            case IBaseHandler.TYPE_DIALOG:// 显示对话框
                // 解决再次进入传输界面时, 自动弹出上一次的申请传输会话框的问题(已解决)
                // (主要原因是IMhandler里的eventbus采用了粘性poststicky模式--> 改为post)
                // 添加接收到的请求组
                rlFreeTransferListDialog.setVisibility(View.VISIBLE);
                freeTranslistDialogList.add(0, request);
                freeTranslistDialogAdapter.notifys(freeTranslistDialogList);
                break;
            case IBaseHandler.TYPE_OK:// 对方点击了OK
                // 上传文件(根据对方反馈回来的文件信息进行上传)
                startUpload(request);

                break;
            case IBaseHandler.TYPE_NOK:// 对方点击了cancel

                // 提示对方拒绝了接收文件
                toast(R.string.freesharing_the_other_party_rejected_your_transfer_request);
                // 重置数据
                resetFreeUser();
                break;

            case IBaseHandler.TYPE_CANCEL:// 客户端发起了取消本次请求
                hideCancelItem(request);
                break;
            case IBaseHandler.TYPE_CLIENT_INTERRUPT:// 客户端中断
                toast(R.string.freesharing_file_is_interrupted_to_upload);
                break;
        }
    }

    /**
     * 开始上传
     */
    private void startUpload(IMRequest request) {

        // 1.封装json数据
        for (Freesharing_FreeImage selectImage : selectImages) {
            // 2.发送文件
            sendBatchFile(selectImage, request, selectImage.getPath());
        }
        for (Freesharing_FreeVideo selectVideo : selectVideos) {
            sendBatchFile(selectVideo, request, selectVideo.getPath());
        }
        // 3.跳转到传输列表界面
        clickProcess(TAB_TRANSFER);
        // 4.收起用户面板
        rlFreeChoiceUser.setVisibility(View.GONE);
        // 5.重置用户组面板状态
        resetFreeUser();
        // 6.重置已选文件集合 + 刷新图片recycler以及视频recycler适配器 
        clearChoice();
        // 7.重置标题
        showTitleAndDoneButton();

    }

    /**
     * 清除已选文件
     */
    private void clearChoice() {
        // 1.清空待选区
        selectVideos.clear();
        selectImages.clear();
        // 2.重置图片视频集合
        resetImageAndVideo();
        // 3.更新适配器
        freeImageAdapter.notifys(freesharingFreeImages);
        freeVideoAdapter.notifys(freesharingFreeVideos);
    }

    /**
     * 重置图片视频集合
     */
    private void resetImageAndVideo() {
        for (Freesharing_FreeImage freesharingFreeImage : freesharingFreeImages) {
            freesharingFreeImage.setSelected(false);
        }
        for (Freesharing_FreeVideo freesharingFreeVideo : freesharingFreeVideos) {
            freesharingFreeVideo.setSelected(false);
        }
    }


    /**
     * 上传文件
     *
     * @param object  Freesharing_FreeImage Freesharing_FreeVideo
     * @param request 服务器反馈的request client
     * @param path    要上传的文件路径
     */
    private void sendBatchFile(Object object, IMRequest request, String path) {

        // 1.准备上传参数
        String server_ip = request.getIp();// http://192.168.1.103:6688/IF
        String client_ip = NetUtils.getLocalIPAddress().getHostAddress();
        String url = "http://" + server_ip + ":" + FreeShareService.port + "/" + FreeShareService.if_intercept;
        File file = new File(path);
        long size = file.length();
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", file));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        int filetype = object instanceof Freesharing_FreeImage ?// 是否为图片
                               TRANSLIST_FILETYPE_PIC :// PIC
                               object instanceof Freesharing_FreeVideo ?// 是否为视频
                                       IBaseHandler.TRANSLIST_FILETYPE_VIDEO :// VIDEO
                                       IBaseHandler.TRANSLIST_FILETYPE_UNKNOWN;// UNKNOWN
        String mimetype = OtherUtils.getMimeType(file);// MIMETYPE(mimetype)
        Logs.t(TAG).ii("mimetype: " + mimetype);
        String newFileName = getNewFileName(file.getName());// 此处使用当前时间作为文件名

        // 2.封装上传头信息
        RequestParams rp = new RequestParams(url);
        rp.setCacheMaxAge(0);
        rp.addHeader(IBaseHandler.id, OtherUtils.getUUid());
        rp.addHeader(IBaseHandler.ip, client_ip);// 客户端IP
        rp.addHeader(IBaseHandler.phonename, Build.BRAND);// 客户端手机品牌
        rp.addHeader(IBaseHandler.requesttype, String.valueOf(IBaseHandler.TYPE_UPLOAD_NOW));
        rp.addHeader(IBaseHandler.size, String.valueOf(size));
        rp.addHeader(IBaseHandler.path, path);
        rp.addHeader(IBaseHandler.filetype, String.valueOf(filetype));
        rp.addHeader(IBaseHandler.filename, newFileName);
        rp.addHeader(IBaseHandler.system, String.valueOf(IBaseHandler.ANDROID));// 安卓系统
        rp.addHeader(IBaseHandler.mimetype, mimetype);// 元类型
        rp.setCancelFast(true);// 设置允许中断取消操作

        // 3.设置文件上传必要条件
        // rp.addHeader("Content-Type", "multipart/form-data");
        rp.setMultipart(true);
        rp.setRequestBody(body);

        // 4.添加到集合
        boolean isExist = FreeClientHelper.isExitFreeTranslist(file.getName(), client_ip, freesharingFreeTranslistList);
        if (!isExist) {
            Freesharing_FreeTranslist ta = new Freesharing_FreeTranslist();
            // 初始化
            ta.setPicUrl(path);
            ta.setPicBitmap(FreeClientHelper.getThumbBitmap(SharingFileActivity.this, file));
            ta.setThumbUrl("");
            ta.setFilename(newFileName);
            ta.setIp(server_ip);
            ta.setPhone(request.getPhonename());
            ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, 0, file.length()));
            ta.setState(IBaseHandler.TRANSLIST_STATE_UPLOADING);
            ta.setProgress(0);
            ta.setSend(true);
            ta.setFileType(filetype);
            freesharingFreeTranslistList.add(0, ta);
        }

        x.http().post(rp, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
                Logs.t(TAG).ii("file: " + newFileName + " is wait for upload");
            }

            @Override
            public void onStarted() {
                rlFreeNoTransferList.setVisibility(View.GONE);
                Logs.t(TAG).ii("file: " + newFileName + " is start for upload");
                freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                // 处理进度
                Logs.t(TAG_PROGRESS).ii("progress: " + newFileName + " is uploading");

                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, current, total));
                    ta.setProgress(current == total ? 100 : FreeClientHelper.getPercent(current, total));
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                Logs.t(TAG).ii("file: " + newFileName + " is success for upload");
                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, file.length(), file.length()));
                    ta.setProgress(100);
                    ta.setState(TRANSLIST_STATE_SUCCESS);
                    ta.setSize(Formatter.formatFileSize(SharingFileActivity.this, size));
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                String brokenPipe = "Broken pipe".toLowerCase();// 文件中断传输会出现
                String connectionReset = "Connection reset".toLowerCase();// 服务器被后台杀死会出现

                if (ex.getMessage().contains(brokenPipe)) {
                    toast(R.string.freesharing_the_file_has_been_discontinued);
                } else if (ex.getMessage().contains(connectionReset)) {
                    toast(R.string.freesharing_receiver_exception);
                }
                Logs.t(TAG).ee("file: " + newFileName + " is error for upload");
                Logs.t(TAG).ee("error: " + ex.getMessage());
                ex.printStackTrace();
                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, 0, file.length()));
                    ta.setProgress(0);
                    ta.setState(TRANSLIST_STATE_FAILED);
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logs.t(TAG).ee("file: " + newFileName + " is cancel for upload");
                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, 0, file.length()));
                    ta.setProgress(0);
                    ta.setState(TRANSLIST_STATE_FAILED);
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void onFinished() {
                Logs.t(TAG).vv("file: " + newFileName.substring(0, 10) + " is finish for upload");
            }
        });
    }

    /**
     * 启动服务器
     */
    private void startServer() {
        if (!OtherUtils.isServiceWork(this, FreeShareService.class)) {
            Intent intent = new Intent(this, FreeShareService.class);
            startService(intent);
        }
    }

    /**
     * 重置所有数据
     */
    private void resetAll() {
        IMHandler.currentIps.clear();
        freesharingFreeImages.clear();
        freesharingFreeVideos.clear();
        freesharingFreeTranslistList.clear();
        freeTranslistDialogList.clear();
        freesharingFreeUsers.clear();
        temps.clear();
        selectImages.clear();
        selectVideos.clear();
        images_resource.clear();
        videos_resource.clear();
        maxCount = 0;
        isImageLoading = false;
        isVideoLoading = false;
        handler = new Handler();
        saveUserState(View.GONE);
    }

    /**
     * 获取rcv初始应显示的数量
     */
    private void getInitShowNum() {
        rlFreeRecycle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取满屏情况下最大展示个数
                maxCount = OtherUtils.getRcvMaxCount(SharingFileActivity.this, rlFreeRecycle);
                // 注销布局监听器
                rlFreeRecycle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // 开始初始化
                init();
            }
        });
    }

    /**
     * 初始化一系列的行为
     */
    private void init() {
        initRes();// 初始化资源
        initAdapter();// 初始化适配器
        initData();// 初始化数据
        initUi();// 初始化UI
        initEvent();// 初始化监听
        initServer();// 初始化服务器
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        underLines = new View[]{vFreeUnderImagesTag, vFreeUnderVideosTag, vFreeUnderTransferListTag};
        noViews = new View[]{rlFreeNoImage, rlFreeNoVideo, rlFreeNoTransferList};
        freeSharingTitle = getString(R.string.freesharing_free_sharing_low);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        /* 图片适配器 */
        GridLayoutManager freeImageGlm = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        freeImageAdapter = new FreeImageAdapter(this, freesharingFreeImages);
        // 1.选择监听(勾选)
        freeImageAdapter.setOnFreeItemSelectClickListener((freesharingFreeImage, isSelected) -> {
            selectChoice(isSelected, freesharingFreeImage);
            // 更新标题数量
            showTitleAndDoneButton();
            // 更新适配器
            freeImageAdapter.notifyDataSetChanged();
        });
        // 2.点击大图监听
        freeImageAdapter.setOnFreeItemClickListener((freesharingFreeImage, position, total) -> {
            // 显示大图
            showBigPic(freesharingFreeImage.getPath());
            // 显示标题
            tvFreeDetailImageTitle.setText(String.valueOf(position + " / " + total));
        });
        // 3.设置选择文件超限监听
        freeImageAdapter.setOnOverSelectFilesListener(() -> {
            toast(R.string.freesharing_you_have_selected_more_than_9_files);
        });
        rcvFreeImageItems.setLayoutManager(freeImageGlm);
        rcvFreeImageItems.setAdapter(freeImageAdapter);

        /* 视频适配器 */
        GridLayoutManager freeVideoGlm = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        freeVideoAdapter = new FreeVideoAdapter(this, freesharingFreeVideos);
        freeVideoAdapter.setOnFreeItemSelectClickListener((freesharingFreeVideo, isSelected) -> {
            selectChoice(isSelected, freesharingFreeVideo);
            showTitleAndDoneButton();// 显示标题
            freeVideoAdapter.notifyDataSetChanged();
        });
        freeVideoAdapter.setOnFreeItemClickListener((freesharingFreeVideo, position, total) -> {
            showBigVideo(freesharingFreeVideo.getPath());
        });
        // 3.设置选择文件超限监听
        freeVideoAdapter.setOnOverSelectFilesListener(() -> {
            toast(R.string.freesharing_you_have_selected_more_than_9_files);
        });
        rcvFreeVideoitems.setLayoutManager(freeVideoGlm);
        rcvFreeVideoitems.setAdapter(freeVideoAdapter);

        /* 传输列表适配器 */
        LinearLayoutManager freeTranslistllm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        freeTranslistAdapter = new FreeTranslistAdapter(this, freesharingFreeTranslistList);

        // 取消中断
        freeTranslistAdapter.setOnTranslistCancelListener((ta, position) -> {
            // 弹出确认会话框
            interruptFreeUser.setVisibility(View.VISIBLE);
            interruptFreeUser.setOnClickbgListener(view -> interruptFreeUser.setVisibility(View.GONE));
            interruptFreeUser.setOnClickCancelListener(view -> interruptFreeUser.setVisibility(View.GONE));
            interruptFreeUser.setOnClickOkListener(view -> {
                interruptClick(ta);
            });


        });

        // 重试
        freeTranslistAdapter.setOnTranslistRetryListener((ta, position) -> {
            // 传输失败了重试(仅客户端--> 直接发送)
            sendRetryFile(ta);
        });

        // 显示大图
        freeTranslistAdapter.setOnShowDetailPicListener((ta, position) -> {
            whenTransationClick(ta);
        });
        rcvFreeTransferListItems.setLayoutManager(freeTranslistllm);
        rcvFreeTransferListItems.setAdapter(freeTranslistAdapter);

        /* 文件弹框适配器 */
        LinearLayoutManager freeDialogllm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        freeTranslistDialogAdapter = new FreeTranslistDialogAdapter(this, freeTranslistDialogList);
        freeTranslistDialogAdapter.setOnFreeTranslistDialotCancelClickListener(request -> {
            // 点击了取消--> 发送cancel请求
            clickOKOrCancel(request, false);
        });
        freeTranslistDialogAdapter.setOnFreeTranslistDialogOkListener(request -> {
            // 点击了OK--> 发送ok请求
            clickOKOrCancel(request, true);
        });
        rcvFreeTransferListDialog.setLayoutManager(freeDialogllm);
        rcvFreeTransferListDialog.setAdapter(freeTranslistDialogAdapter);

        /* 用户组适配器 */
        LinearLayoutManager freeUserllm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        freeChoiceUserAdapter = new FreeChoiceUserAdapter(this, null);
        freeChoiceUserAdapter.setOnUserClickListener((freesharingFreeUser, position) -> {
            clickOneUser(position);// 点击了某个用户
        });
        rcvFreeChoiceUser.setLayoutManager(freeUserllm);
        rcvFreeChoiceUser.setHasFixedSize(true);
        rcvFreeChoiceUser.setItemViewCacheSize(-1);
        rcvFreeChoiceUser.setAdapter(freeChoiceUserAdapter);

    }

    /**
     * 重新发送文件
     */
    private void sendRetryFile(Freesharing_FreeTranslist ta) {
        // 1.准备上传参数
        String server_ip = ta.getIp();// http://192.168.1.103:6688/IF
        String client_ip = NetUtils.getLocalIPAddress().getHostAddress();
        String url = "http://" + server_ip + ":" + FreeShareService.port + "/" + FreeShareService.if_intercept;
        File file = new File(ta.getPicUrl());
        long size = file.length();
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", file));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        int filetype = ta.getFileType();
        String mimetype = OtherUtils.getMimeType(file);
        String newFileName = ta.getFilename();

        // 2.封装上传头信息
        RequestParams rp = new RequestParams(url);
        rp.setCacheMaxAge(0);
        rp.addHeader(IBaseHandler.id, OtherUtils.getUUid());
        rp.addHeader(IBaseHandler.ip, client_ip);// 客户端IP
        rp.addHeader(IBaseHandler.phonename, Build.BRAND);// 客户端手机品牌
        rp.addHeader(IBaseHandler.requesttype, String.valueOf(IBaseHandler.TYPE_UPLOAD_NOW));
        rp.addHeader(IBaseHandler.size, String.valueOf(size));
        rp.addHeader(IBaseHandler.path, ta.getPicUrl());
        rp.addHeader(IBaseHandler.filetype, String.valueOf(filetype));
        rp.addHeader(IBaseHandler.filename, newFileName);
        rp.addHeader(IBaseHandler.system, String.valueOf(IBaseHandler.ANDROID));// 安卓系统
        rp.addHeader(IBaseHandler.mimetype, mimetype);// 元类型
        rp.setCancelFast(true);// 设置允许中断取消操作

        // 3.设置文件上传必要条件
        rp.setMultipart(true);
        rp.setRequestBody(body);

        // 4.重置对象各项状态
        ta.setPicUrl(ta.getPicUrl());
        ta.setPicBitmap(FreeClientHelper.getThumbBitmap(SharingFileActivity.this, file));
        ta.setThumbUrl("");
        ta.setFilename(newFileName);
        ta.setIp(server_ip);
        ta.setPhone(ta.getPhone());
        ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, 0, file.length()));
        ta.setState(IBaseHandler.TRANSLIST_STATE_UPLOADING);
        ta.setProgress(0);
        ta.setSend(true);
        ta.setFileType(filetype);

        x.http().post(rp, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
                Logs.t(TAG).ii("file: " + newFileName + " is wait for upload");
            }

            @Override
            public void onStarted() {
                Logs.t(TAG).ii("retry file: " + newFileName + " is start for upload");
                freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                // 处理进度
                Logs.t(TAG_PROGRESS).ii("progress: " + newFileName + " is uploading");

                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, current, total));
                    ta.setProgress(current == total ? 100 : FreeClientHelper.getPercent(current, total));
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                Logs.t(TAG).ii("retry file: " + newFileName + " is success for upload");
                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, file.length(), file.length()));
                    ta.setProgress(100);
                    ta.setState(TRANSLIST_STATE_SUCCESS);
                    ta.setSize(Formatter.formatFileSize(SharingFileActivity.this, size));
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                String brokenPipe = "Broken pipe".toLowerCase();// 文件中断传输会出现
                String connectionReset = "Connection reset".toLowerCase();// 服务器被后台杀死会出现

                if (ex.getMessage().contains(brokenPipe)) {
                    toast(R.string.freesharing_the_file_has_been_discontinued);
                } else if (ex.getMessage().contains(connectionReset)) {
                    toast(R.string.freesharing_the_other_side_has_been_off_line);
                }
                Logs.t(TAG).ee("retry file: " + newFileName + " is error for upload");
                Logs.t(TAG).ee("retry error: " + ex.getMessage());
                ex.printStackTrace();
                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, 0, file.length()));
                    ta.setProgress(0);
                    ta.setState(TRANSLIST_STATE_FAILED);
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logs.t(TAG).ee("retry file: " + newFileName + " is cancel for upload");
                Freesharing_FreeTranslist ta = FreeClientHelper.getExistTrans(newFileName, server_ip, freesharingFreeTranslistList);
                if (ta != null) {
                    ta.setSize(FreeClientHelper.getSize(SharingFileActivity.this, 0, file.length()));
                    ta.setProgress(0);
                    ta.setState(TRANSLIST_STATE_FAILED);
                    freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
                }
            }

            @Override
            public void onFinished() {
                Logs.t(TAG).vv("retry file: " + newFileName.substring(0, 10) + " is finish for upload");
            }
        });
    }

    /**
     * 中断传输
     */
    private void interruptClick(Freesharing_FreeTranslist ta) {
        if (ta.isSend()) {// 客户端点击取消
            // 发送中断请求
            requestInterrupt(ta);
        } else {// 服务器端点击取消
            // 清除集合里的文件名
            IFileHandler.uploadingFileSet.remove(ta.getIp() + FILE_SPLIT + ta.getFilename());
            interruptFreeUser.setVisibility(View.GONE);
        }
    }

    /**
     * 发送中断请求
     */
    private void requestInterrupt(Freesharing_FreeTranslist ta) {

        List<String> interrupts = new ArrayList<>();
        interrupts.add(ta.getFilename());
        String localIp = NetUtils.getLocalIPAddress().getHostAddress();

        String url = "http://" + ta.getIp() + ":" + FreeShareService.port + "/" + FreeShareService.im_intercept;
        String jsonForDialog = getInterruptImBody(localIp, IBaseHandler.TYPE_CLIENT_INTERRUPT, interrupts);
        RequestParams entity = RequestParamHelper.getEntity(url, jsonForDialog);// 得到实体
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                IMResponse response = JSONObject.parseObject(result, IMResponse.class);
                Logs.t(TAG).ii(response.toString());
                toast(R.string.freesharing_the_file_has_been_discontinued);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toast(R.string.freesharing_interrupt_failure);
                Logs.t(TAG).ee("interrupt error: " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                toast(R.string.freesharing_interrupt_cancellation);
                Logs.t(TAG).ee("interrupt cancel: " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                interruptFreeUser.setVisibility(View.GONE);
                Logs.t(TAG).ii("interrupt finish");
            }
        });
    }


    /**
     * 当在历史列表中被点击
     * 传输中: 提示; 成功--> 显示; 失败--> 提示
     */
    private void whenTransationClick(Freesharing_FreeTranslist ta) {
        if (ta.getState() == TRANSLIST_STATE_SUCCESS) {// 成功

            if (ta.getFileType() == TRANSLIST_FILETYPE_PIC) {
                showBigPic(ta.getPicUrl());
            } else if (ta.getFileType() == TRANSLIST_FILETYPE_VIDEO) {
                showBigVideo(ta.getPicUrl());
            } else {
                toast(R.string.freesharing_file_type_cannot_be_identified);
            }

        } else if (ta.getState() == TRANSLIST_STATE_UPLOADING) {// 传输中 
            toast(R.string.freesharing_the_file_is_being_transferred_please_wait_a_moment);
        } else {// 失败
            if (ta.isSend()) {
                if (ta.getFileType() == TRANSLIST_FILETYPE_PIC) {
                    showBigPic(ta.getPicUrl());
                } else if (ta.getFileType() == TRANSLIST_FILETYPE_VIDEO) {
                    showBigVideo(ta.getPicUrl());
                } else {
                    toast(R.string.freesharing_file_type_cannot_be_identified);
                }
            } else {
                toast(R.string.freesharing_file_transfer_failed_unable_to_open);
            }

        }
    }

    /**
     * 调用系统播放器进行播放
     */
    private void showBigVideo(String videoUrl) {
        try {// 检测到播放器则进行播放
            String url = "file://" + videoUrl;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String type = "video/*";
            Uri uri = Uri.parse(url);
            intent.setDataAndType(uri, type);
            startActivity(intent);
        } catch (Exception e) {// 没有检测到播放器--> 防止崩溃--> 提示
            Logs.t(TAG).ee("free sharing activity: video play error: \n" + e.getMessage());
            e.printStackTrace();
            toastLong(R.string.freesharing_free_sharing_noMediaPlayer);
        }
    }

    /**
     * 显示详情大图
     */
    private void showBigPic(String picUrl) {
        rlFreeDetailImage.setVisibility(View.VISIBLE);
        Bitmap bitmap = FormatTools.getInstance().file2FitPhoneBitmap(this, picUrl);
        ivFreeDetailImage.setImageBitmap(bitmap);
    }

    /**
     * 点击了某个用户
     */
    private void clickOneUser(int position) {

        // 检测是否之前有请求过？--> -1:首次请求
        int lastRequestPosition = isLastRequest();

        if (lastRequestPosition != -1) {/* 检测到存在上次请求 */
            cancelFreeUser.setVisibility(View.VISIBLE);
            cancelFreeUser.setOnBgClickListener(() -> cancelFreeUser.setVisibility(View.GONE));
            cancelFreeUser.setOnCancelClickListener(() -> cancelFreeUser.setVisibility(View.GONE));
            cancelFreeUser.setOnOkClickListener(() -> {
                // 取消对应的请求
                cancelLastRequest(lastRequestPosition);
            });
        } else {/* 检测到这是首次请求 */
            cancelFreeUser.setVisibility(View.GONE);
            for (int i = 0; i < freesharingFreeUsers.size(); i++) {
                // 0.1当前选中的显示已经连接
                freesharingFreeUsers.get(i).setConnecting(i == position);
            }
            // 更新UI
            freeChoiceUserAdapter.notifys(freesharingFreeUsers);
            // 1.向对方询问是否可以发送
            inquiryRequest(freesharingFreeUsers.get(position));
            // 4.连接成功后
            // 清空集合,更新图片视频适配器
            // 跳转到translist标签
        }
    }

    /**
     * 取消上次请求
     *
     * @param lastRequestPosition 上次请求的用户位标
     */
    private void cancelLastRequest(int lastRequestPosition) {
        Freesharing_FreeUser freesharingFreeUser = freesharingFreeUsers.get(lastRequestPosition);
        String url = "http://" + freesharingFreeUser.getIp() + ":" + FreeShareService.port + "/" + FreeShareService.im_intercept;
        String jsonForDialog = getIMBody(null, NetUtils.getLocalIPAddress().getHostAddress(), IBaseHandler.TYPE_CANCEL);
        RequestParams entity = RequestParamHelper.getEntity(url, jsonForDialog);// 得到实体
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                IMResponse response = JSONObject.parseObject(result, IMResponse.class);
                Logs.t(TAG).ii(response.toString());
                if (response.getReback() == IBaseHandler.STATE_200) {
                    cancelFreeUser.setVisibility(View.GONE);
                    resetFreeUser();
                } else {
                    Logs.t(TAG).ee("responce code is not 200");
                    cancelFreeUser.setVisibility(View.GONE);
                    toast(R.string.freesharing_network_exception_please_try_again);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logs.t(TAG).ee("error: " + ex.getMessage());
                cancelFreeUser.setVisibility(View.GONE);
                toast(R.string.freesharing_the_other_side_has_been_off_line);
                clickDone();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logs.t(TAG).ee("cancel request is cancel");
                cancelFreeUser.setVisibility(View.GONE);
                toast(R.string.freesharing_network_exception_please_try_again);
            }

            @Override
            public void onFinished() {
                cancelFreeUser.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 发送OK 或者 cancel 给客户端
     */
    private void clickOKOrCancel(IMRequest request, boolean isOk) {
        String localIp = NetUtils.getLocalIPAddress().getHostAddress();
        String url = "http://" + request.getIp() + ":" + FreeShareService.port + "/" + FreeShareService.im_intercept;
        Logs.t(TAG).ii("when click ok: " + request.getAttach());
        String jsonForDialog = getIMBody(request, localIp, isOk ? IBaseHandler.TYPE_OK : IBaseHandler.TYPE_NOK);
        RequestParams entity = RequestParamHelper.getEntity(url, jsonForDialog);// 得到实体
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                IMResponse response = JSONObject.parseObject(result, IMResponse.class);
                if (response.getType() == IBaseHandler.TYPE_OVER) {
                    // 提示对方已经offline
                    Logs.t(TAG).ii("clickOKOrCancel: TYPE_OVER: the_other_side_has_been_off_line");
                    toast(R.string.freesharing_the_other_side_has_been_off_line);
                } else if (response.getType() == IBaseHandler.TYPE_PRE_STOP) {
                    // 提示对方接收了你的NOK请求
                    toast(R.string.freesharing_the_other_party_accepts_your_refusal);
                    IMHandler.currentIps.remove(request.getIp());
                } else {
                    // 提示对方准备开始发送
                    toast(R.string.freesharing_the_other_is_preparing_to_send_it);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // 提示对方已经离线
                toast(R.string.freesharing_the_other_side_has_been_off_line);
                Logs.t(TAG).ee("clickOKOrCancel: onError: " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideCancelItem(request);
            }
        });
    }

    /**
     * 去除某个取消的Item的条目
     */
    private void hideCancelItem(IMRequest request) {
        String ip = request.getIp();
        int sessionSize = freeTranslistDialogList.size();
        // 1.删除集合对应的item
        for (int i = 0; i < sessionSize; i++) {
            if (freeTranslistDialogList.get(i).getIp().equalsIgnoreCase(ip)) {
                freeTranslistDialogList.remove(i);
                break;
            }
        }

        // 2. 刷新适配器
        freeTranslistDialogAdapter.notifys(freeTranslistDialogList);

        // 3.如果集合没有元素了, 则隐藏布局, 同时跳转到传输列表
        if (freeTranslistDialogList.size() <= 0) {
            rlFreeTransferListDialog.setVisibility(View.GONE);
            // 跳转到传输列表
            // clickProcess(TAB_TRANSFER);
        }

    }

    /**
     * 向对方询问是否可以发送
     */
    private void inquiryRequest(Freesharing_FreeUser freesharingFreeUser) {
        // 询问对方
        String url = "http://" + freesharingFreeUser.getIp() + ":" + FreeShareService.port + "/" + FreeShareService.im_intercept;
        String jsonForDialog = getDialogImBody(null, NetUtils.getLocalIPAddress().getHostAddress(), IBaseHandler.TYPE_DIALOG);
        RequestParams entity = RequestParamHelper.getEntity(url, jsonForDialog);// 得到实体
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                IMResponse response = JSONObject.parseObject(result, IMResponse.class);
                // 请求成功
                if (response.getReback() == IBaseHandler.STATE_200) {

                    // 上次请求没有回复--> 不允许重复请求
                    if (response.getType() == IBaseHandler.TYPE_NO_REPLY) {
                        Logs.t(TAG).ii("the last request had no reback");
                        toastLong(R.string.freesharing_the_last_request_did_not_respond_to_many_requests);

                        // 达到最大设备连接数
                    } else if (response.getType() == IBaseHandler.TYPE_MAX_PHONE) {
                        Logs.t(TAG).ii("the other party had reach max conn count");
                        toastLong(R.string.freesharing_the_receiving_device_has_reached_the_maximum);
                        resetFreeUser();

                        // 达到单个设备上传文件连接数
                    } else if (response.getType() == IBaseHandler.TYPE_NO_FILE_REMAINED) {
                        Logs.t(TAG).ii("current devices had reach max request count");
                        toastLong(R.string.freesharing_your_current_device_has_the_maximum_number_of_uploads);
                        resetFreeUser();

                        // 当前请求文件上传个数大于单个设备可用的文件连接数
                    } else if (response.getType() == IBaseHandler.TYPE_LESS_FILE_REMAINED) {
                        Logs.t(TAG).ii("current remain contain less than request count");
                        String tip = String.format(getString(R.string.freesharing_files_can_also_be_uploaded_to_this_request), response.getRebackcontent());
                        toastLong(tip);
                        resetFreeUser();

                    } else {
                        // 提示等待对方同意或者不同意
                        Logs.t(TAG).ii("inquiry success");
                        toastLong(R.string.freesharing_the_request_has_been_sent);
                    }

                } else {
                    // 对方服务器出错--> 提示连接失败重连
                    Logs.t(TAG).ee("inquiry failed , server had problem");
                    toastLong(R.string.freesharing_the_connection_failed_please_try_again);
                    // 重置用户组列表
                    clickDone();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // 对方不在线
                Logs.t(TAG).ee("inquiry failed , server had not online");
                toastLong(R.string.freesharing_the_connection_failed_please_try_again);
                // 重置用户组列表
                clickDone();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                // 连接被取消
                Logs.t(TAG).ee("inquiry failed , server had not online");
                toastLong(R.string.freesharing_the_connection_is_cancelled_please_try_again);
                // 重置用户组列表
                clickDone();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {

        // 1.读取图库
        AbstructProvider imageProvider = new ImageProvider(this);
        images_resource = (List<Freesharing_Image>) imageProvider.getList();
        Collections.sort(images_resource, new CompareHelper<>());
        // 判断图库是否有图片
        if (images_resource == null | images_resource.size() == 0) {
            // 显示无数据UI
            showNoViewUi(TAB_IMAGES);
        } else {
            // 根据计算出来的count进行集合过滤显示
            imageThread = threadLoadMore(imageThread, images_resource, 0, maxCount);
        }

        // 2.读取媒体库
        AbstructProvider videoProvider = new VideoProvider(this);
        videos_resource = (List<Freesharing_Video>) videoProvider.getList();
        Collections.sort(videos_resource, new CompareHelper<>());
        if (videos_resource == null | videos_resource.size() == 0) {
            // 显示无数据UI
            showNoViewUi(TAB_VIDEO);
        } else {
            // 根据计算出来的count进行集合过滤显示
            videoThread = threadLoadMore(videoThread, videos_resource, 0, maxCount);
        }

        // 3.读取传输历史列表
        readTransHistory();
    }

    /**
     * 显示
     */
    private void readTransHistory() {
        // 从储存文件夹中读取信息
        if (FreeCacheUtils.readCacheProperty() != null) {
            freesharingFreeTranslistList.addAll(FreeCacheUtils.readCacheProperty());
            freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
        }
    }

    /**
     * 初始化UI
     */
    private void initUi() {
        isShow(TAB_IMAGES);// 默认显示图片列表
    }

    /**
     * 初始化滑动事件
     */

    private void initEvent() {

        // 为image添加监听器
        rcvFreeImageItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                boolean isBottom = OtherUtils.isVisBottom(rcvFreeImageItems);// 1.已经到底了
                boolean isIdle = RecyclerView.SCROLL_STATE_IDLE == newState;// 2.滚动停止
                boolean isFinish = !isImageLoading;// 3.数据加载完成
                if (isBottom & isIdle & isFinish) {
                    // 4.子线程进行加载
                    threadLoadMore(imageThread, images_resource, freesharingFreeImages.size(), maxCount);
                }
            }
        });

        // 为video添加监听器
        rcvFreeVideoitems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                boolean isBottom = OtherUtils.isVisBottom(rcvFreeVideoitems);// 1.已经到底了
                boolean isIdle = RecyclerView.SCROLL_STATE_IDLE == newState;// 2.滚动停止
                boolean isFinish = !isVideoLoading;// 3.数据加载完成
                if (isBottom & isIdle & isFinish) {
                    threadLoadMore(videoThread, videos_resource, freesharingFreeVideos.size(), maxCount);
                }
            }
        });

    }

    /**
     * 初始化服务器
     */
    private void initServer() {
        EventBus.getDefault().register(this);
    }

    /**
     * 点击了done
     */
    private void clickDone() {

        // 0.启动等待动画
        cvFreeUser.startAnimations(2000, getString(R.string.freesharing_scanning_online_users));
        // 1.清空集合等重置操作
        freesharingFreeUsers.clear();
        temps.clear();
        isScanUser = true;
        rlFreeChoiceUser.setVisibility(View.GONE);
        saveUserState(rlFreeChoiceUser.getVisibility());
        if (handler != null) {
            handler.postDelayed(this::doneLogic, 500);
        } else {
            doneLogic();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getInteractiveResponce(InteractiveResponceBean responceBean) {
        handlerDeviceList(responceBean);
    }

    /**
     * 执行done的逻辑
     */
    private void doneLogic() {
        // 发送给外部
        EventBus.getDefault().post(new InteractiveRequestBean(InteractiveRequestBean.REQ));
    }

    private void handlerDeviceList(InteractiveResponceBean responceBean) {
        // TODO: 2019/7/24 0024 此处通过eventbus从外部接收进来 
        // 1.1.请求硬件获取已经连接的Devices
        DeviceHelper deviceHelper = new DeviceHelper(responceBean);
        deviceHelper.setOnGetDevicesSuccessListener(connectedList -> {
            Logs.t(TAG).ii(selectImages.size() + selectVideos.size() + "");
            // 1.2.获取本机IP
            String hostAddress = NetUtils.getLocalIPAddress().getHostAddress();
            Logs.t(TAG).ii("Hose ip_phone: " + hostAddress);
            // 1.2.1. 循环发起请求
            for (ConnectedList.Device device : connectedList.getConnectedList()) {
                // 1.3.从设备获取到已连IP
                String ipFromRouter = device.getIPAddress();
                Logs.t(TAG).ii("devices ip_phone: " + ipFromRouter);
                // 1.4.排除本机IP
                if (!ipFromRouter.equalsIgnoreCase(hostAddress)) {
                    new Thread(() -> getServerPhoneName(ipFromRouter)).start();
                }
            }
            Logs.t(TAG).ii("End of request");

            // 1.5.启动定时器轮询检测回归的用户数
            requestTimer = new TimerHelper(this) {
                @Override
                public void doSomething() {
                    runOnUiThread(() -> {
                        if (temps.size() == connectedList.getConnectedList().size() - 1) {
                            isScanUser = false;
                            if (requestTimer != null) {
                                requestTimer.stop();
                            }
                            Logs.t(TAG).ii("break out xutils request");
                            // 1.6.整理转换为freeUsers用户组
                            transferResponce();

                            // 1.7.如有手机连接上路由器--> 显示用户组面板--> 更新用户列表
                            if (freesharingFreeUsers.size() > 0) {
                                Logs.t(TAG).ii("freeuser size: " + freesharingFreeUsers.size());
                                rlFreeChoiceUser.setVisibility(View.VISIBLE);
                                saveUserState(rlFreeChoiceUser.getVisibility());
                                tvFreeDone.setVisibility(View.GONE);
                                freeChoiceUserAdapter.notifys(freesharingFreeUsers);
                            } else {
                                Logs.t(TAG).ii("no match accordinary freeusers");
                                // 1.8.显示提示框(2秒后消失)
                                rlFreeNoDevice.clearAnimation();// 重置动画
                                rlFreeNoDevice.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(() -> {
                                    AlphaAnimation al = new AlphaAnimation(1, 0);
                                    al.setDuration(3000);
                                    al.setFillAfter(true);
                                    al.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            rlFreeNoDevice.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    rlFreeNoDevice.setAnimation(al);
                                    rlFreeNoDevice.startAnimation(al);
                                }, 2000);
                            }
                            // 等待动画消失
                            setConnUserGone();
                        }
                    });
                }
            };
            requestTimer.start(200);
        });
        deviceHelper.setOnGetDevicesResultErrorListener(error -> {
            setConnUserGone();// 等待动画消失
            toast(R.string.freesharing_connect_failed);
        });
        deviceHelper.setOnGetDevicesErrorListener(throwable -> {
            setConnUserGone();// 等待动画消失
            toast(R.string.freesharing_connect_failed);
        });
        deviceHelper.getDevices();
    }

    /**
     * 把请求得到的数据转换为freeuser
     */
    private void transferResponce() {
        Logs.t(TAG).ii("transferResponce");
        for (Object temp : temps) {
            if (temp instanceof IMResponse) {
                IMResponse response = (IMResponse) temp;
                Freesharing_FreeUser user = new Freesharing_FreeUser();
                user.setIp(response.getIp());
                user.setPhoneName(response.getPhonename());
                boolean aTrue = response.getRebackcontent().equalsIgnoreCase("true");
                user.setConnecting(aTrue);
                user.setCanClick(true);
                freesharingFreeUsers.add(user);
            }
        }
    }

    /**
     * 根据ip请求获取对方手机名称
     *
     * @param ip 对方的ip
     */
    private void getServerPhoneName(String ip) {
        Logs.t(TAG).ii("ready to request ip_phone: " + ip);
        String url = "http://" + ip + ":" + FreeShareService.port + "/" + FreeShareService.im_intercept;
        String jsonForPhonename = getIMBody(null, NetUtils.getLocalIPAddress().getHostAddress(), IBaseHandler.TYPE_PHONE);
        RequestParams entity = RequestParamHelper.getEntity(url, jsonForPhonename);// 得到实体
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void responseBody(UriRequest uriRequest) {

            }

            @Override
            public void onSuccess(String result) {
                Logs.t(TAG).ii(result);
                // 添加到临时集合
                IMResponse response = JSONObject.parseObject(result, IMResponse.class);
                temps.add(response.getReback() == IBaseHandler.STATE_200 ? response : false);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logs.t(TAG).ee(ex.getMessage());
                temps.add(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logs.t(TAG).ee(cex.getMessage());
            }

            @Override
            public void onFinished() {
                Logs.t(TAG).ii("Finish");
            }
        });
    }

    @OnClick({R2.id.iv_free_back,// 返回
            R2.id.tv_free_done,// 点击传输
            R2.id.rl_free_images_tag,// 图片tab
            R2.id.rl_free_videos_tag,// 视频tab
            R2.id.rl_free_transferList_tag, // 传输列表tab
            R2.id.iv_free_detailImage,// 详情大图
            R2.id.iv_free_detailImage_back,// 详情大图返回键
            R2.id.rl_free_transferList_dialog,// 请求对话框 
            R2.id.iv_free_choice_user, // 用户组防触碰底版
            R2.id.rl_free_choice_user_hide,// 隐藏用户组
            R2.id.rl_free_noDevice,// 无设备提示面板
            R2.id.tv_free_clearHistory// 清空历史记录
    })
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_free_back) {// free sharing返回键
            finish();

        } else if (id == R.id.tv_free_clearHistory) {// 清空历史(传输中: 提示不能清除)
            clearHistory();

        } else if (id == R.id.rl_free_images_tag) {// image tag
            clickProcess(TAB_IMAGES);

        } else if (id == R.id.rl_free_videos_tag) {// video tag
            clickProcess(TAB_VIDEO);

        } else if (id == R.id.rl_free_transferList_tag) {// transfer tag
            clickProcess(TAB_TRANSFER);

        } else if (id == R.id.iv_free_detailImage) {// 详情大图(点击后收起banner)
            boolean isDetailBannerVisible = rlFreeDetailImageBanner.getVisibility() == View.VISIBLE;
            rlFreeDetailImageBanner.setVisibility(isDetailBannerVisible ? View.GONE : View.VISIBLE);

        } else if (id == R.id.iv_free_detailImage_back) {// 详情大图返回键(点击后详情页隐藏)
            rlFreeDetailImage.setVisibility(View.GONE);

        } else if (id == R.id.tv_free_done) {// 点击Done
            clickDone();

        } else if (id == R.id.rl_free_transferList_dialog) {// 请求对话框
            toast(R.string.freesharing_please_choose_to_receice_or_reject);

        } else if (id == R.id.rl_free_choice_user_hide) {// 收起用户组面板
            if (rlFreeChoiceUser.getVisibility() == View.VISIBLE) {
                clearFreeUsers();
            }
        } else if (id == R.id.iv_free_choice_user) {// 用户组防误碰面板
            Logs.t("matouch").ii("The user had touch");

        } else if (id == R.id.rl_free_noDevice) {// 无设备提示面板
            Logs.t("matouch").ii("click no devices panel");

        }

    }

    /**
     * 清空历史(传输中: 提示不能清除)
     */
    private void clearHistory() {
        // 1.查询是否有正在传输的文件(判断state)
        boolean isTransation = false;
        for (Freesharing_FreeTranslist ta : freesharingFreeTranslistList) {
            if (ta.getState() == IBaseHandler.TRANSLIST_STATE_UPLOADING) {
                isTransation = true;
                break;
            }
        }

        // 2.如果有这提示
        if (isTransation) {
            toast(R.string.freesharing_the_file_is_being_sent);
        } else {
            // 2.1.先清空缓存文件夹
            for (File file : cacheDir.listFiles()) {
                boolean delete = file.delete();
                Logs.t(TAG).ii("delete file: " + file + "=" + delete);
            }
            // 2.2.在重新建立smarthistory.txt文件
            File historyTxt = new File(cacheDir, FILE_CACHE_HISTORY);
            try {
                boolean creatFile = historyTxt.createNewFile();
            } catch (Exception e) {
                Logs.t(TAG).ee("re-create history txt failed: \n" + e.getMessage());
                e.printStackTrace();
            }
            // 2.3.刷新传输列表
            freesharingFreeTranslistList.clear();
            freeTranslistAdapter.notifiys(freesharingFreeTranslistList);
            // 2.4.隐藏[清空历史]面板
            tvFreeClearHistory.setVisibility(View.GONE);
            // 2.5.清空集合

        }

    }

    /* -------------------------------------------- method -------------------------------------------- */

    /**
     * 移除请求组 + 对话框消失(集合已经没有元素)
     */
    private void cancelDialog(IMRequest request) {
        // 1.移除对应的请求组
        int dialogSize = freeTranslistDialogList.size();
        for (int i = 0; i < dialogSize; i++) {
            if (freeTranslistDialogList.get(i).getId().equalsIgnoreCase(request.getId())) {
                freeTranslistDialogList.remove(i);
                freeTranslistDialogAdapter.notifys(freeTranslistDialogList);
                break;
            }
        }
        // 2.如果集合已经没有数据, 则隐藏布局
        if (freeTranslistDialogList.size() == 0) {
            rlFreeTransferListDialog.setVisibility(View.GONE);
        }
    }

    /**
     * 根据标记显示无数据的UI
     */
    private void showNoViewUi(int tag) {
        rlFreeNoImage.setVisibility(tag == TAB_IMAGES ? View.VISIBLE : View.GONE);
        rlFreeNoVideo.setVisibility(tag == TAB_VIDEO ? View.VISIBLE : View.GONE);
        rlFreeNoTransferList.setVisibility(tag == TAB_TRANSFER & // 位于传输列表
                                                   FreeCacheUtils.readCacheProperty().size() <= 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 点击各个tab后的逻辑
     *
     * @param index 索引
     */
    private void clickProcess(int index) {
        // 1.显示下划线
        showUnderLine(index);
        // 2.读取对应的列表
        isShow(index);
        // 3.根据标签决定是否显示「clear history」面板
        isShowClearHistoryPanel(index);
    }

    /**
     * 显示「清除历史」面板
     */
    private void isShowClearHistoryPanel(int index) {
        switch (index) {
            case TAB_IMAGES:
            case TAB_VIDEO:
                tvFreeClearHistory.setVisibility(View.GONE);
                break;
            case TAB_TRANSFER:
                boolean isTranslistHadData = freesharingFreeTranslistList != null & freesharingFreeTranslistList.size() > 0;
                tvFreeClearHistory.setVisibility(isTranslistHadData ? View.VISIBLE : View.GONE);
                break;
        }
    }

    /**
     * 显示下划线
     *
     * @param index 索引
     */
    private void showUnderLine(int index) {
        for (int i = 0; i < underLines.length; i++) {
            underLines[i].setVisibility(i == index ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 是否显示image列表
     */
    private void isShow(int tab) {
        rlFreeImage.setVisibility(tab == TAB_IMAGES ? View.VISIBLE : View.GONE);
        rlFreeVideo.setVisibility(tab == TAB_VIDEO ? View.VISIBLE : View.GONE);
        rlFreeTransferList.setVisibility(tab == TAB_TRANSFER ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示图片或者视频
     *
     * @param datas 数据源
     * @param begin 开始索引
     * @param limit 显示个数
     */
    private void showImageOrVideo(List<?> datas, int begin, int limit) {
        // 1.把标记位暂时设置为true,表示数据正在加载
        if (datas.get(0) instanceof Freesharing_Image) {
            isImageLoading = true;
        } else if (datas.get(0) instanceof Freesharing_Video) {
            isVideoLoading = true;
        }

        // 1.1.判断循环的数量是否超过限制
        if (datas.get(0) instanceof Freesharing_Image) {
            limit = datas.size() - freesharingFreeImages.size() >= limit ? limit : datas.size() - freesharingFreeImages.size();
        } else if (datas.get(0) instanceof Freesharing_Video) {
            limit = datas.size() - freesharingFreeVideos.size() >= limit ? limit : datas.size() - freesharingFreeVideos.size();
        }
        // 2.开始封装并显示数据
        for (int i = begin; i < begin + limit; i++) {
            if (datas.get(i) instanceof Freesharing_Image) {// 图片处理格式
                Freesharing_Image image = (Freesharing_Image) datas.get(i);
                // Log.i("ma_data", "mimetype: " + image.getMimeType());
                // Log.i("ma_data", "path: " + image.getPath());
                if (image.getPath().contains("/storage/emulated/0/DCIM")) {// 检查是否位于图库中(其他文件夹的不显示)
                    Freesharing_FreeImage freesharingFreeImage = FreeFilter.getImageFilterSingle(this, image);
                    runOnUiThread(() -> {
                        if (freesharingFreeImage != null) {
                            freesharingFreeImages.add(freesharingFreeImage);
                            freeImageAdapter.notifys(freesharingFreeImages);
                        }
                    });
                }
            } else if (datas.get(i) instanceof Freesharing_Video) {// 视频处理格式
                Freesharing_FreeVideo freesharingFreeVideo = FreeFilter.getVideoFilterSingle(this, (Freesharing_Video) datas.get(i));
                runOnUiThread(() -> {
                    if (freesharingFreeVideo != null) {
                        freesharingFreeVideos.add(freesharingFreeVideo);
                        freeVideoAdapter.notifys(freesharingFreeVideos);
                    }
                });
            }
        }
        // 3.恢复标记位
        if (datas.get(0) instanceof Freesharing_Image) {
            isImageLoading = false;
        } else if (datas.get(0) instanceof Freesharing_Video) {
            isVideoLoading = false;
        }
    }

    /**
     * 子线程加载更多
     *
     * @param thread 子线程
     * @param datas  数据源
     * @param begin  开始索引
     * @param limit  加载数量
     * @return 返回子线程
     */
    private Thread threadLoadMore(Thread thread, List<?> datas, int begin, int limit) {
        thread = new Thread(() -> {
            showImageOrVideo(datas, begin, limit);
        });
        thread.start();
        return thread;
    }

    /**
     * 处理被选中的
     */
    private void selectChoice(boolean isSelected, Object object) {

        // 2.类型区分
        if (object instanceof Freesharing_FreeImage) {
            Freesharing_FreeImage freesharingFreeImage = (Freesharing_FreeImage) object;
            if (isSelected) {// 如果被选中
                if (!selectImages.contains(freesharingFreeImage)) {// 且集合中没有包含
                    selectImages.add(freesharingFreeImage);// 则添加
                }
            } else {// 如果被取消
                // 且集合中包含
                selectImages.remove(freesharingFreeImage);// 则移除
            }
        } else if (object instanceof Freesharing_FreeVideo) {
            Freesharing_FreeVideo freesharingFreeVideo = (Freesharing_FreeVideo) object;
            if (isSelected) {// 如果被选中
                if (!selectVideos.contains(freesharingFreeVideo)) {// 且集合中没有包含
                    selectVideos.add(freesharingFreeVideo);// 则添加
                }
            } else {// 如果被取消
                // 且集合中包含
                selectVideos.remove(freesharingFreeVideo);// 则移除
            }
        }

    }

    /**
     * 显示标题 & 显示done提交按钮
     */
    private void showTitleAndDoneButton() {
        // 切换标题以及数量
        int allSelected = selectImages.size() + selectVideos.size();
        String title = allSelected == 0 ? freeSharingTitle : freeSharingTitle + "(" + allSelected + ")";
        tvFreeTitle.setText(title);
        // 显示done按钮与否
        tvFreeDone.setVisibility(allSelected == 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 清空用户组
     */
    private void clearFreeUsers() {

        if (!isScanUser) {
            // 扫描用户完毕, 清空用户组
            rlFreeChoiceUser.setVisibility(View.GONE);
            saveUserState(rlFreeChoiceUser.getVisibility());
            tvFreeDone.setVisibility(View.VISIBLE);
            freesharingFreeUsers.clear();
            freeChoiceUserAdapter.notifys(freesharingFreeUsers);
        } else {
            // 如果线程正在扫描用户, 则提示稍候
            toast(R.string.freesharing_please_wait_a_moment_to_scan);
        }
    }

    /**
     * 等待动画停止
     */
    private void setConnUserGone() {
        cvFreeUser.setGone();
        cvFreeUser.setVisibility(View.GONE);
    }


    /**
     * 封装请求元素
     */
    private String getIMBody(IMRequest request, String ip, int type) {

        IMRequest req = new IMRequest();
        req.setId(OtherUtils.getUUid());// session id
        req.setIp(ip);// ip_phone
        req.setPhonename(Build.BRAND);// TCL
        req.setType(type);// 0,1,2...
        // req.setPathList(getAllChoicePath());// [a.png, b.png, c.png ...]
        req.setPathList(new ArrayList<>());// [a.png, b.png, c.png ...]
        req.setAttach(request == null ? REQUEST_DIALOG : request.getAttach());
        return JSONObject.toJSONString(req);
    }

    /**
     * 封装请求元素(用于请求dialog)
     */
    private String getDialogImBody(IMRequest request, String ip, int type) {

        // 整合已选的文件
        ArrayList<String> selects = new ArrayList<>();
        for (Freesharing_FreeImage freesharingFreeImage : selectImages) {
            selects.add(freesharingFreeImage.getPath());
        }
        for (Freesharing_FreeVideo freesharingFreeVideo : selectVideos) {
            selects.add(freesharingFreeVideo.getPath());
        }

        IMRequest req = new IMRequest();
        req.setId(OtherUtils.getUUid());// session id
        req.setIp(ip);// ip_phone
        req.setPhonename(Build.BRAND);// TCL
        req.setType(type);// 0,1,2...
        // req.setPathList(getAllChoicePath());// [a.png, b.png, c.png ...]
        req.setPathList(selects);// [a.png, b.png, c.png ...]
        req.setAttach(request == null ? REQUEST_DIALOG : request.getAttach());
        return JSONObject.toJSONString(req);
    }

    /**
     * 获取中断的请求json
     *
     * @param localIp        本地IP
     * @param type           请求类型
     * @param interruptFiles 需要中断的文件集合
     * @return json
     */
    private String getInterruptImBody(String localIp, int type, List<String> interruptFiles) {
        IMRequest req = new IMRequest();
        req.setId(OtherUtils.getUUid());// session id
        req.setIp(localIp);// ip
        req.setPhonename(Build.BRAND);// TCL
        req.setType(type);// 0,1,2...
        req.setPathList(interruptFiles);// 待重试的文件集合
        req.setAttach(REQUEST_INTERRUPT);
        return JSONObject.toJSONString(req);
    }

    /**
     * 获取已选的文件路径集合
     */
    private List<String> getAllChoicePath() {
        ArrayList<String> filenames = new ArrayList<>();
        for (Freesharing_FreeImage selectImage : selectImages) {
            filenames.add(selectImage.getPath());
        }
        for (Freesharing_FreeVideo selectVideo : selectVideos) {
            filenames.add(selectVideo.getPath());
        }
        return filenames;
    }

    /**
     * 存放用户面板收缩|扩展状态
     *
     * @param isVisible 1:
     */
    public void saveUserState(int isVisible) {
        SPUtils.getInstance(this).put(IMHandler.user_State, isVisible);
    }


    /**
     * 重置用户组数据
     */
    private void resetFreeUser() {
        for (Freesharing_FreeUser freesharingFreeUser : freesharingFreeUsers) {
            freesharingFreeUser.setCanClick(true);
            freesharingFreeUser.setConnecting(false);
        }
        freeChoiceUserAdapter.notifys(freesharingFreeUsers);
    }

    /**
     * 是否为首次请求
     *
     * @return -1:没有请求过
     */
    private int isLastRequest() {
        int index = -1;// 默认:-1
        for (int i = 0; i < freesharingFreeUsers.size(); i++) {
            // 有任何一个在连接则break
            if (freesharingFreeUsers.get(i).isConnecting()) {
                index = i;// 定位是哪个user
                break;
            }
        }
        return index;
    }

    /**
     * 是否为图片
     */
    private int getFileType(String filepath) {
        // 是否为图片
        for (String exe1 : IBaseHandler.TRANSLIST_FILE_PIC) {
            if (filepath.endsWith(exe1)) {
                return TRANSLIST_FILETYPE_PIC;
            }
        }
        // 是否为视频
        for (String exe2 : IBaseHandler.TRANSLIST_FILE_VIDEO) {
            if (filepath.endsWith(exe2)) {
                return IBaseHandler.TRANSLIST_FILETYPE_VIDEO;
            }
        }
        // 否则为未知
        return IBaseHandler.TRANSLIST_FILETYPE_UNKNOWN;
    }

    /**
     * 当前时间 + 原文件后缀
     */
    private String getNewFileName(String oldName) {
        return OtherUtils.getCurrentDateForString() + OtherUtils.getExe(oldName);
    }


    public void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    public void toastLong(int resId) {
        ToastUtil_m.showLong(this, resId);
    }

    public void toastLong(int resId, int duration) {
        ToastUtil_m.showLong(this, resId);
    }

    public void toastLong(String text) {
        ToastUtil_m.showLong(this, text);
    }

    public void toast(String content) {
        ToastUtil_m.show(this, content);
    }

    public void to(Class ac, boolean isFinish) {
        CA.toActivity(this, ac, false, isFinish, false, 0);
    }
}
