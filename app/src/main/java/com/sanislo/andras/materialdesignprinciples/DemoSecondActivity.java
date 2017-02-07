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
    public static final String PHOTO_ONE = "https://wallpaperscraft.com/image/toyota_supra_side_view_light_97798_1280x720.jpg";
    public static final String PHOTO_TWO = "https://wallpaperscraft.com/image/joy_jennifer_lawrence_2015_105464_1920x1080.jpg";
    public static final String PHOTO_THREE = "https://aos.iacpublishinglabs.com/question/aq/1400px-788px/colors-make-up-white-light_806a2a185b18277e.jpg?domain=cx.aos.ask.com";
    public static final String PHOTO_FOUR = "http://s.4pda.to/hGEtq2mw1WjVWZG7x6b7gHFRK2OfRVU4pj9X.jpg";
    public static final String PHOTO_FIVE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSShsCo2Iuc6WvinYP7Cfz-5DeVxev8gS0CeRipzvMvSnm-0265";

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
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if (i % 5 == 0) {
                Comment comment = new Comment(PHOTO_ONE, "Jennifer Lawrence", getString(R.string.lorem_short));
                comments.add(comment);
            } else if (i % 5 == 1) {
                Comment comment = new Comment(PHOTO_TWO, "AAA AAA", getString(R.string.lorem_short));
                comments.add(comment);
            } else if (i % 5 == 2) {
                Comment comment = new Comment(PHOTO_THREE, "BBBB BBBB", getString(R.string.lorem_short));
                comments.add(comment);
            } else if (i % 5 == 3) {
                Comment comment = new Comment(PHOTO_FOUR, "CCC CCC", getString(R.string.lorem_short));
                comments.add(comment);
            } else {
                Comment comment = new Comment(PHOTO_FIVE, "DDD DDD", getString(R.string.lorem_short));
                comments.add(comment);
            }
        }
        mCommentAdapter = new CommentColorAdapter(DemoSecondActivity.this, comments);
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
