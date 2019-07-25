package com.p_freesharing.p_freesharing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p_freesharing.p_freesharing.R;


/**
 * Created by qianli.ma on 2018/4/8 0008.
 */

public class FreeTranslistDialogHolder extends RecyclerView.ViewHolder {

    public LinearLayout llDialog;
    public TextView tvTitle;
    public TextView tvDes;
    public TextView tvCancel;
    public TextView tvOk;
    public View vSplit;

    public FreeTranslistDialogHolder(View itemView) {
        super(itemView);
        llDialog = itemView.findViewById(R.id.ll_item_free_translist_dialog);
        tvTitle = itemView.findViewById(R.id.tv_item_free_translist_dialog_title);
        tvDes = itemView.findViewById(R.id.tv_item_free_translist_dialog_des);
        tvCancel = itemView.findViewById(R.id.tv_free_item_translist_cancel);
        tvOk = itemView.findViewById(R.id.tv_free_item_translist_ok);
        vSplit = itemView.findViewById(R.id.v_item_free_translist_split);
        
    }
}
