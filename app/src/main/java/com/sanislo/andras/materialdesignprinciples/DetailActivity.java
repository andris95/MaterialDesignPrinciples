package com.sanislo.andras.materialdesignprinciples;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sanislo.andras.materialdesignprinciples.adapter.CommentAdapter;
import com.sanislo.andras.materialdesignprinciples.adapter.DetailFragmentPagerAdapter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 06.02.17.
 */

public class DetailActivity extends AppCompatActivity {
    private String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_START_POSITION = "EXTRA_START_POSITION";
    public static final String EXTRA_RETURN_POSITION = "EXTRA_RETURN_POSITION";

    @BindView(R.id.vp_details)
    ViewPager vpDetails;

    private int mStartPosition;
    private int mReturnPosition;
    private boolean mIsReturning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_details);
        setupEnterTransition();
        ButterKnife.bind(this);
        postponeEnterTransition();

        mStartPosition = getIntent().getIntExtra(EXTRA_START_POSITION, 0);
        Log.d(TAG, "onCreate: mStartPosition: " + mStartPosition);
        setupViewPager();
    }

    private void setupViewPager() {
        vpDetails.setAdapter(new DetailFragmentPagerAdapter(Utils.populatePhotos(), getSupportFragmentManager()));
        vpDetails.setCurrentItem(mStartPosition);
        vpDetails.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mReturnPosition = position;
            }
        });
    }

    private void setupEnterTransition() {
        Transition transition = TransitionHelper.getDetailActivityEnterTransition(DetailActivity.this);
        getWindow().setEnterTransition(transition);
    }

    @Override
    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_START_POSITION, mStartPosition);
        intent.putExtra(EXTRA_RETURN_POSITION, mReturnPosition);
        setResult(RESULT_OK);
        super.finishAfterTransition();
    }
}
