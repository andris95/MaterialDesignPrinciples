package com.sanislo.andras.materialdesignprinciples.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.sanislo.andras.materialdesignprinciples.DetailFragment;

import java.util.List;

/**
 * Created by root on 08.02.17.
 */

public class DetailFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private DetailFragment mCurrentDetailFragment;
    private List<String> mPhotos;

    public DetailFragmentPagerAdapter(List<String> photos, FragmentManager fm) {
        super(fm);
        mPhotos = photos;
    }

    public void setPhotos(List<String> photos) {
        mPhotos = photos;
    }

    public DetailFragment getCurrentDetailFragment() {
        return mCurrentDetailFragment;
    }

    @Override
    public Fragment getItem(int position) {
        DetailFragment detailFragment = DetailFragment.newInstance(position, mPhotos.get(position));
        return detailFragment;
    }

    /**Called to inform the adapter of which item is currently considered to be the "primary", that is the one show to the user as the current page.*/
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentDetailFragment = (DetailFragment) object;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }
}
