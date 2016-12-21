package com.sds.study.humidgraph;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by efro2 on 2016-12-21.
 */

public class ChartListAdapter extends BaseAdapter {
    DetailActivity detailActivity;
    public List<ChartDTO> list;
    public ChartListAdapter(DetailActivity detailActivity) {
        this.detailActivity=detailActivity;
        this.list=detailActivity.chartList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChartDTO dto=(ChartDTO)list.get(position);
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
