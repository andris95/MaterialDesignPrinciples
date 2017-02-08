package com.sanislo.andras.materialdesignprinciples;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.sanislo.andras.materialdesignprinciples.adapter.PhotosAdapter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 06.02.17.
 */

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_photos_list)
    RecyclerView rvPhotosList;

    private PhotosAdapter mPhotosAdapter;
    /** returned data from called activity's setResult() */
    private Intent mData;
    private int mStartedPosition;
    private int mReturnedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setupReenterTransition();
        setupExitSharedElementCallback();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.demo_2:
                Intent intent = new Intent(MainActivity.this, DemoSecondActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* When makeSceneTransitionAnimation(Activity, android.view.View, String) was used to start an Activity,
    callback will be called to handle shared elements on the launching Activity.
    Most calls will only come when returning from the started Activity.*/
    private void setupExitSharedElementCallback() {
        // if the data intent is null, then the activity is exiting
        SharedElementCallback sharedElementCallback = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (mData == null) {
                    Log.d(TAG, "setupExitSharedElementCallback: exiting");
                } else {
                    if (mStartedPosition != mReturnedPosition) {
                        //користувач зробив свайп
                        //очистмо теперішні розшарені view-шки
                        names.clear();
                        sharedElements.clear();
                        //добавимо правильну вюшку залежнy від returnedPosition
                        String newTransitionName = getString(R.string.transition_photo_item) + "_" + mReturnedPosition;
                        PhotosAdapter.ViewHolder viewHolder = (PhotosAdapter.ViewHolder) rvPhotosList.findViewHolderForAdapterPosition(mReturnedPosition);
                        View newSharedView = viewHolder.getPhotoView();
                        names.add(newTransitionName);
                        sharedElements.put(newTransitionName, newSharedView);
                    }
                }
            }
        };
        setExitSharedElementCallback(sharedElementCallback);
    }

    private void setupExitTransition() {
        Transition transition = TransitionHelper.getMainActivityExitTransition(MainActivity.this);
        getWindow().setExitTransition(transition);
    }

    private void setupReenterTransition() {
        Transition transition = TransitionHelper.getMainActivityReenterTransition(MainActivity.this);
        getWindow().setReenterTransition(transition);
    }

    private void setupAdapter() {
        mPhotosAdapter = new PhotosAdapter(MainActivity.this, Utils.populatePhotos());
        mPhotosAdapter.setOnClickListener(new PhotosAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position, String url) {
               startDetailsActivityAnimating(view, url, position);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /* emulating https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0B6Okdz75tqQsck9lUkgxNVZza1U/style_imagery_integration_scale1.png */
                switch (position % 6) {
                    case 5:
                        return 3;
                    case 3:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        rvPhotosList.addItemDecoration(new GridMarginDecoration());
        rvPhotosList.setLayoutManager(gridLayoutManager);
        rvPhotosList.setAdapter(mPhotosAdapter);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        Log.d(TAG, "onActivityReenter: " + data.toString());
        mData = data;
        mStartedPosition = data.getIntExtra(DetailActivity.EXTRA_START_POSITION, 0);
        mReturnedPosition = data.getIntExtra(DetailActivity.EXTRA_RETURN_POSITION, 0);
        if (mStartedPosition != mReturnedPosition) {
            rvPhotosList.scrollToPosition(mReturnedPosition);
        }
        postponeEnterTransition();
        rvPhotosList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rvPhotosList.getViewTreeObserver().removeOnPreDrawListener(this);
                rvPhotosList.requestLayout();
                startPostponedEnterTransition();
                return true;
            }
        });
    }

    private void startDetailsActivity(int position, String url) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    private void startDetailsActivityAnimating(View sharedView, String url, int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MainActivity.this,
                        sharedView,
                        sharedView.getTransitionName());
        intent.putExtra(DetailActivity.EXTRA_START_POSITION, position);
        startActivity(intent, options.toBundle());
    }
}
