package com.hss.recyclerviewtest;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/12/12.
 */

public class MessagesAdapter extends BaseQuickAdapter<String , BaseViewHolder> {
    public MessagesAdapter() {
        super( R.layout.item_msg, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int index = helper.getLayoutPosition();//获取当前item的position
//        helper.setText(R.id.tv_date,item.createdAt==null ? "": TimeTools.getCurDayDate2(item.createdAt));
//        helper.setText(R.id.tv_title,item.);
        helper.setText(R.id.tv_subtitle,item==null?"":item);
//        helper.getView(R.id.civ_head);
    }
}
