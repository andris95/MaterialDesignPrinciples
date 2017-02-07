package com.sanislo.andras.materialdesignprinciples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 07.02.17.
 */

public class DemoActivity extends Activity {
    private String TAG = DemoActivity.class.getSimpleName();
    public static final String PHOTO_ONE = "https://wallpaperscraft.com/image/toyota_supra_side_view_light_97798_1280x720.jpg";
    public static final String PHOTO_TWO = "https://wallpaperscraft.com/image/joy_jennifer_lawrence_2015_105464_1920x1080.jpg";
    public static final String PHOTO_THREE = "https://wallpaperscraft.com/image/mountains_buildings_sky_peaks_snow_107559_1440x900.jpg";

    @BindView(R.id.rv_comments)
    RecyclerView rvComments;

    private CommentAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        initComments();
    }

    private void initComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if (i % 3 == 0) {
                Comment comment = new Comment(PHOTO_TWO, "Jennifer Lawrence", getString(R.string.lorem_short));
                comments.add(comment);
            } else if (i % 3 == 1) {
                Comment comment = new Comment(PHOTO_ONE, "Tatara Adcv", getString(R.string.lorem_short));
                comments.add(comment);
            } else {
                Comment comment = new Comment(PHOTO_THREE, "QWezx ASDxcvdf", getString(R.string.lorem_short));
                comments.add(comment);
            }
        }
        mCommentAdapter = new CommentAdapter(DemoActivity.this, comments);
        mCommentAdapter.setOnClickListener(new CommentAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                int currentExpandedPositon = mCommentAdapter.getExpandedPosition();
                mCommentAdapter.setExpandedPosition(currentExpandedPositon == position ? RecyclerView.NO_POSITION : position);
                TransitionManager.beginDelayedTransition(rvComments);
                mCommentAdapter.notifyDataSetChanged();
                //mCommentAdapter.notifyItemChanged(position);
            }
        });
        rvComments.setLayoutManager(new LinearLayoutManager(DemoActivity.this));
        ((SimpleItemAnimator) rvComments.getItemAnimator()).setSupportsChangeAnimations(false);
        rvComments.setAdapter(mCommentAdapter);
    }
}
