package com.sanislo.andras.materialdesignprinciples;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 06.02.17.
 */

public class DetailActivity extends Activity {
    private String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String PHOTO_ONE = "https://wallpaperscraft.com/image/toyota_supra_side_view_light_97798_1280x720.jpg";
    public static final String PHOTO_TWO = "https://wallpaperscraft.com/image/joy_jennifer_lawrence_2015_105464_1920x1080.jpg";
    public static final String PHOTO_THREE = "https://wallpaperscraft.com/image/mountains_buildings_sky_peaks_snow_107559_1440x900.jpg";

    @BindView(R.id.iv_photo)
    ImageView ivPhotoDetail;

    @BindView(R.id.rv_comments)
    RecyclerView rvComments;

    private CommentAdapter mCommentAdapter;
    private String mURL;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setTransitionCallback();
        /** Causes glitch...
        getWindow().setSharedElementReturnTransition(null); */
        setupEnterTransition();
        setupReenterTransition();
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        postponeEnterTransition();
        mURL = getIntent().getStringExtra(EXTRA_URL);
        loadImage();
        //initComments();
    }

    private void loadImage() {
        Glide.with(DetailActivity.this)
                .load(mURL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        scheduleStartPostponedTransition(ivPhotoDetail);
                        return false;
                    }
                })
                .into(ivPhotoDetail);
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
        mCommentAdapter = new CommentAdapter(DetailActivity.this, comments);
        mCommentAdapter.setOnClickListener(new CommentAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                int currentExpandedPositon = mCommentAdapter.getExpandedPosition();
                mCommentAdapter.setExpandedPosition(currentExpandedPositon == position ? RecyclerView.NO_POSITION : position);
                TransitionManager.beginDelayedTransition(rvComments);
                mCommentAdapter.notifyDataSetChanged();
            }
        });
        rvComments.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        rvComments.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) rvComments.getItemAnimator()).setSupportsChangeAnimations(false);
        rvComments.setAdapter(mCommentAdapter);
    }

    private void setTransitionCallback() {
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                Log.d(TAG, "onSharedElementStart: ");
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                Log.d(TAG, "onSharedElementEnd: ");
                isFirst = false;
            }

            @Override
            public void onRejectSharedElements(List<View> rejectedSharedElements) {
                super.onRejectSharedElements(rejectedSharedElements);
                Log.d(TAG, "onRejectSharedElements: ");
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Log.d(TAG, "onMapSharedElements: ");
                if (!isFirst) {
                    sharedElements.clear();
                    names.clear();
                }
            }

            @Override
            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                Log.d(TAG, "onCaptureSharedElementSnapshot: ");
                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
            }

            @Override
            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
                Log.d(TAG, "onCreateSnapshotView: ");
                return super.onCreateSnapshotView(context, snapshot);
            }

            @Override
            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
                Log.d(TAG, "onSharedElementsArrived: ");
            }
        });
    }

    private void setupEnterTransition() {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.interpolator.linear_out_slow_in));
        slide.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        slide.excludeTarget(R.id.tv_description_title, true);
        slide.excludeTarget(R.id.rl_detail_comments, true);
        getWindow().setEnterTransition(slide);
    }

    private void setupReenterTransition() {
        Fade fade = new Fade();
        fade.addTarget(ivPhotoDetail);
        fade.addTarget(R.id.tv_description_title);
        fade.addTarget(R.id.tv_description_text);
        getWindow().setReenterTransition(fade);
    }

    @Override
    public void finishAfterTransition() {
        setResult(RESULT_OK);
        super.finishAfterTransition();
    }

    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     *
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     *     lives inside a Fragment hosted by the called Activity).
     *
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     *     asynchronously load/scale a bitmap before the transition can begin).
     *
     * (3) Inside a LoaderCallback's onLoadFinished() method (if the shared
     *     element depends on data queried by a Loader).
     */
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        Log.d(TAG, "onPreDraw: ");
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @OnClick({R.id.iv_photo, R.id.tv_description_text})
    public void onClick() {
        Intent intent = new Intent(DetailActivity.this, DemoActivity.class);
        startActivity(intent);
    }
}
