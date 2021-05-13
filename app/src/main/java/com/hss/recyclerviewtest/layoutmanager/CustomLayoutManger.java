package com.hss.recyclerviewtest.layoutmanager;//package com.hss.recyclerviewtest.layoutmanager;
//
//import android.graphics.Rect;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
///*
//* https://blog.csdn.net/boboyuwu/article/details/76819534
//* */
//public class CustomLayoutManger extends RecyclerView.LayoutManager {
//    SparseArray<Rect> mViewsRF;
//    SparseArray<View> mVisibableViews;
//    int mLeftDecorationWidth;
//    int mRightDecorationWidth;
//    int mTopDecorationHeight;
//    int mBottomDecorationHeight;
//    int mDecorationMeasuredWidth;
//    int mDecorationMeasuredHeight;
//
//
//    int mScrollOffsetDy = 0;
//    /*   这个方法是必须实现的，那么实现它有什么作用呢？简单的解释下，
//            假设我们平时调用LayoutInflate.inflate(resource , null, false)
//            第二个参数传的是null或者直接使用View.inflate()去填充View的时候那么填充的View是没有布局参数的，
//    那么当我们的Recyclerview去addView()时就会进行判断，
//    如果childView的布局参数为null就是调用这个方法去生成一个默认的布局参数。*/
//    @Override
//    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
//        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//    }
//
////自定义ViewGroup要重写并实现一个onLayout()方法，我们这里类似需要实现onLayoutChildren()这个方法
//    @Override
//    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        if (getItemCount() == 0 && state.isPreLayout()){
//            return;
//        }
///*        首先我们需要判断itemCount是否为空，state.isPreLayout()是判断之前布局时动画有没有处理结束，
//        这里我们假设所有的itemView的宽和高都是相等的，创建第一个view并测量得到这个view的宽和高，
//        注意这里我们调用的是getDecoratedMeasuredWidth方法而不是getMeasuredWidth*/
//        if (mViewsRF == null){
//            mViewsRF = new SparseArray<>();
//        }else {
//            mViewsRF.clear();
//        }
//        if (getChildCount()== 0){
//            View childView = recycler.getViewForPosition(0);
//            addView(childView);
//            measureChildWithMargins(childView,0,0);
//            mLeftDecorationWidth = getLeftDecorationWidth(childView);
//            mRightDecorationWidth = getRightDecorationWidth(childView);
//            mTopDecorationHeight = getTopDecorationHeight(childView);
//            mBottomDecorationHeight = getBottomDecorationHeight(childView);
//            mDecorationMeasuredWidth = getDecoratedMeasuredWidth(childView);
//            mDecorationMeasuredHeight = getDecoratedMeasuredHeight(childView);
//            detachAndScrapView(childView,recycler);
//        }
//
//        int childHeightOffset = getPaddingTop();
//        //存储每一个view的位置
//        for (int i=0;i<getItemCount();i++){
//            Rect rect = new Rect(getPaddingLeft(),childHeightOffset,mDecorationMeasuredWidth,childHeightOffset+mDecorationMeasuredHeight);
//            mViewsRF.put(i,rect);
//            childHeightOffset +=mDecorationMeasuredHeight;
//        }
//        fillViews(啊,recycler, state);
//    }
//
//    private void fillViews(int direction,RecyclerView.Recycler recycler, RecyclerView.State state){
//        if (state.isPreLayout()){
//            return;
//        }
//        if (mVisibableViews == null){
//            mVisibableViews = new SparseArray<>();
//        }else {
//            mVisibableViews.clear();
//        }
//        int childHeightOffset = getPaddingTop();
////        if (getChildCount() == 0){
////            for (int i=0;i<getVisibableChildCount();i++){
////                View childView = recycler.getViewForPosition(i);
////                addView(childView);
////                measureChildWithMargins(childView,0,0);
////                layoutDecorated(childView,getPaddingLeft(),childHeightOffset,mDecorationMeasuredWidth-getPaddingRight(),childHeightOffset+mDecorationMeasuredHeight);
////                childHeightOffset +=mDecorationMeasuredHeight;
////                mVisibableViews.put(i,childView);
////            }
////        }
//
//
//        //方案2
//        //可见区域范围内的
//        Rect visiableRangeRF =new Rect(getPaddingLeft(),mScrollOffsetDy,mDecorationMeasuredWidth-getPaddingRight(),mScrollOffsetDy + getVerticalSpace());
//        Rect reycleRageRF = new Rect(getPaddingLeft(), 0,mDecorationMeasuredWidth - getPaddingRight(),getVerticalSpace());
//
//
//        //每个child 区域范围
//        Rect rect = new Rect();
//        //回收这个view
//        if (getChildCount() != 0){
//            //Rect
//            //判断是否有view 划出边界回收
//            for (int i=0;i<getChildCount();i++){
//                View childView = getChildAt(i);
//                rect.set(getDecoratedLeft(childView),getDecoratedTop(childView),getDecoratedRight(childView),getDecoratedBottom(childView));
//                if (!Rect.intersects(rect,reycleRageRF)){
//                    removeAndRecycleView(childView,recycler);
//                    i--;
//                    Log.e("recycle",childView.toString());
//                }
//            }
//            //所有view 存入scap中detach 然后再 attach
//            detachAndScrapAttachedViews(recycler);
//            for (int i=0;i <getItemCount();i++){
//                rect = mViewsRF.get(i);
//                if (Rect.intersects(rect,visiableRangeRF)){
//                    View view = recycler.getViewForPosition(i);
//                    addView(view);
//                    measureChildWithMargins(view,0,0);
//                     layoutDecorated(view,rect.left,rect.top- mScrollOffsetDy, rect.right,rect.bottom - mScrollOffsetDy);
//                }
//            }
//
//        }
//    }
//    @Override
//    public boolean canScrollVertically() {
//        return true;
//    }
//
///*    当我们需要垂直滚动时就重写scrollVerticallyBy这个方法，它会把垂直滚动的偏移量传递给到dy里。
//
//    这里进行下逻辑判断
//    dy>0:向上滚动
//    dy<0:向下滚动
//    这里处理一下边界越界情况，最后将实际位移距离应用给子视图，注意这里返回的偏移量不能算错了，因为返回值被用来决定什么时候取消 flings，
//    如果返回错误的值会让你失去对 content fling 的控制，并且正确的返回值还可以正确拥有边缘发光效果。
//  */
//    @Override
//    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        if (dy > 0){
//            if (mScrollOffsetDy +dy+getVerticalSpace()>getItemHeightCount()){
//                dy = getItemHeightCount() - mScrollOffsetDy - getVerticalSpace();
//            }
//        }else{
//            if (mScrollOffsetDy +dy <0){
//                dy = -mScrollOffsetDy;
//            }
//        }
//
//        mScrollOffsetDy = mScrollOffsetDy + dy;
//        offsetChildrenVertical(-dy);
//        if (dy >0){
//            fillViews(啊,recycler,state);
//        }else {
//            fillViews(2,recycler,state);
//        }
//        return dy;
//    }
//}
