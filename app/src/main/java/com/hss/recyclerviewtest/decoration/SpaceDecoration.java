package com.hss.recyclerviewtest.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    boolean vertical;
    int divider;
    Paint paint;
    final String TAG = "SpaceDecoration";
    public SpaceDecoration(boolean vertical, int divider) {
        this.vertical = vertical;
        this.divider = divider;
        paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Log.e(TAG,"onDrawOver");
        int childCount = parent.getChildCount();
        Log.e(TAG,"childCount = "+childCount);
        int left = parent.getPaddingLeft();
        for (int i = 0; i <= childCount - 1; i++) {
            View child = parent.getChildAt(i);
            Rect rect = new Rect(left,child.getBottom(),child.getRight(),child.getBottom()+divider);
            c.drawRect(rect,paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom =  divider;
        outRect.left = divider/2;
    }
}
