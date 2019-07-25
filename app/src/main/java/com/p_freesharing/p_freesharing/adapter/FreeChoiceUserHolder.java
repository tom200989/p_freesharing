package com.p_freesharing.p_freesharing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/4/10 0010.
 */

public class FreeChoiceUserHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlUser;// 单个用户面板
    public TextView tvIp;// 用户IP
    public TextView tvPhoneName;// 用户手机品牌
    public ImageView ivConning;// 正在连接

    public FreeChoiceUserHolder(View itemView) {
        super(itemView);
        rlUser = itemView.findViewById(R.id.rl_item_free_choice_user);
        tvIp = itemView.findViewById(R.id.tv_item_free_choice_user_ip);
        tvPhoneName = itemView.findViewById(R.id.tv_item_free_choice_user_PhoneName);
        ivConning = itemView.findViewById(R.id.iv_item_free_choice_user_headconn);
    }
}
