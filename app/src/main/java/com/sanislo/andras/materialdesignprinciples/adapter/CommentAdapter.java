package com.sanislo.andras.materialdesignprinciples.adapter;

import android.content.Context;
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
import com.sanislo.andras.materialdesignprinciples.Comment;
import com.sanislo.andras.materialdesignprinciples.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 07.02.17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private String TAG = CommentAdapter.class.getSimpleName();
    private List<Comment> mComments;
    private OnClickListener mOnClickListener;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mExpandedPosition = RecyclerView.NO_POSITION;

    public CommentAdapter(Context context, List<Comment> comments) {
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
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
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
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivAuthorAvatar);

            setData();
            setCommentFooterVisibility(mPosition);
        }

        private void setData() {
            tvAuthorName.setText(mComments.get(mPosition).getAuthorName());
            tvCommentText.setText(mComments.get(mPosition).getCommentText());
        }

        private void setCommentFooterVisibility(int position) {
            boolean isExpanded = mExpandedPosition == position;
            ivShare.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            ivLike.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            mRootView.setActivated(isExpanded);
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
