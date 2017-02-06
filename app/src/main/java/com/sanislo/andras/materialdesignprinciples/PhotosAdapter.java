package com.sanislo.andras.materialdesignprinciples;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 06.02.17.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mPhotosList;
    private LayoutInflater mLayoutInflater;
    private OnClickListener mOnClickListener;

    public PhotosAdapter(Context context, List<String> photosList) {
        mContext = context;
        mPhotosList = photosList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mPhotosList.size();
    }

    public interface OnClickListener {
        void onClick(View view, int position, String url);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mRootView;

        @BindView(R.id.iv_photo_detail)
        ImageView ivPhotoDetail;

        ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.bind(this, mRootView);
            ivPhotoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(ivPhotoDetail, getAdapterPosition(), mPhotosList.get(getAdapterPosition()));
                    }
                }
            });
        }

        void bind(int position) {
            Glide.with(mContext)
                    .load(mPhotosList.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPhotoDetail);
        }
    }
}
