package com.p_freesharing.p_freesharing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/3/7 0007.
 */

public class FreeImageHolder extends RecyclerView.ViewHolder {


    public ImageView ivSelect;
    public ImageView ivPic;

    public FreeImageHolder(View itemView) {
        super(itemView);
        ivPic = itemView.findViewById(R.id.iv_item_free_pic_image);
        ivSelect = itemView.findViewById(R.id.iv_item_free_select_image);
    }
}
