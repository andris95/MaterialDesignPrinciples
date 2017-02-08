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

    private DetailFragmentPagerAdapter mDetailsFragmentPagerAdapter;
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
        setupEnterSharedElementCallback();
        ButterKnife.bind(this);
        postponeEnterTransition();

        mStartPosition = getIntent().getIntExtra(EXTRA_START_POSITION, 0);
        mReturnPosition = mStartPosition;
        Log.d(TAG, "onCreate: mStartPosition: " + mStartPosition);
        setupViewPager();
    }

    private void setupViewPager() {
        mDetailsFragmentPagerAdapter = new DetailFragmentPagerAdapter(Utils.populatePhotos(), getSupportFragmentManager());
        vpDetails.setAdapter(mDetailsFragmentPagerAdapter);
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

    private void setupEnterSharedElementCallback() {
        SharedElementCallback sharedElementCallback = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (mIsReturning) {
                    ImageView sharedElement = mDetailsFragmentPagerAdapter.getCurrentDetailFragment().getAlbumImage();
                    if (sharedElement == null) {
                        // If shared element is null, then it has been scrolled off screen and
                        // no longer visible. In this case we cancel the shared element transition by
                        // removing the shared element from the shared elements map.
                        names.clear();
                        sharedElements.clear();
                    } else if (mStartPosition != mReturnPosition) {
                        // If the user has swiped to a different ViewPager page, then we need to
                        // remove the old shared element and replace it with the new shared element
                        // that should be transitioned instead.
                        names.clear();
                        names.add(sharedElement.getTransitionName());
                        sharedElements.clear();
                        sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                    }
                }
            }
        };
        setEnterSharedElementCallback(sharedElementCallback);
    }

    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_START_POSITION, mStartPosition);
        intent.putExtra(EXTRA_RETURN_POSITION, mReturnPosition);
        Log.d(TAG, "finishAfterTransition: " + mStartPosition + " / " + mReturnPosition);
        setResult(RESULT_OK, intent);
        super.finishAfterTransition();
    }
}
