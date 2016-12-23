package com.sds.study.humidgraph;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    String TAG;
    double cgar;
    double start;
    double end;
    GraphicalView chart;
    ListView listView;
    TextView txt_predict;
    ChartListAdapter chartListAdapter;
    static Handler handler;
    ArrayList<Bluetooth_DataDTO> list= new ArrayList<Bluetooth_DataDTO>();
    ArrayList<ChartDTO> chartList=new ArrayList<ChartDTO>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getClass().getName();
        setContentView(R.layout.activity_detail);
        listView=(ListView)findViewById(R.id.listView);
        txt_predict=(TextView)findViewById(R.id.txt_predict);

        getList();
        //그래프 생성
        chart = ChartFactory.getLineChartView(this, getDataset(list), getRenderer());
        //레이아웃에 추가
        LinearLayout layout= (LinearLayout) findViewById(R.id.chart);
        layout.addView(chart);
        chartListAdapter=new ChartListAdapter(this);
        listView.setAdapter(chartListAdapter);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                render();
            }
        };
    }
    public void render(){
        getList();
        chartListAdapter.notifyDataSetChanged();
        chart=ChartFactory.getLineChartView(this,getDataset(list),getRenderer());
        ((LinearLayout)findViewById(R.id.chart)).addView(chart);
    }
    //그래프 설정 모음
    // http://www.programkr.com/blog/MQDN0ADMwYT3.html ( 그래프 설정 속성 한글로 써져있는 사이트 )
    private void setChartSettings(XYMultipleSeriesRenderer renderer) {
        //타이틀, x,y축 글자,xy축 범위
        renderer.setChartTitle("습도 변화 그래프");
        renderer.setXTitle("시간");
        renderer.setYTitle("습도");
        renderer.setRange(new double[] {0,6,-70,40});
        //background
        renderer.setApplyBackgroundColor(true);      //변경 가능여부
        renderer.setBackgroundColor(Color.WHITE);    //그래프 부분 색
        renderer.setMarginsColor(Color.WHITE);       //그래프 바깥 부분 색(margin)
        //글자크기
        renderer.setAxisTitleTextSize(30);          //x,y축 title
        renderer.setChartTitleTextSize(30);         //상단 title
        renderer.setLabelsTextSize(30);             //x,y축 수치
        renderer.setLegendTextSize(15);             //Series 구별 글씨 크기
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 20, 50, 50 }); //상 좌 하 우 ( '하' 의 경우 setFitLegend(true)일 때에만 가능 )
        //색
        renderer.setAxesColor(Color.RED);       //x,y축 선 색
        renderer.setLabelsColor(Color.CYAN);    //x,y축 글자색

        //x,y축 표시 간격 ( 각 축의 범위에 따라 나눌 수 있는 최소치가 제한 됨 )
        renderer.setXLabels(10);
        renderer.setYLabels(1);

        //x축 최대 최소(화면에 보여질)
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(20);
        //y축 최대 최소(화면에 보여질)
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(40);

        //클릭 가능 여부
        renderer.setClickEnabled(true);
        //줌 기능 가능 여부
        renderer.setZoomEnabled(true,true);
        //X,Y축 스크롤
        renderer.setPanEnabled(true, false);                // 가능 여부
        renderer.setPanLimits(new double[]{-2,24,20,40} );   // 가능한 범위

        //지정된 크기에 맞게 그래프를 키움
        renderer.setFitLegend(true);
        //간격에 격자 보이기
        renderer.setShowGrid(true);
    }

    //선 그리기
    private XYMultipleSeriesRenderer getRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        //---그려지는 점과 선 설정----
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.BLACK);            //색
        r.setPointStyle(PointStyle.DIAMOND);//점의 모양
        r.setFillPoints(true);             //점 체우기 여부
        renderer.addSeriesRenderer(r);
        //----------------------------
        /*
        * 다른 그래프를 추가하고 싶으면
        * XYSeriesRenderer 추가로 생성한 후
        *  renderer.addSeriesRenderer(r) 해준다 (Data도 있어야함)
        */
        setChartSettings(renderer);
        return renderer;
    }

    //데이터들
    private XYMultipleSeriesDataset getDataset( List list) {

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries series = new XYSeries("습도 변화량");
        int count=0;
        for (int i = ((list.size()<20)?0:list.size()-20); i < list.size(); i++) {
            series.add(count++, ((Bluetooth_DataDTO) list.get(i)).getHumidity1());
        }
        /*
        * 다른 그래프를 추가하고 싶으면
        * XYSeries를 추가로 생성한 후
        * dataset.addSeries(series) 해준다 (renderer도 있어야함)
        */
        dataset.addSeries(series);
        return dataset;
    }

    @Override
    public void onBackPressed() {
        handler=null;
        finish();
    }
    public void getList(){
        Cursor cursor=MainActivity.db.rawQuery("select * from datasheet",null);
         /*기존 arraylist 모두 삭제*/
        list.removeAll(list);
        while (cursor.moveToNext()){
            /*int i=0;*/
            double hum1=cursor.getDouble(cursor.getColumnIndex("humidity1"));
            String time=cursor.getString(cursor.getColumnIndex("regdate"));
            Bluetooth_DataDTO dto=new Bluetooth_DataDTO();
            dto.setHumidity1(hum1);
            dto.setRegdate(time);
            list.add(dto);
        }

        for(int i=list.size()-1;i>=0;i--){
            Bluetooth_DataDTO dto=list.get(i);
            ChartDTO chartDTO=new ChartDTO();
            if(i==0){
                start=dto.getHumidity1();
            }else if(i==list.size()-1){
                end=dto.getHumidity1();
            }
            if(dto.getHumidity1()>=40){
                chartDTO.setEtc("많이 젖음");
            }else if(dto.getHumidity1()>=25&&dto.getHumidity1()<40){
                chartDTO.setEtc("조금 젖음");
            }else if(dto.getHumidity1()>=4&&dto.getHumidity1()<25){
                chartDTO.setEtc("조금 건조");
            }else if(dto.getHumidity1()>=0&&dto.getHumidity1()<4) {
                chartDTO.setEtc("완전 건조");
                Intent intent=new Intent(this,NotificationService.class);
                startService(intent);
            }
            chartDTO.setHumidity(dto.getHumidity1());
            chartDTO.setProgressTime(dto.getRegdate());
            chartList.add(chartDTO);
        }

        double rate=Math.pow(end/start,1.0/(list.size()-1.0));
        Log.d(TAG,"end"+end+"start"+start+"rate"+rate);
        int remainTime=0;
        while(true){
            Log.d(TAG,""+remainTime++);
            if((end*=rate)<4||remainTime>100){
                break;
            }

        };
        txt_predict.setText((remainTime-1)+"시간");
    }
}
