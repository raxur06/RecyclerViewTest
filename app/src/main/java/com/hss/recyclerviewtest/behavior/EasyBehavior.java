package com.hss.recyclerviewtest.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EasyBehavior extends CoordinatorLayout.Behavior<TextView> {////这里的泛型是child的类型，也就是观察者View


    public EasyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //代表寻找被观察View
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof Button;
    }
//被观察View变化的时候回调用的方法
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        child.setX(dependency.getX()+200);
        child.setY(dependency.getY());
        child.setText(dependency.getX()+","+dependency.getY());
        return true;
    }
}
