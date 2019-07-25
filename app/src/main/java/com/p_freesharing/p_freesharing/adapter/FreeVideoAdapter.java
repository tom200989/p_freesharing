package com.p_freesharing.p_freesharing.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeVideo;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;
import com.p_freesharing.p_freesharing.utils.ToastUtil_m;

import java.util.List;

/**
 * Created by qianli.ma on 2018/3/9 0009.
 */

public class FreeVideoAdapter extends RecyclerView.Adapter<FreeVideaHolder> {

    private Context context;
    private List<Freesharing_FreeVideo> freesharingFreeVideos;

    public FreeVideoAdapter(Context context, List<Freesharing_FreeVideo> freesharingFreeVideos) {
        this.context = context;
        this.freesharingFreeVideos = freesharingFreeVideos;
    }

    public void notifys(List<Freesharing_FreeVideo> freesharingFreeVideos) {
        this.freesharingFreeVideos = freesharingFreeVideos;
        notifyDataSetChanged();
    }

    @Override
    public FreeVideaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreeVideaHolder(LayoutInflater.from(context).inflate(R.layout.freesharing_item_video_free, parent, false));
    }

    @Override
    public void onBindViewHolder(FreeVideaHolder holder, int position) {
        Drawable pressed = context.getResources().getDrawable(R.drawable.freesharing_free_item_pressed);
        Drawable unpressed = context.getResources().getDrawable(R.drawable.freesharing_free_item_unpressed);
        Freesharing_FreeVideo freesharingFreeVideo = freesharingFreeVideos.get(position);
        holder.ivFrame.setImageBitmap(freesharingFreeVideo.getFrame());
        holder.ivSelect.setImageDrawable(freesharingFreeVideo.isSelected() ? pressed : unpressed);
        holder.ivSelect.setOnClickListener(v -> {
            // 判断是否超限
            SharingFileActivity fsa = (SharingFileActivity) context;
            boolean isOverFiles = fsa.selectImages.size() + fsa.selectVideos.size() >= 9;
            if (isOverFiles&holder.ivSelect.getDrawable() == unpressed ) {
                overSelectFileNext();
            }
            // 勾选
            if (isOverFiles) {
                holder.ivSelect.setImageDrawable(unpressed);
            } else {
                holder.ivSelect.setImageDrawable(holder.ivSelect.getDrawable() == pressed ? unpressed : pressed);
            }
            
            freesharingFreeVideo.setSelected(holder.ivSelect.getDrawable() == pressed);
            freeItemSelectClickNext(freesharingFreeVideo, freesharingFreeVideo.isSelected());
        });
        holder.ivFrame.setOnClickListener(v -> {
            // 播放视频
            freeItemClickNext(freesharingFreeVideo, position, freesharingFreeVideos.size());
        });
        holder.tvDuration.setText(freesharingFreeVideo.getDuration());
    }

    private FreeImageAdapter.OnOverSelectFilesListener onOverSelectFilesListener;

    // 接口OnOverSelectFilesListener
    public interface OnOverSelectFilesListener {
        void overSelectFile();
    }

    // 对外方式setOnOverSelectFilesListener
    public void setOnOverSelectFilesListener(FreeImageAdapter.OnOverSelectFilesListener onOverSelectFilesListener) {
        this.onOverSelectFilesListener = onOverSelectFilesListener;
    }

    // 封装方法overSelectFileNext
    private void overSelectFileNext() {
        if (onOverSelectFilesListener != null) {
            onOverSelectFilesListener.overSelectFile();
        }
    }

    private OnFreeItemClickListener onFreeItemClickListener;

    // 接口OnFreeItemClickListener
    public interface OnFreeItemClickListener {
        void freeItemClick(Freesharing_FreeVideo freesharingFreeVideo, int position, int total);
    }

    // 对外方式setOnFreeItemClickListener
    public void setOnFreeItemClickListener(OnFreeItemClickListener onFreeItemClickListener) {
        this.onFreeItemClickListener = onFreeItemClickListener;
    }

    // 封装方法freeItemClickNext
    private void freeItemClickNext(Freesharing_FreeVideo freesharingFreeVideo, int position, int total) {
        if (onFreeItemClickListener != null) {
            onFreeItemClickListener.freeItemClick(freesharingFreeVideo, position, total);
        }
    }

    @Override
    public int getItemCount() {
        return freesharingFreeVideos != null ? freesharingFreeVideos.size() : 0;
    }

    private OnFreeItemSelectClickListener onFreeItemSelectClickListener;

    // 接口OnFreeItemSelectClickListener
    public interface OnFreeItemSelectClickListener {
        void freeItemSelectClick(Freesharing_FreeVideo freesharingFreeVideo, boolean isSelected);
    }

    // 对外方式setOnFreeItemSelectClickListener
    public void setOnFreeItemSelectClickListener(OnFreeItemSelectClickListener onFreeItemSelectClickListener) {
        this.onFreeItemSelectClickListener = onFreeItemSelectClickListener;
    }

    // 封装方法freeItemSelectClickNext
    private void freeItemSelectClickNext(Freesharing_FreeVideo freesharingFreeVideo, boolean isSelected) {
        if (onFreeItemSelectClickListener != null) {
            onFreeItemSelectClickListener.freeItemSelectClick(freesharingFreeVideo, isSelected);
        }
    }

    public void toast(int resId) {
        ToastUtil_m.show(context, resId);
    }

    public void toastLong(int resId) {
        ToastUtil_m.showLong(context, resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(context, content);
    }
}
