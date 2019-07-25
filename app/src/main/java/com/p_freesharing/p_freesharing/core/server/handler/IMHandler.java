package com.p_freesharing.p_freesharing.core.server.handler;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.p_freesharing.p_freesharing.core.server.bean.IMRequest;
import com.p_freesharing.p_freesharing.core.server.bean.IMResponse;
import com.p_freesharing.p_freesharing.core.server.core.NetUtils;
import com.p_freesharing.p_freesharing.utils.Logs;
import com.p_freesharing.p_freesharing.utils.SPUtils;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import upload.corehttp.org.apache.http.HttpRequest;
import upload.corehttp.org.apache.http.HttpResponse;
import upload.corehttp.org.apache.http.entity.StringEntity;
import upload.corehttp.org.apache.http.protocol.HttpContext;

import static com.p_freesharing.p_freesharing.core.server.handler.IFileHandler.uploadingFileSet;


public class IMHandler extends IBaseHandler implements RequestHandler {

    private String TAG = "imhandler";
    public static final String user_State = "USER_STATE";// 存放到sp的用户组面板收缩|扩展情况
    public static HashSet<String> currentIps = new HashSet<>();

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) {

        Logs.t(TAG).ii("1. The handler had receive the request");
        try {
            // 1.解析请求内容
            String json = HttpRequestParser.getContent(request);
            // 这一句不能和上面那一句同时使用, 否则报错
            // Map<String, String> params = HttpRequestParser.parseParams(request);
            Logs.t(TAG).ii("2. parse data result: \n" + json);

            // 2.转换请求对象
            IMRequest imRequest = JSONObject.parseObject(json, IMRequest.class);

            // 2.0.接收到手机信息请求(优先处理)
            if (imRequest.getType() == TYPE_PHONE) {
                String phoneNameJson = getIMResponcePhoneNameJson(imRequest);
                response.setEntity(new StringEntity(phoneNameJson));
                Logs.t(TAG).ii("3.get phone info");
                return;

                // 请求取消(优先处理)
            } else if (imRequest.getType() == TYPE_CANCEL) {
                Logs.t(TAG).ii("3.client had click cancel");
                currentIps.remove(imRequest.getIp());
                response.setEntity(new StringEntity(getCancelLastResponce(imRequest)));

                // 收到中断传输指令
            } else if (imRequest.getType() == TYPE_CLIENT_INTERRUPT) {
                // 192.168.3.101#smartlink#aaa.mp4
                String ip_filename = imRequest.getIp() + FILE_SPLIT + imRequest.getPathList().get(0);
                Logs.t(TAG).ii("3.receiver the interrupt ip is : " + ip_filename);
                Logs.t("tongyi").ii("3.receiver the interrupt ip is : " + ip_filename);
                if (uploadingFileSet.contains(ip_filename)) {
                    uploadingFileSet.remove(ip_filename);
                    currentIps.remove(imRequest.getIp());
                }
                response.setEntity(new StringEntity(getInterruptResponce(imRequest)));

                // 2.0.上次请求没有回复--> 则不允许再次请求
            } else if (currentIps.contains(imRequest.getIp())) {
                Logs.t(TAG).ii("3.ip_phone had repeat");
                response.setEntity(new StringEntity(getNoReplyResponce(imRequest)));
                return;

                // 2.1.判断是否超出了最大连接手机设备数量(最多允许两部,单个手机最大只允许发送9个,否则发生OOM)
            } else if (isMaxPhoneCount(imRequest)) {// 超出最大手机请求数--> 直接不允许上传
                Logs.t(TAG).ii("3.ip_phone had reach max count");
                response.setEntity(new StringEntity(getMaxPhoneResponce(imRequest)));
                return;

                // 重复ip可传的数量为0--> 不允许上传
            } else if (remainSameIpCount(imRequest) == 0) {
                Logs.t(TAG).ii("3.same ip_phone had no contain count");
                response.setEntity(new StringEntity(getNoRemainCountResponce(imRequest)));
                return;

                // 剩余可上传线路数量 「小于」 当前请求的文件数--> 不允许上传
            } else if (remainSameIpCount(imRequest) < imRequest.getPathList().size()) {
                Logs.t(TAG).ii("3.same ip_phone had less contain count");
                response.setEntity(new StringEntity(getLessRemainCountResponce(imRequest, remainSameIpCount(imRequest))));
                return;
            }

            // 2.2.传递到UI层 (这里使用Post,不可使用PostStick, 否则会导致每次注册重复传递的问题)
            EventBus.getDefault().post(imRequest);
            Logs.t(TAG).ii("3. transfer request success");
            Logs.t(TAG).ii("3. transfer request imrequest url size: " + imRequest.getPathList().size());

            // 3.分类处理
            int type = imRequest.getType();
            if (type == TYPE_DIALOG) {/* 接收到会话请求 */
                if (!isExitIp(imRequest.getIp())) {
                    currentIps.add(imRequest.getIp());// 添加到ip集合
                }
                String dialogJson = getIMResponceDialogJson(imRequest);
                response.setEntity(new StringEntity(dialogJson));
                Logs.t(TAG).ii("4. handler dialog type\n" + dialogJson);

            } else if (type == TYPE_OK) {/* 接收到OK请求 */
                // TOAT: 此处不管对方的面板是否有收起, 均可传输(plan B)
                Logs.t(TAG).ii("4. handler session recevier ok: " + imRequest.getAttach());
                String sessionPreStartJson = getIMResponcePreStart(imRequest);
                response.setEntity(new StringEntity(sessionPreStartJson));
                Logs.t(TAG).ii("4. handler session pre start type\n" + sessionPreStartJson);

            } else if (type == TYPE_NOK) {/* 接收到NOK请求 */
                // 从集合中剔除
                if (currentIps.contains(imRequest.getIp())) {
                    Logs.t(TAG).ii("4.had remove ip_phone: " + imRequest.getIp());
                    currentIps.remove(imRequest.getIp());
                }
                // 回复会话没有取消
                String sessionPreStopJson = getIMResponcePreStop(imRequest);
                response.setEntity(new StringEntity(sessionPreStopJson));
                Logs.t(TAG).ii("4. handler session pre stop type\n" + sessionPreStopJson);
            }
        } catch (Exception e) {
            Logs.t(TAG).ee("4. server error: " + e.getMessage());
            try {
                String imResponceError = getIMResponceError(e);
                response.setEntity(new StringEntity(imResponceError));
                Logs.t(TAG).ee("4. server error: \n" + imResponceError);
            } catch (Exception e1) {
                Logs.t(TAG).ee("4. server error: " + e1.getMessage());
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * 是否已有指定IP
     * @param ip
     * @return
     */
    private boolean isExitIp(String ip) {
        for (String currentIp : currentIps) {
            if (currentIp.contains(ip) | currentIp.equalsIgnoreCase(ip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 服务器弹出对话框--> 反馈客户端
     *
     * @param imRequest
     * @return
     */
    private String getIMResponceDialogJson(IMRequest imRequest) {
        IMResponse imResponse = new IMResponse();
        imResponse.setId(imRequest.getId());
        imResponse.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        imResponse.setPhonename(Build.BRAND);
        imResponse.setType(imRequest.getType());
        imResponse.setFilename(new ArrayList<String>());
        imResponse.setReback(STATE_200);
        imResponse.setRebackcontent("");
        return JSONObject.toJSONString(imResponse);
    }

    /**
     * 反馈: 获取手机名称等信息内容
     *
     * @param imRequest
     */
    private String getIMResponcePhoneNameJson(IMRequest imRequest) {
        IMResponse response = new IMResponse();
        response.setId(imRequest.getId());
        response.setReback(STATE_200);
        // 获取手机类型时--> 检测上次是否有请求过
        response.setRebackcontent(currentIps.contains(imRequest.getIp()) ? "true" : "false");
        response.setFilename(new ArrayList<>());
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setType(TYPE_PHONE);
        response.setPhonename(Build.BRAND);
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 服务器报错
     *
     * @param e
     */
    private String getIMResponceError(Exception e) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_ERROR);
        response.setFilename(new ArrayList<String>());
        response.setReback(STATE_503);
        response.setRebackcontent(e.getMessage());
        response.setId(SERVER_ERROR_ID);
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 未知
     *
     * @return 未知状态
     */
    private String getIMResponceUnknown() {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_UNKNOWN);
        response.setFilename(new ArrayList<String>());
        response.setReback(STATE_404);
        response.setRebackcontent("");
        response.setId(SERVER_UNKNOWN_ID);
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 会话结束
     *
     * @return
     */
    private String getIMResponceOver() {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_OVER);
        response.setFilename(new ArrayList<String>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(SERVER_OVER_ID);
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 准备发送文件
     *
     * @return
     */
    private String getIMResponcePreStart(IMRequest request) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_PRE_START);
        response.setFilename(request.getPathList());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(request.getId());
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 取消准备发送文件
     *
     * @return
     */
    private String getIMResponcePreStop(IMRequest request) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_PRE_STOP);
        response.setFilename(new ArrayList<String>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(request.getId());
        return JSONObject.toJSONString(response);
    }


    /**
     * 反馈: 最大手机连接数
     *
     * @return
     */
    private String getMaxPhoneResponce(IMRequest imRequest) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_MAX_PHONE);
        response.setFilename(new ArrayList<>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(imRequest.getId());
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 没有剩余手机容量
     *
     * @param imRequest
     * @return
     */
    private String getNoRemainCountResponce(IMRequest imRequest) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_NO_FILE_REMAINED);
        response.setFilename(new ArrayList<>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(imRequest.getId());
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 单个IP没有剩余的传输线路
     *
     * @param imRequest
     * @param remainCount 本次请求剩余可传
     * @return
     */
    private String getLessRemainCountResponce(IMRequest imRequest, int remainCount) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_LESS_FILE_REMAINED);
        response.setFilename(new ArrayList<>());
        response.setReback(STATE_200);
        response.setRebackcontent(String.valueOf(remainCount));
        response.setId(imRequest.getId());
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 上次请求没有回复
     *
     * @param imRequest
     * @return
     */
    private String getNoReplyResponce(IMRequest imRequest) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_NO_REPLY);
        response.setFilename(new ArrayList<String>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(imRequest.getId());
        return JSONObject.toJSONString(response);
    }

    /**
     * 反馈: 中断传输
     *
     * @param imRequest
     * @return
     */
    private String getInterruptResponce(IMRequest imRequest) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_CLIENT_INTERRUPT);
        response.setFilename(new ArrayList<>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(imRequest.getId());
        return JSONObject.toJSONString(response);
    }


    /**
     * 反馈: 取消上次请求
     *
     * @param imRequest
     * @return
     */
    private String getCancelLastResponce(IMRequest imRequest) {
        IMResponse response = new IMResponse();
        response.setIp(NetUtils.getLocalIPAddress().getHostAddress());
        response.setPhonename(Build.BRAND);
        response.setType(TYPE_CANCEL);
        response.setFilename(new ArrayList<>());
        response.setReback(STATE_200);
        response.setRebackcontent("");
        response.setId(imRequest.getId());
        return JSONObject.toJSONString(response);
    }

    /**
     * 获取临时存储的用户面板收缩|扩展状态
     *
     * @return
     */
    private int getRlUserPanelState(Context context) {
        return SPUtils.getInstance(context).getInt(user_State);
    }

    /**
     * 是否达到最大设备连接数量
     *
     * @param imrequest
     * @return false:没有达到; true:达到
     * @throws Exception
     */
    private boolean isMaxPhoneCount(IMRequest imrequest) {
        boolean isMax = false;
        List<String> ips = new ArrayList<>();
        // 检测是否已经有两部手机在传
        for (String ipFileName : uploadingFileSet) {
            String[] split = ipFileName.split(FILE_SPLIT);// ip_phone + filename
            String ip = split[0];
            String filename = split[1];
            if (!ips.contains(ip)) {
                ips.add(ip);
            }
        }

        // 如果连接数大于2部手机
        isMax = ips.size() > MAX_PHONE_COUNT;

        return isMax;
    }

    /**
     * 获取已有IP当前正在传输的个数
     *
     * @param request
     * @return
     */
    private int remainSameIpCount(IMRequest request) {
        // -1.初始化计数位
        int count = 0;
        // 0.获取到当前请求的ip
        String currentIp = request.getIp();
        // 1.遍历正在上传文件的集合
        List<String> ips = new ArrayList<>();
        for (String ipFilename : uploadingFileSet) {
            String[] split = ipFilename.split(FILE_SPLIT);
            String ip = split[0];
            String filename = split[1];
            ips.add(ip);
        }
        // 2.查询集合
        for (String ip : ips) {
            if (ip.contains(currentIp) || ip.contains(currentIp)) {
                count++;
            }
        }
        return MAX_SINGLE_COUNT - count;
    }

}
