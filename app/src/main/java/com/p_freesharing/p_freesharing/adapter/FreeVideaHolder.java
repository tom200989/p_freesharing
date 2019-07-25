package com.p_freesharing.p_freesharing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/3/9 0009.
 */

public class FreeVideaHolder extends RecyclerView.ViewHolder {

    public ImageView ivFrame;
    public ImageView ivSelect;
    public TextView tvDuration;

    public FreeVideaHolder(View itemView) {
        super(itemView);
        ivFrame = itemView.findViewById(R.id.iv_item_free_pic_video);
        ivSelect = itemView.findViewById(R.id.iv_item_free_select_video);
        tvDuration = itemView.findViewById(R.id.tv_item_free_cameraTime_video);
    }
}
