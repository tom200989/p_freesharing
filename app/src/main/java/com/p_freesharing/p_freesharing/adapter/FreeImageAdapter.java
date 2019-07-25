package com.p_freesharing.p_freesharing.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeImage;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;
import com.p_freesharing.p_freesharing.utils.ToastUtil_m;

import java.util.List;

/**
 * Created by qianli.ma on 2018/3/7 0007.
 */

public class FreeImageAdapter extends RecyclerView.Adapter<FreeImageHolder> {

    private Context context;
    private List<Freesharing_FreeImage> freesharingFreeImages;


    public FreeImageAdapter(Context context, List<Freesharing_FreeImage> freesharingFreeImages) {
        this.context = context;
        this.freesharingFreeImages = freesharingFreeImages;
    }

    public void notifys(List<Freesharing_FreeImage> freesharingFreeImages) {
        this.freesharingFreeImages = freesharingFreeImages;
        notifyDataSetChanged();
    }

    @Override
    public FreeImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreeImageHolder(LayoutInflater.from(context).inflate(R.layout.freesharing_item_image_free, parent, false));
    }

    @Override
    public void onBindViewHolder(FreeImageHolder holder, int position) {
        // 素材
        Drawable pressed = context.getResources().getDrawable(R.drawable.freesharing_free_item_pressed);
        Drawable unpressed = context.getResources().getDrawable(R.drawable.freesharing_free_item_unpressed);
        // freeimage
        Freesharing_FreeImage freesharingFreeImage = freesharingFreeImages.get(position);
        holder.ivPic.setImageBitmap(freesharingFreeImage.getBitmap());
        holder.ivSelect.setImageDrawable(freesharingFreeImage.isSelected() ? pressed : unpressed);
        holder.ivSelect.setOnClickListener(v -> {
            // 判断是否超限
            SharingFileActivity fsa = (SharingFileActivity) context;
            boolean isOverFiles = fsa.selectImages.size() + fsa.selectVideos.size() >= 9;

            if (isOverFiles&holder.ivSelect.getDrawable() == unpressed ) {// 总数超过9个 & 当前是未被选中-->显示吐司
                overSelectFileNext();
            }
            // 勾选
            if (isOverFiles) {
                holder.ivSelect.setImageDrawable(unpressed);
            } else {
                holder.ivSelect.setImageDrawable(holder.ivSelect.getDrawable() == pressed? unpressed : pressed);
            }
            
            freesharingFreeImage.setSelected(holder.ivSelect.getDrawable() == pressed);
            freeItemSelectClickNext(freesharingFreeImage, freesharingFreeImage.isSelected());
        });
        holder.ivPic.setOnClickListener(v -> {
            // 点击放大
            freeItemClickNext(freesharingFreeImage, position, freesharingFreeImages.size());
        });
    }

    private OnOverSelectFilesListener onOverSelectFilesListener;

    // 接口OnOverSelectFilesListener
    public interface OnOverSelectFilesListener {
        void overSelectFile();
    }

    // 对外方式setOnOverSelectFilesListener
    public void setOnOverSelectFilesListener(OnOverSelectFilesListener onOverSelectFilesListener) {
        this.onOverSelectFilesListener = onOverSelectFilesListener;
    }

    // 封装方法overSelectFileNext
    private void overSelectFileNext() {
        if (onOverSelectFilesListener != null) {
            onOverSelectFilesListener.overSelectFile();
        }
    }

    private OnFreeItemSelectClickListener onFreeItemSelectClickListener;

    // 接口OnFreeItemSelectClickListener
    public interface OnFreeItemSelectClickListener {
        void freeItemSelectClick(Freesharing_FreeImage freesharingFreeImage, boolean isSelected);
    }

    // 对外方式setOnFreeItemSelectClickListener
    public void setOnFreeItemSelectClickListener(OnFreeItemSelectClickListener onFreeItemSelectClickListener) {
        this.onFreeItemSelectClickListener = onFreeItemSelectClickListener;
    }

    // 封装方法freeItemSelectClickNext
    private void freeItemSelectClickNext(Freesharing_FreeImage freesharingFreeImage, boolean isSelected) {
        if (onFreeItemSelectClickListener != null) {
            onFreeItemSelectClickListener.freeItemSelectClick(freesharingFreeImage, isSelected);
        }
    }

    @Override
    public int getItemCount() {
        return freesharingFreeImages != null ? freesharingFreeImages.size() : 0;
    }

    private OnFreeItemClickListener onFreeItemClickListener;

    // 接口OnFreeItemClickListener
    public interface OnFreeItemClickListener {
        void freeItemClick(Freesharing_FreeImage freesharingFreeImage, int position, int total);
    }

    // 对外方式setOnFreeItemClickListener
    public void setOnFreeItemClickListener(OnFreeItemClickListener onFreeItemClickListener) {
        this.onFreeItemClickListener = onFreeItemClickListener;
    }

    // 封装方法freeItemClickNext
    private void freeItemClickNext(Freesharing_FreeImage freesharingFreeImage, int position, int total) {
        if (onFreeItemClickListener != null) {
            onFreeItemClickListener.freeItemClick(freesharingFreeImage, position, total);
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
