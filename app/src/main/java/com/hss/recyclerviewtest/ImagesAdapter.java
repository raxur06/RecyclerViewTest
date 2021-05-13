package com.hss.recyclerviewtest;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class ImagesAdapter extends BaseQuickAdapter<String ,BaseViewHolder> {
    int[] images= new  int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j};
    public ImagesAdapter() {
        super( R.layout.item_image, null);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int index = helper.getLayoutPosition();
        helper.setBackgroundRes(R.id.image,images[index%images.length]);
    }
}
