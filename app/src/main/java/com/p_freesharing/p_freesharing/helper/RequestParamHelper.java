package com.p_freesharing.p_freesharing.helper;

import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;

/**
 * Created by qianli.ma on 2018/4/12 0012.
 */

public class RequestParamHelper {

    /**
     * 获取请求实体entity
     *
     * @param url
     * @param json
     * @return
     */
    public static RequestParams getEntity(String url, String json) {
        RequestParams entity = new RequestParams(url);
        entity.setConnectTimeout(15 * 1000);
        entity.setCancelFast(true);
        // entity.addHeader(HTTP.CONN_DIRECTIVE,HTTP.CONN_CLOSE);// 短连接
        entity.setExecutor(new PriorityExecutor(9,true));
        entity.addHeader("Accept", "application/json");
        entity.addHeader("Content-Type", "application/json");
        entity.setAsJsonContent(true);
        entity.setCacheMaxAge(0);
        entity.setBodyContent(json);
        return entity;
    }
}
