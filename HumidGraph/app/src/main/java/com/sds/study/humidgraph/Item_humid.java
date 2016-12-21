package com.sds.study.humidgraph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by efro2 on 2016-12-21.
 */

public class Item_humid extends LinearLayout{
    Context context;
    LayoutInflater inflater;
    TextView txt_progress,txt_humid,txt_etc;
    public Item_humid(Context context,ChartDTO dto) {
        super(context);
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_humid,this);
        txt_progress=(TextView) findViewById(R.id.txt_progress);
        txt_humid=(TextView) findViewById(R.id.txt_humid);
        txt_etc=(TextView) findViewById(R.id.txt_etc);
        setData(dto);
    }
    public void setData(ChartDTO dto){
        txt_progress.setText(Integer.toString(dto.getProgressTime()));
        txt_humid.setText(Double.toString(dto.getHumidity()));
        txt_etc.setText(dto.getEtc());
    }
}
