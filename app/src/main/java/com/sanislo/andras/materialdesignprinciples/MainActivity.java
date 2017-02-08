package com.sanislo.andras.materialdesignprinciples;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.sanislo.andras.materialdesignprinciples.adapter.PhotosAdapter;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setupExitTransition();
        setupReenterTransition();
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
               startDetailsActivityAnimating(view, url);
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
        Log.d(TAG, "onActivityReenter: ");
    }

    private void startDetailsActivity(String url) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_URL, url);
        startActivity(intent);
    }

    private void startDetailsActivityAnimating(View sharedView, String url) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MainActivity.this,
                        sharedView,
                        getString(R.string.transition_photo_item));
        intent.putExtra(DetailActivity.EXTRA_URL, url);
        startActivity(intent, options.toBundle());
    }
}
