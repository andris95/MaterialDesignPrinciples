package com.sanislo.andras.materialdesignprinciples;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by root on 08.02.17.
 */

public class GridMarginDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
        outRect.left = 4;
        outRect.right = 4;
        outRect.top = 4;
        outRect.bottom = 4;
    }
}
