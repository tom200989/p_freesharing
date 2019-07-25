package com.p_freesharing.p_freesharing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p_circlebar.p_circlebar.core.CircleBar;
import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/4/24 0024.
 */

public class FreeTranslistHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlAll;// 总布局 
    
    public ImageView ivPic;// 缩略图
    public TextView tvTitle;// 标题
    public TextView tvIP;// 对方IP
    public TextView tvSize;// 大小: 如: 125MB/300MB

    public RelativeLayout rlProgress;// 进度面板
    public CircleBar cpProgress;// 进度圈
    
    public TextView tvProgress;// 进度百分比

    public RelativeLayout rlRetry;//  客户端重试面板
    public ImageView ivRetry; // 客户端重试图标
    
    public ImageView ivFinish;// 成功图标
    public ImageView ivFailed;// 失败图标


    public FreeTranslistHolder(View itemView) {
        super(itemView);
        rlAll = itemView.findViewById(R.id.rl_item_free_translist_all);
        ivPic = itemView.findViewById(R.id.iv_item_free_translist_pic);
        tvTitle = itemView.findViewById(R.id.tv_item_free_translist_title);
        tvIP = itemView.findViewById(R.id.tv_item_free_translist_ip);
        tvSize = itemView.findViewById(R.id.tv_item_free_translist_size);
        rlProgress = itemView.findViewById(R.id.rl_item_free_translist_progress);
        cpProgress = itemView.findViewById(R.id.cp_item_free_translist_progress);
        tvProgress = itemView.findViewById(R.id.tv_item_free_translist_percent);
        rlRetry = itemView.findViewById(R.id.rl_item_free_translist_retry);
        ivRetry = itemView.findViewById(R.id.iv_item_free_translist_retry_logo);
        ivFailed = itemView.findViewById(R.id.iv_item_free_translist_failed);
        ivFinish = itemView.findViewById(R.id.iv_item_free_translist_finish);
    }
}
