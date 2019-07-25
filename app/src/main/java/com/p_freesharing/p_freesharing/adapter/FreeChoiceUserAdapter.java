package com.p_freesharing.p_freesharing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeUser;

import java.util.List;

/**
 * Created by qianli.ma on 2018/4/10 0010.
 */

public class FreeChoiceUserAdapter extends RecyclerView.Adapter<FreeChoiceUserHolder> {


    private Context context;
    private List<Freesharing_FreeUser> users;

    public FreeChoiceUserAdapter(Context context, List<Freesharing_FreeUser> users) {
        this.context = context;
        this.users = users;
    }

    public void notifys(List<Freesharing_FreeUser> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public FreeChoiceUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreeChoiceUserHolder(LayoutInflater.from(context).inflate(R.layout.freesharing_item_free_choice_user, parent, false));
    }

    @Override
    public void onBindViewHolder(FreeChoiceUserHolder holder, int position) {
        Freesharing_FreeUser freesharingFreeUser = users.get(position);
        holder.tvIp.setText(freesharingFreeUser.getIp());
        holder.tvPhoneName.setText(freesharingFreeUser.getPhoneName());
        // 设置动画
        holder.ivConning.setVisibility(freesharingFreeUser.isConnecting() ? View.VISIBLE : View.GONE);
        if (freesharingFreeUser.isConnecting()) {
            RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(2000);
            ra.setInterpolator(new AccelerateDecelerateInterpolator());
            ra.setRepeatMode(Animation.INFINITE);
            ra.setRepeatCount(Animation.INFINITE);
            ra.reset();
            holder.ivConning.setAnimation(ra);
            holder.ivConning.startAnimation(ra);
        }

        // 限制是否可点(当某一个item被点击时, 所有都不可点)
        if (freesharingFreeUser.isCanClick()) {
            holder.rlUser.setOnClickListener(v -> {
                clickNext(freesharingFreeUser, position);
            });
        } else {
            holder.rlUser.setOnClickListener(null);
        }

    }
    
    @Override
    public int getItemCount() {
        return users.size();
    }

    private OnUserClickListener onUserClickListener;

    // 接口OnUserClickListener
    public interface OnUserClickListener {
        void click(Freesharing_FreeUser freesharingFreeUser, int position);
    }

    // 对外方式setOnUserClickListener
    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    // 封装方法clickNext
    private void clickNext(Freesharing_FreeUser freesharingFreeUser, int position) {
        if (onUserClickListener != null) {
            onUserClickListener.click(freesharingFreeUser, position);
        }
    }
}
