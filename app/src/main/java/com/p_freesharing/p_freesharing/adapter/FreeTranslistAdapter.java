package com.p_freesharing.p_freesharing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p_freesharing.p_freesharing.R;
import com.p_freesharing.p_freesharing.bean.Freesharing_FreeTranslist;
import com.p_freesharing.p_freesharing.utils.FormatTools;
import com.p_freesharing.p_freesharing.utils.Logs;

import java.util.List;

import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_FILETYPE_PIC;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_FILETYPE_VIDEO;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_STATE_FAILED;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_STATE_SUCCESS;
import static com.p_freesharing.p_freesharing.core.server.handler.IBaseHandler.TRANSLIST_STATE_UPLOADING;


/**
 * Created by qianli.ma on 2018/4/24 0024.
 */

public class FreeTranslistAdapter extends RecyclerView.Adapter<FreeTranslistHolder> {


    private Context context;
    private List<Freesharing_FreeTranslist> translists;
    private String TAG = "FreeTranslistAdapter";

    public FreeTranslistAdapter(Context context, List<Freesharing_FreeTranslist> translists) {
        this.context = context;
        this.translists = translists;
    }

    public void notifiys(List<Freesharing_FreeTranslist> translists) {
        this.translists = translists;
        notifyDataSetChanged();
    }

