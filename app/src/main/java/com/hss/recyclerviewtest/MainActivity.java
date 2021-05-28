package com.hss.recyclerviewtest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss.recyclerviewtest.decoration.SpaceDecoration;
import com.hss.recyclerviewtest.layoutmanager.PageLayoutManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcv;
    MessagesAdapter messagesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcv = findViewById(R.id.rcv);
        initRecyclerView();
    }

    void initRecyclerView() {
        rcv.setLayoutManager(new LinearLayoutManager(this));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcv);

        SpaceDecoration spaceDecoration = new SpaceDecoration(true,100);
        rcv.addItemDecoration(spaceDecoration);
        rcv.setHasFixedSize(false);
        if (messagesAdapter == null){
            messagesAdapter = new MessagesAdapter();
        }
        List<String > list = new ArrayList<>();
         for (int i=0;i<50;i++){
             list.add(String .valueOf(i));
        }
        messagesAdapter.addData(list);
        rcv.setAdapter(messagesAdapter);
        messagesAdapter.bindToRecyclerView(rcv);


        messagesAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        messagesAdapter.isFirstOnly(false);  //动画默认只执行一次,如果想重复执行可设置
        messagesAdapter.disableLoadMoreIfNotFullPage();//默认第一次加载会进入回调，如果不需要可以配置：

//        messagesAdapter.setEmptyView(R.layout.empty_view);
    }

    public void setLoadComplete(){
        if (messagesAdapter !=null){
            messagesAdapter.loadMoreComplete();
        }
    }

    public void setLoadMoreFail(){
        if (messagesAdapter !=null){
            messagesAdapter.loadMoreFail();
        }
    }
    public void setLoadMoreEnd(){
        if (messagesAdapter !=null){
            messagesAdapter.loadMoreEnd(true);
        }
    }
//    public void setEnableRefresh(boolean enable){
//        if ( swipeLayout!=null){
//            swipeLayout.setEnabled(enable);
//        }
//    }

    public void setEnableLoadMore(boolean enable){
        if (messagesAdapter !=null){
            messagesAdapter.setEnableLoadMore(enable);
        }
    }

//    public void  addData(@NonNull Collection<? extends String > newData){
//        if (messagesAdapter !=null){
//            messagesAdapter.addData(newData);
//        }
//    }
//
//    public void setNewData(@Nullable List<String> data){
//        if (messagesAdapter !=null){
//            messagesAdapter.setNewData(data);
//        }
//    }
    public int  getAdapterDataSize(){
        if (messagesAdapter !=null){
            return  messagesAdapter.getData().size();
        }
        return  0;
    }
}
