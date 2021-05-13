package com.hss.recyclerviewtest.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * https://www.jianshu.com/p/34a0ef2d806d?utm_medium=email&utm_source=gank.io
 *
 * https://www.jianshu.com/p/ef3a3b8d0a77
 */
public class PageLayoutManager extends LinearLayoutManager {
    private RecyclerView mRecyclerView;
    private PagerSnapHelper mPagerSnapHelper;

    private int mDrift;//位移，用来判断移动方向

    public void setmOnViewPagerListener(OnViewPagerListener mOnViewPagerListener) {
        this.mOnViewPagerListener = mOnViewPagerListener;
    }

    OnViewPagerListener mOnViewPagerListener;
    public PageLayoutManager(Context context) {
        this(context, VERTICAL, false);
    }

    public PageLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    private void init() {
        mPagerSnapHelper= new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        this.mRecyclerView = view;
        mRecyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }


    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        mRecyclerView.removeOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
        this.mRecyclerView = null;
    }

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        //第一次进入界面的监听，可以用来实现首次播放的逻辑

        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onInitComplete();
            }
        }
        // 可以释放资源的监听，也就是回收Item的时候
        @Override
        public void onChildViewDetachedFromWindow(View view) {
            if (mDrift >= 0){
                if (mOnViewPagerListener != null)
                    mOnViewPagerListener.onPageRelease(true,getPosition(view));
            }else {
                if (mOnViewPagerListener != null)
                    mOnViewPagerListener.onPageRelease(false,getPosition(view));
            }


        }
    };


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        if (mOnViewPagerListener != null) mOnViewPagerListener.onLayoutComplete();
    }
/*    要监听滑动到哪页，需要我们重写onScrollStateChanged（）函数，
    这里面有三种状态：SCROLL_STATE_IDLE（空闲），SCROLL_STATE_DRAGGING（拖动），
    SCROLL_STATE_SETTLING（要移动到最后位置时）。我们需要的就是RecyclerView停止时的状态，
    我们就可以拿到这个View的Position.注意这里还有一个问题，
    当你通过这个position去拿Item会报错，这里涉及到RecyclerView的缓存机制，
    自己去脑补~~。打印Log,你会发现RecyclerView.getChildCount()一直为1或者会出现为2的情况。
    好了，我们自己来实现一个接口然后通过接口把状态传递出去*/

    @Override
    public void onScrollStateChanged(int state) {
        switch (state){
            case RecyclerView.SCROLL_STATE_IDLE://空闲 停止滚动
                View viewIdle = mPagerSnapHelper.findSnapView(this);
                int positionIdle = getPosition(viewIdle);
                if (mOnViewPagerListener != null && getChildCount() == 1) {
                    mOnViewPagerListener.onPageSelected(positionIdle,positionIdle == getItemCount() - 1);
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING://拖动 正在被外部拖拽,一般为用户正在用手指滚动
                break;
            case RecyclerView.SCROLL_STATE_SETTLING://要移动到最后位置时 自动滚动开始
                break;
        }
    }

    public static interface OnViewPagerListener {
        /*初始化完成*/
        void onInitComplete();
        /*释放的监听*/
        void onPageRelease(boolean isNext,int position);
        /*选中的监听以及判断是否滑动到底部*/
        void onPageSelected(int position,boolean isBottom);

        void  onLayoutComplete();
    }


}
