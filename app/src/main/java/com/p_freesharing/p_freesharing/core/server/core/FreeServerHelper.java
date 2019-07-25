package com.p_freesharing.p_freesharing.core.server.core;


import com.p_freesharing.p_freesharing.bean.Freesharing_FreeTranslist;

import java.util.List;

/**
 * Created by qianli.ma on 2018/5/10 0010.
 */

public class FreeServerHelper {

    /**
     * 现有的传输集合中是否存在指定对象
     *
     * @param ta   待判断的对象
     * @param ftss 传输集合
     * @return null 或者 已经存在的对象
     */
    public static Freesharing_FreeTranslist isFreeTransExist(Freesharing_FreeTranslist ta, List<Freesharing_FreeTranslist> ftss) {
        for (Freesharing_FreeTranslist fts : ftss) {
            if (fts.getIp().equalsIgnoreCase(ta.getIp()) & fts.getFilename().equalsIgnoreCase(ta.getFilename())) {
                return fts;
            }
        }
        return null;
    }

}
