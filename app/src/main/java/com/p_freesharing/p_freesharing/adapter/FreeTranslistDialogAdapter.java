package com.p_freesharing.p_freesharing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.core.server.bean.IMRequest;

import java.util.List;

/**
 * Created by qianli.ma on 2018/4/8 0008.
 */

public class FreeTranslistDialogAdapter extends RecyclerView.Adapter<FreeTranslistDialogHolder> {

    private Context context;
    private List<IMRequest> datas;

    public FreeTranslistDialogAdapter(Context context, List<IMRequest> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void notifys(List<IMRequest> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public FreeTranslistDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreeTranslistDialogHolder(LayoutInflater.from(context).inflate(R.layout.freesharing_item_free_translist_dialog, parent, false));
    }

    @Override
    public void onBindViewHolder(FreeTranslistDialogHolder holder, int position) {
        // 1.获取
        IMRequest request = datas.get(position);
        // 2.整理
        String requestPhonename = request.getPhonename();
        int requestFileCount = request.getPathList().size();
        String title = String.format(context.getString(R.string.freesharing_From_phonename), requestPhonename);
        String des = String.format(context.getString(R.string.freesharing_There_are_files_waiting_for_you_to_accept), requestFileCount);
        // 3.设置
        holder.tvTitle.setText(title);// 手机名称
        holder.tvDes.setText(des);// 文件数量
        holder.tvCancel.setOnClickListener(v -> {// 取消按钮
            cancelNext(request);
        });
        holder.tvOk.setOnClickListener(v -> {// 同意按钮
            okNext(request);
        });
        holder.vSplit.setVisibility(datas.size() > 1 ? View.VISIBLE : View.GONE);
        // 4.动画(最后一个)
        if (position == 0) {
            AlphaAnimation aa = new AlphaAnimation(0, 1);
            aa.setDuration(500);
            aa.setFillAfter(true);
            holder.llDialog.setAnimation(aa);
            holder.llDialog.startAnimation(aa);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private OnFreeTranslistDialogOkListener onFreeTranslistDialogOkListener;

    // 接口OnFreeTranslistDialogOkListener
    public interface OnFreeTranslistDialogOkListener {
        void ok(IMRequest request);
    }

    // 对外方式setOnFreeTranslistDialogOkListener
    public void setOnFreeTranslistDialogOkListener(OnFreeTranslistDialogOkListener onFreeTranslistDialogOkListener) {
        this.onFreeTranslistDialogOkListener = onFreeTranslistDialogOkListener;
    }

    // 封装方法okNext
    private void okNext(IMRequest request) {
        if (onFreeTranslistDialogOkListener != null) {
            onFreeTranslistDialogOkListener.ok(request);
        }
    }

    private OnFreeTranslistDialotCancelClickListener onFreeTranslistDialotCancelClickListener;

    // 接口OnFreeTranslistDialotCancelClickListener
    public interface OnFreeTranslistDialotCancelClickListener {
        void cancel(IMRequest request);
    }

    // 对外方式setOnFreeTranslistDialotCancelClickListener
    public void setOnFreeTranslistDialotCancelClickListener(OnFreeTranslistDialotCancelClickListener onFreeTranslistDialotCancelClickListener) {
        this.onFreeTranslistDialotCancelClickListener = onFreeTranslistDialotCancelClickListener;
    }

    // 封装方法cancelNext
    private void cancelNext(IMRequest request) {
        if (onFreeTranslistDialotCancelClickListener != null) {
            onFreeTranslistDialotCancelClickListener.cancel(request);
        }
    }

}
