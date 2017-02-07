package com.sanislo.andras.materialdesignprinciples;

/**
 * Created by root on 07.02.17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentColorAdapter extends RecyclerView.Adapter<CommentColorAdapter.ViewHolder> {
    private String TAG = CommentAdapter.class.getSimpleName();
    private List<Comment> mComments;
    private OnClickListener mOnClickListener;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mExpandedPosition = RecyclerView.NO_POSITION;

    public CommentColorAdapter(Context context, List<Comment> comments) {
            mContext = context;
            mComments = comments;
            mLayoutInflater = LayoutInflater.from(context);
            }

    public int getExpandedPosition() {
            return mExpandedPosition;
            }

    public void setExpandedPosition(int expandedPosition) {
            mExpandedPosition = expandedPosition;
            }

    public void setOnClickListener(OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
            }

    @Override
    public CommentColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_comment, parent, false);
            return new CommentColorAdapter.ViewHolder(view);
            }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(mComments.get(position), position);
            }

    @Override
    public int getItemCount() {
            return mComments.size();
            }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mRootView;

        private int mPosition;
        private int mBodyTextColor = -1;
        private int mTitleTextColor = -1;
        private int mBackgroundColor = -1;

        @BindView(R.id.iv_comment_author_avatar)
        ImageView ivAuthorAvatar;

        @BindView(R.id.tv_comment_author_name)
        TextView tvAuthorName;

        @BindView(R.id.tv_comment_text)
        TextView tvCommentText;

        @BindView(R.id.rl_comment_root)
        RelativeLayout rlCommentRoot;

        @BindView(R.id.rl_comment_footer)
        RelativeLayout rlCommentFooter;

        @BindView(R.id.rl_comment_body)
        RelativeLayout rlCommentBody;

        @BindView(R.id.iv_like_comment)
        ImageView ivLike;

        @BindView(R.id.iv_share_comment)
        ImageView ivShare;

        ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.bind(this, mRootView);
        }

        public void bind(Comment comment, final int position) {
            mPosition = position;

            Glide.with(mContext)
                    .load(comment.getPhotoUrl())
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (mBodyTextColor == -1) {
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        //work with the palette here
                                        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                                        Log.d(TAG, "onGenerated: " + dominantSwatch.getBodyTextColor() + " / " + dominantSwatch.getTitleTextColor());
                                        Log.d(TAG, "onGenerated: " + dominantSwatch.getPopulation());
                                        mBodyTextColor = dominantSwatch.getBodyTextColor();
                                        mTitleTextColor = dominantSwatch.getTitleTextColor();
                                        mBackgroundColor = dominantSwatch.getRgb();

                                        setData();
                                    }
                                });
                            }
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivAuthorAvatar);

            setCommentFooterVisibility(mPosition);
        }

        private void setData() {
            tvAuthorName.setText(mComments.get(mPosition).getAuthorName());
            tvCommentText.setText(mComments.get(mPosition).getCommentText());

            setViewsColors();
        }

        private void setViewsColors() {
            tvCommentText.setTextColor(mBodyTextColor);
            tvAuthorName.setTextColor(mTitleTextColor);
            rlCommentRoot.setBackgroundColor(mBackgroundColor);
            ivShare.setColorFilter(mBodyTextColor);
            ivLike.setColorFilter(mBodyTextColor);
        }

        private void setCommentFooterVisibility(int position) {
            boolean isExpanded = mExpandedPosition == position;
            ivShare.setVisibility(position == mExpandedPosition ? View.VISIBLE : View.GONE);
            ivLike.setVisibility(position == mExpandedPosition ? View.VISIBLE : View.GONE);
            mRootView.setActivated(isExpanded);
            Log.d(TAG, "setCommentFooterVisibility: " + (rlCommentFooter.getVisibility() == View.VISIBLE));
        }

        @OnClick(R.id.rl_comment_root)
        public void onClick() {
            Log.d(TAG, "onClick: ");
            if (mOnClickListener != null) {
                mOnClickListener.onClick(mRootView, mPosition);
            }
        }
    }
}