package com.sanislo.andras.materialdesignprinciples;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sanislo.andras.materialdesignprinciples.adapter.CommentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 08.02.17.
 */

public class DetailFragment extends Fragment {
    private String TAG = DetailFragment.class.getSimpleName();
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @BindView(R.id.rv_comments)
    RecyclerView rvComments;

    private CommentAdapter mCommentAdapter;
    private int mPosition;
    private String mURL;
    private boolean isFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mURL = getArguments().getString(EXTRA_URL);
        mPosition = getArguments().getInt(EXTRA_POSITION);
        Log.d(TAG, "onCreate: mURL: " + mURL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_with_toolbar, container, false);
        ButterKnife.bind(this, rootView);
        String transitionName = getString(R.string.transition_photo_item) + "_" + mPosition;
        ivPhoto.setTransitionName(transitionName);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadImage();
        initComments();
    }

    public static DetailFragment newInstance(int position, String url) {
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        args.putInt(EXTRA_POSITION, position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void loadImage() {
        Glide.with(this)
                .load(mURL)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        scheduleStartPostponedTransition(ivPhoto);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: ");
                        scheduleStartPostponedTransition(ivPhoto);
                        return false;
                    }
                })
                .into(ivPhoto);
    }

    private void initComments() {
        mCommentAdapter = new CommentAdapter(getActivity(), Utils.populateComments(getActivity()));
        mCommentAdapter.setOnClickListener(new CommentAdapter.OnClickListener() {
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
        rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvComments.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) rvComments.getItemAnimator()).setSupportsChangeAnimations(false);
        rvComments.setAdapter(mCommentAdapter);
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
                        getActivity().startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    /**
     * Returns the shared element that should be transitioned back to the previous Activity,
     * or null if the view is not visible on the screen.
     */
    @Nullable
    ImageView getAlbumImage() {
        if (isViewInBounds(getActivity().getWindow().getDecorView(), ivPhoto)) {
            return ivPhoto;
        }
        return null;
    }

    /**
     * Returns true if {@param view} is contained within {@param container}'s bounds.
     */
    private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
        Rect containerBounds = new Rect();
        container.getHitRect(containerBounds);
        return view.getLocalVisibleRect(containerBounds);
    }

    @OnClick({R.id.iv_photo, R.id.tv_description_text})
    public void onClick() {
        Intent intent = new Intent(getActivity(), DemoActivity.class);
        startActivity(intent);
    }
}
