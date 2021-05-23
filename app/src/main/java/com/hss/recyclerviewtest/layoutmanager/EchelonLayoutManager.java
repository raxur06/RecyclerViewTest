package com.hss.recyclerviewtest.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class EchelonLayoutManager extends RecyclerView.LayoutManager  {
    private Context mContext;
    private int mItemViewWidth;
    private int mItemViewHeight;
    private int mItemCount;
    private int mScrollOffset = Integer.MAX_VALUE;
    private float mScale = 0.9f;

    public EchelonLayoutManager(Context mContext) {
        this.mContext = mContext;
//        mItemViewWidth = (int) (getHorizontalSpace() * 0.87f);//item的宽
//        mItemViewHeight = (int) (mItemViewWidth * 1.46f);//item的高
    }

    private int  getHorizontalSpace() {
        return  getWidth() - getPaddingLeft() - getPaddingRight();
    }
    public int getVerticalSpace() {
        return  getHeight() - getPaddingTop()  - getPaddingBottom();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0 || state.isPreLayout()) return;
        removeAndRecycleAllViews(recycler);
        mItemViewWidth = (int) (getHorizontalSpace() * 0.87f);//item的宽
        mItemViewHeight = (int) (mItemViewWidth * 1.46f);//item的高


        mScrollOffset = Math.min(Math.max(mItemViewHeight, mScrollOffset), mItemCount * mItemViewHeight);

        mItemCount = getItemCount();
        fill(recycler,state);
    }
    //回收不必要的view（超出屏幕的），取出需要的显示出来
    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {

    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
