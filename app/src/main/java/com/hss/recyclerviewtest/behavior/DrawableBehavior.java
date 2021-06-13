package com.hss.recyclerviewtest.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class DrawableBehavior extends CoordinatorLayout.Behavior<TextView> {////这里的泛型是child的类型，也就是观察者View
    int mStartY=0;
    public DrawableBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        //记录开始的Y坐标  也就是toolbar起始Y坐标
        if(mStartY == 0) {
            mStartY = (int) dependency.getY();
        }

        //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY()/mStartY;

        //改变child的坐标(从消失，到可见)
        child.setY(child.getHeight()*(1-percent) - child.getHeight());
        return true;
    }
}
