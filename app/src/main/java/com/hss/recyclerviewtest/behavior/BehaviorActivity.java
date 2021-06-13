package com.hss.recyclerviewtest.behavior;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hss.recyclerviewtest.R;

public class BehaviorActivity extends AppCompatActivity {
    TextView tv_child;

    Button btn_observed;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_behavior);
//        tv_child = findViewById(R.id.tv_child);
//        btn_observed = findViewById(R.id.btn_observed);
//        btn_observed.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()){
//
//                    case MotionEvent.ACTION_MOVE:
//                        v.setX(event.getRawX()-v.getWidth()/2);
//                        v.setY(event.getRawY()-v.getHeight()/2);
//                        break;
//                }
//                return  false;
//            }
//        });
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior1);
    }
}
