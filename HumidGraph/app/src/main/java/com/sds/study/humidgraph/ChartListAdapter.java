package com.sds.study.humidgraph;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by efro2 on 2016-12-21.
 */

public class ChartListAdapter extends BaseAdapter {
    DetailActivity detailActivity;
    public ChartListAdapter(DetailActivity detailActivity) {
        this.detailActivity=detailActivity;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChartDTO dto=null;
        Item_humid view=null;
        if(convertView!=null){
            view=(Item_humid)convertView;
            view.setData(dto);
        }else{
            view=new Item_humid(detailActivity,dto);
        }
        return view;
    }
}
