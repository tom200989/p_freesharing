package com.p_freesharing.p_freesharing.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.p_freesharing.p_freesharing.core.server.core.NetUtils;
import com.p_freesharing.p_freesharing.core.server.handler.IFileHandler;
import com.p_freesharing.p_freesharing.core.server.handler.IMHandler;
import com.p_freesharing.p_freesharing.utils.Logs;
import com.yanzhenjie.andserver.Corebuilder;
import com.yanzhenjie.andserver.Server;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class FreeShareService extends Service {

    private String TAG = "freeshareservice";// 日志标记
    public static int port = 6688;// 端口
    public static String im_intercept = "IM";// 消息servlet
    public static String if_intercept = "IF";// 文件servlet
    private Server mServer;// 服务器
    private Thread thread;

    public FreeShareService() {
        Logs.t(TAG).ii("FreeShareService had create new");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thread == null) {
            thread = new Thread(this::startServer);
        }
        thread.start();
        // return START_STICKY_COMPATIBILITY;
        return START_NOT_STICKY;
    }

    // 启动服务器
    private void startServer() {
        try {
            Logs.t(TAG).ii("0. prepare to start AndServer");
            if (mServer == null || !mServer.isRunning()) {// 如果服务器没有运行--> 创建并运行
                // 创建并启动Andserver Builder
                Logs.t(TAG).ii("1. create builder");
                Server.Builder builder = Corebuilder.getServerBuilder();
                InetAddress localIPAddress = NetUtils.getLocalIPAddress();
                String hostAddress = localIPAddress.getHostAddress();
                Logs.t(TAG).ii("1.1. hostAddress: " + hostAddress);
                builder.inetAddress(NetUtils.getLocalIPAddress());
                builder.port(port);
                builder.timeout(60, TimeUnit.SECONDS);
                // 从这里添加多个请求的站点(builder.registerHandler)
                builder.registerHandler("/" + im_intercept, new IMHandler());// 消息
                builder.registerHandler("/" + if_intercept, new IFileHandler(getApplicationContext()));// 文件
                mServer = builder.build();
                mServer.startup();
                Logs.t(TAG).ii("2. start server");
            } else {
                Logs.t(TAG).ii("2. The server is running");
            }
        } catch (Exception e) {
            Logs.t(TAG).ee("UnknownHostException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
