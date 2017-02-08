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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 06.02.17.
 */

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    public static final String PHOTO_ONE = "https://wallpaperscraft.com/image/toyota_supra_side_view_light_97798_1280x720.jpg";
    public static final String PHOTO_TWO = "https://wallpaperscraft.com/image/joy_jennifer_lawrence_2015_105464_1920x1080.jpg";
    public static final String PHOTO_THREE = "https://wallpaperscraft.com/image/mountains_buildings_sky_peaks_snow_107559_1440x900.jpg";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_photos_list)
    RecyclerView rvPhotosList;

    private PhotosAdapter mPhotosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setupExitTransition();
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

    private void setupAdapter() {
        mPhotosAdapter = new PhotosAdapter(MainActivity.this, Utils.populatePhotos());
        mPhotosAdapter.setOnClickListener(new PhotosAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position, String url) {
               startDetailsActivityAnimating(view, url);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        rvPhotosList.setLayoutManager(gridLayoutManager);
        rvPhotosList.setAdapter(mPhotosAdapter);
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
