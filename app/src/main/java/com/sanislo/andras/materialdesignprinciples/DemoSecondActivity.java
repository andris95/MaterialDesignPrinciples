package com.sanislo.andras.materialdesignprinciples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.transition.TransitionManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 07.02.17.
 */

public class DemoSecondActivity extends Activity {

    private String TAG = DemoActivity.class.getSimpleName();

    @BindView(R.id.rv_comments)
    RecyclerView rvComments;

    private CommentColorAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        initComments();
    }

    private void initComments() {
        mCommentAdapter = new CommentColorAdapter(DemoSecondActivity.this, Utils.populateComments(this));
        mCommentAdapter.setOnClickListener(new CommentColorAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                int currentExpandedPositon = mCommentAdapter.getExpandedPosition();
                mCommentAdapter.setExpandedPosition(currentExpandedPositon == position ? RecyclerView.NO_POSITION : position);
                TransitionManager.beginDelayedTransition(rvComments);
                //mCommentAdapter.notifyDataSetChanged();
                mCommentAdapter.notifyItemChanged(currentExpandedPositon);
                mCommentAdapter.notifyItemChanged(position);
            }
        });
        rvComments.setLayoutManager(new LinearLayoutManager(DemoSecondActivity.this));
        ((SimpleItemAnimator) rvComments.getItemAnimator()).setSupportsChangeAnimations(false);
        rvComments.setAdapter(mCommentAdapter);
    }
}
