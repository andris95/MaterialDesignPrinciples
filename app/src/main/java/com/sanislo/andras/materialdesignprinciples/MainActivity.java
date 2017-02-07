package com.sanislo.andras.materialdesignprinciples;


import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 06.02.17.
 */

public class MainActivity extends Activity {
    private String TAG = MainActivity.class.getSimpleName();
    public static final String PHOTO_ONE = "https://wallpaperscraft.com/image/toyota_supra_side_view_light_97798_1280x720.jpg";
    public static final String PHOTO_TWO = "https://wallpaperscraft.com/image/joy_jennifer_lawrence_2015_105464_1920x1080.jpg";

    @BindView(R.id.rv_photos_list)
    RecyclerView rvPhotosList;

    private PhotosAdapter mPhotosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupAdapter();
    }

    private void setupAdapter() {
        mPhotosAdapter = new PhotosAdapter(MainActivity.this, populateData());
        mPhotosAdapter.setOnClickListener(new PhotosAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position, String url) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this,
                                view,
                                getString(R.string.transition_photo_item));
                intent.putExtra(DetailActivity.EXTRA_URL, url);
                startActivity(intent, options.toBundle());
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        rvPhotosList.setLayoutManager(gridLayoutManager);
        rvPhotosList.setAdapter(mPhotosAdapter);
    }

    private List<String> populateData() {
        List<String> photoList = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if (i % 3 == 0) {
                photoList.add(PHOTO_ONE);
            } else if (i % 3 == 1) {
                photoList.add(PHOTO_TWO);
            } else {
                photoList.add("https://wallpaperscraft.com/image/mountains_buildings_sky_peaks_snow_107559_1440x900.jpg");
            }
        }
        return photoList;
    }
}
