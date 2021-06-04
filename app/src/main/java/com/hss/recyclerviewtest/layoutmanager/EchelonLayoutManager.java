package com.hss.recyclerviewtest.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
        if (getItemCount() == 0) return;
        int bottomItemPosition = (int) Math.floor(mScrollOffset / mItemViewHeight);
        int remainSpace = getVerticalSpace() - mItemViewHeight;

        int bottomItemVisibleHeight = mScrollOffset % mItemViewHeight;
        final float offsetPercentRelativeToItemView = bottomItemVisibleHeight * 1.0f / mItemViewHeight;
        ArrayList<ItemViewInfo> layoutInfos = new ArrayList<>();
        for (int i = bottomItemPosition - 1, j = 1; i >= 0; i--, j++) {
            double maxOffset = (getVerticalSpace() - mItemViewHeight) / 2 * Math.pow(0.8, j);
            int start = (int) (remainSpace - offsetPercentRelativeToItemView * maxOffset);
            float scaleXY = (float) (Math.pow(mScale, j - 1) * (1 - offsetPercentRelativeToItemView * (1 - mScale)));
            float positonOffset = offsetPercentRelativeToItemView;
            float layoutPercent = start * 1.0f / getVerticalSpace();
            ItemViewInfo info = new ItemViewInfo(start, scaleXY, positonOffset, layoutPercent);
            layoutInfos.add(0, info);
            remainSpace = (int) (remainSpace - maxOffset);
            if (remainSpace <= 0) {
                info.setTop((int) (remainSpace + maxOffset));
                info.setPositionOffset(0);
                info.setLayoutPercent(info.getTop() / getVerticalSpace());
                info.setScaleXY((float) Math.pow(mScale, j - 1));

                break;
            }

        }

        if (bottomItemPosition < mItemCount) {
            final int start = getVerticalSpace() - bottomItemVisibleHeight;
            layoutInfos.add(new ItemViewInfo(start, 1.0f, bottomItemVisibleHeight * 1.0f / mItemViewHeight, start * 1.0f / getVerticalSpace())
                    .setIsBottom());
        } else {
            bottomItemPosition = bottomItemPosition - 1;//99
        }

        int layoutCount = layoutInfos.size();
        final int startPos = bottomItemPosition - (layoutCount - 1);
        final int endPos = bottomItemPosition;
        final int childCount = getChildCount();
        //滑出屏幕回收到缓存中
        for (int i = childCount - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            int pos = getPosition(childView);
            if (pos > endPos || pos < startPos) {
                removeAndRecycleView(childView, recycler);
            }
        }

        detachAndScrapAttachedViews(recycler);

        for (int i = 0; i < layoutCount; i++) {
            View view = recycler.getViewForPosition(startPos + i);
            ItemViewInfo layoutInfo = layoutInfos.get(i);
            addView(view);
            measureChildWithExactlySize(view);
            int left = (getHorizontalSpace() - mItemViewWidth) / 2;
            layoutDecoratedWithMargins(view, left, layoutInfo.getTop(), left + mItemViewWidth, layoutInfo.getTop() + mItemViewHeight);
            view.setPivotX(view.getWidth() / 2);
            view.setPivotY(0);
            view.setScaleX(layoutInfo.getScaleXY());
            view.setScaleY(layoutInfo.getScaleXY());
        }
    }

    /**
     * 测量itemview的确切大小
     */
    private void measureChildWithExactlySize(View child ) {
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(mItemViewWidth, View.MeasureSpec.EXACTLY);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(mItemViewHeight, View.MeasureSpec.EXACTLY);
        child.measure(widthSpec, heightSpec);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int pendingScrollOffset = mScrollOffset + dy;
        mScrollOffset = Math.min(Math.max(mItemViewHeight, mScrollOffset + dy), mItemCount * mItemViewHeight);
        fill(recycler,state);
        return mScrollOffset - pendingScrollOffset + dy;
    }


    public static   class ItemViewInfo {
        private float mScaleXY;
        private float mLayoutPercent;
        private float mPositionOffset;
        private int mTop;
        private boolean mIsBottom;

        public ItemViewInfo(int top, float scaleXY, float positonOffset, float percent) {
            this.mTop = top;
            this.mScaleXY = scaleXY;
            this.mPositionOffset = positonOffset;
            this.mLayoutPercent = percent;
        }

        public ItemViewInfo setIsBottom() {
            mIsBottom = true;
            return this;
        }

        public float getScaleXY() {
            return mScaleXY;
        }

        public void setScaleXY(float mScaleXY) {
            this.mScaleXY = mScaleXY;
        }

        public float getLayoutPercent() {
            return mLayoutPercent;
        }

        public void setLayoutPercent(float mLayoutPercent) {
            this.mLayoutPercent = mLayoutPercent;
        }

        public float getPositionOffset() {
            return mPositionOffset;
        }

        public void setPositionOffset(float mPositionOffset) {
            this.mPositionOffset = mPositionOffset;
        }

        public int getTop() {
            return mTop;
        }

        public void setTop(int mTop) {
            this.mTop = mTop;
        }

    }

}
