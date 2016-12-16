package com.sds.study.humidgraph;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String TAG;
    MyHelper helper;/*데이터 베이스 구축*/
    static SQLiteDatabase db;/*데이터 베이스 쿼리문 제어*/
    TextView[] h,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initDB();


        //갯수 추가를 위해서는
        //dataset에 있는 xySeries의 수와 renderer에 있는 xySeriesRender의 수가 맞아야함

        //int[] data = {25,30,35,23,24,35,32,30,27,38,24};
        final int[] data = {40,35,27,23,22,20,15,10,5,3,1};

        h=new TextView[9];
        state=new TextView[9];

        for(int i=1;i<9;i++) {
            h[i] = (TextView) findViewById(getResources().getIdentifier("h"+i,"id",this.getPackageName()));
            state[i] = (TextView) findViewById(getResources().getIdentifier("state"+i,"id",this.getPackageName()));
           /* TextView h2 = (TextView) findViewById(R.id.h2);
            TextView h3 = (TextView) findViewById(R.id.h3);
            TextView h4 = (TextView) findViewById(R.id.h4);
            TextView h5 = (TextView) findViewById(R.id.h5);
            TextView h6 = (TextView) findViewById(R.id.h6);
            TextView h7 = (TextView) findViewById(R.id.h7);*/
            h[i].setText(Integer.toString(data[i-1]));

            if(data[i-1]>=40){
                state[i].setText("많이 젖음");
            }else if(data[i-1]>=25&&data[i-1]<40){
                state[i].setText("조금 젖음");
            }else if(data[i-1]>=20&&data[i-1]<25){
                state[i].setText("조금 건조");
            }else if(data[i-1]>=0&&data[i-1]<20){
                state[i].setText("완전 건조");
            }
        }




          /*  h[i].setText(Integer.toString(data[0]));
            h2.setText(Integer.toString(data[1]));
            h3.setText(Integer.toString(data[2]));
            h4.setText(Integer.toString(data[3]));
            h5.setText(Integer.toString(data[4]));
            h6.setText(Integer.toString(data[5]));
            h7.setText(Integer.toString(data[6]));*/





        //그래프 생성
        final GraphicalView chart = ChartFactory.getLineChartView(this, getDataset(data), getRenderer());
        //레이아웃에 추가
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.addView(chart);

    }

    public MainActivity() {


    }

    //그래프 설정 모음
    // http://www.programkr.com/blog/MQDN0ADMwYT3.html ( 그래프 설정 속성 한글로 써져있는 사이트 )
    private void setChartSettings(XYMultipleSeriesRenderer renderer) {
        //타이틀, x,y축 글자
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
        renderer.setXLabels(5);
        renderer.setYLabels(5);

        //x축 최대 최소(화면에 보여질)
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(10);
        //y축 최대 최소(화면에 보여질)
        renderer.setYAxisMin(10);
        renderer.setYAxisMax(60);

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
        *
        */


        setChartSettings(renderer);
        return renderer;
    }

    //데이터들
    private XYMultipleSeriesDataset getDataset( int[] data ) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();


        XYSeries series = new XYSeries("습도 변화량");
        for (int i = 0; i < data.length; i++ ) {
            series.add(i*2, data[i] );
        }

        /*
        *
        * 다른 그래프를 추가하고 싶으면
        * XYSeries를 추가로 생성한 후
        * dataset.addSeries(series) 해준다 (renderer도 있어야함)
        *
        */


        dataset.addSeries(series);


        return dataset;
    }

    public void initDB(){//sqlite초기화
        helper=new MyHelper(this,"iot.sqlite",null,1);
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from humidair",null);
        while (cursor.moveToNext()){
            int temp=cursor.getInt(cursor.getColumnIndex("temp"));
            float weight=cursor.getFloat(cursor.getColumnIndex("weight"));
            Log.d(TAG,"temp"+temp+"weight"+weight);
        }
    }

}