    @Override
    public FreeTranslistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreeTranslistHolder(LayoutInflater.from(context).inflate(R.layout.freesharing_item_free_translist, parent, false));
    }

    @Override
    public void onBindViewHolder(FreeTranslistHolder holder, int position) {

        Freesharing_FreeTranslist ta = translists.get(position);

        if (ta.isSend()) {/* 客户端 */
            holder.ivPic.setImageBitmap(ta.getPicBitmap());
            holder.tvTitle.setText(ta.getFilename());
            holder.tvIP.setText(String.format(context.getString(R.string.freesharing_to), ta.getIp(), ta.getPhone()));
            holder.tvSize.setText(ta.getSize());
            holder.rlProgress.setVisibility(ta.getState() == TRANSLIST_STATE_UPLOADING ? View.VISIBLE : View.GONE);
            holder.cpProgress.setMax(100);
            holder.cpProgress.setProgress(ta.getProgress());
            holder.tvProgress.setText(String.valueOf(ta.getProgress() + "%"));
            holder.ivFailed.setVisibility(View.GONE);// 失败图标隐藏(客户端允许重试--> 所以不显示失败图标)
            holder.rlRetry.setVisibility(ta.getState() == TRANSLIST_STATE_FAILED ? View.VISIBLE : View.GONE);// 重试图标显示
            holder.ivFinish.setVisibility(ta.getState() == TRANSLIST_STATE_SUCCESS ? View.VISIBLE : View.GONE);

            holder.rlProgress.setOnClickListener(v -> {
                // 正在传输时传输需要取消暂停
                cancelNext(ta, position);
            });
            holder.rlRetry.setOnClickListener(v -> {
                // 点击了重试按钮--> 重新请求网络
                retryNext(ta, position);
            });
            holder.rlAll.setOnClickListener(v -> {
                // 显示大图
                detailPicNext(ta, position);
            });
        } else {/* 服务器端: 只显示成功或者传输中状态 */
            Drawable picDraw = context.getResources().getDrawable(R.drawable.freesharing_item_photo);
            Drawable videoDraw = context.getResources().getDrawable(R.drawable.freesharing_item_video);
            Drawable unknownDraw = context.getResources().getDrawable(R.drawable.freesharing_item_unknown);
            if (ta.getState() == TRANSLIST_STATE_UPLOADING | ta.getState() == TRANSLIST_STATE_FAILED) {// 传输中|传输失败
                int fileType = ta.getFileType();
                if (fileType == TRANSLIST_FILETYPE_PIC) {
                    holder.ivPic.setImageDrawable(picDraw);
                } else if (fileType == TRANSLIST_FILETYPE_VIDEO) {
                    holder.ivPic.setImageDrawable(videoDraw);
                } else {
                    holder.ivPic.setImageDrawable(unknownDraw);
                }
            } else if (ta.getState() == TRANSLIST_STATE_SUCCESS) {// 传输成功
                Bitmap bitmap = FormatTools.getInstance().drawable2Bitmap(unknownDraw);
                try {
                    // 把缩略图转换为图元模式
                    String thumbUrl = ta.getThumbUrl();
                    byte[] picByte = FormatTools.File2byte(thumbUrl);
                    bitmap = FormatTools.getInstance().Bytes2Bitmap(picByte);
                } catch (Exception e) {
                    Logs.t(TAG).ee("adapter thumbUrl error: " + e.getMessage());
                    e.printStackTrace();
                }
                holder.ivPic.setImageBitmap(bitmap);
            }

            holder.tvTitle.setText(ta.getFilename());
            Log.i("ma_tras_text", "ta.getFilename(): "+ta.getFilename());
            holder.tvIP.setText(String.format(context.getString(R.string.freesharing_from), ta.getIp(), ta.getPhone()));
            holder.tvSize.setText(ta.getSize());
            holder.rlProgress.setVisibility(ta.getState() == TRANSLIST_STATE_UPLOADING ? View.VISIBLE : View.GONE);
            holder.cpProgress.setMax(100);
            holder.cpProgress.setProgress(ta.getProgress());
            holder.tvProgress.setText(String.valueOf(ta.getProgress() + "%"));
            holder.rlRetry.setVisibility(View.GONE);// 重试图标隐藏(服务器不能主动发起重试)
            holder.ivFailed.setVisibility(ta.getState() == TRANSLIST_STATE_FAILED ? View.VISIBLE : View.GONE);// 失败图标显示
            holder.ivFinish.setVisibility(ta.getState() == TRANSLIST_STATE_SUCCESS ? View.VISIBLE : View.GONE);
            holder.rlProgress.setOnClickListener(v -> {
                // 正在传输时传输需要取消暂停
                cancelNext(ta, position);
            });
            holder.rlAll.setOnClickListener(v -> {
                // 显示大图
                detailPicNext(ta, position);
            });
        }
    }

    private OnShowDetailPicListener onShowDetailPicListener;

    // 接口OnShowDetailPicListener
    public interface OnShowDetailPicListener {
        void detailPic(Freesharing_FreeTranslist translist, int position);
    }

    // 对外方式setOnShowDetailPicListener
    public void setOnShowDetailPicListener(OnShowDetailPicListener onShowDetailPicListener) {
        this.onShowDetailPicListener = onShowDetailPicListener;
    }

    // 封装方法detailPicNext
    private void detailPicNext(Freesharing_FreeTranslist translist, int position) {
        if (onShowDetailPicListener != null) {
            onShowDetailPicListener.detailPic(translist, position);
        }
    }

    @Override
    public int getItemCount() {
        return translists.size();
    }

    private OnTranslistRetryListener onTranslistRetryListener;

    // 接口OnTranslistRetryListener
    public interface OnTranslistRetryListener {
        void retry(Freesharing_FreeTranslist translist, int position);
    }

    // 对外方式setOnTranslistRetryListener
    public void setOnTranslistRetryListener(OnTranslistRetryListener onTranslistRetryListener) {
        this.onTranslistRetryListener = onTranslistRetryListener;
    }

    // 封装方法retryNext
    private void retryNext(Freesharing_FreeTranslist translist, int position) {
        if (onTranslistRetryListener != null) {
            onTranslistRetryListener.retry(translist, position);
        }
    }

    private OnTranslistCancelListener onTranslistCancelListener;

    // 接口OnTranslistCancelListener
    public interface OnTranslistCancelListener {
        void cancel(Freesharing_FreeTranslist translist, int position);
    }

    // 对外方式setOnTranslistCancelListener
    public void setOnTranslistCancelListener(OnTranslistCancelListener onTranslistCancelListener) {
        this.onTranslistCancelListener = onTranslistCancelListener;
    }

    // 封装方法cancelNext
    private void cancelNext(Freesharing_FreeTranslist attr, int position) {
        if (onTranslistCancelListener != null) {
            onTranslistCancelListener.cancel(attr, position);
        }
    }
}
