package com.sds.study.humidgraph;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lee on 2016-12-20.
 */

public class Weather extends AppCompatActivity{

    String TAG;
    TextView textView;
    Document doc = null;
    TextView w_time;
    ImageView w_weater;
    TextView w_temp;
    TextView w_humid;
    TextView w_wind;
    int time;
    int pty;
    String temp;
    String humid;
    String wind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        TAG = this.getClass().getName();

        w_time = (TextView)findViewById(R.id.w_time);
        w_weater = (ImageView)findViewById(R.id.w_weather);
        w_temp = (TextView)findViewById(R.id.w_temp);
        w_humid = (TextView)findViewById(R.id.w_humid);
        w_wind=(TextView)findViewById(R.id.w_wind);
        textView = (TextView)findViewById(R.id.weathertextview);

        GetXMLTask task = new GetXMLTask();
        task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=61&gridy=125");

    }

   /* public void onClick(View view){
        GetXMLTask task = new GetXMLTask();
        task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=61&gridy=125");
        weatherpars();
    }*/



    //private inner class extending AsyncTask
    private class GetXMLTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            Node fistnode = nodeList.item(0);
            Element fistElement = (Element)fistnode;

            NodeList onetime = fistElement.getElementsByTagName("hour");
            time = Integer.parseInt(onetime.item(0).getChildNodes().item(0).getNodeValue());

            NodeList onepty = fistElement.getElementsByTagName("pty");
            pty = Integer.parseInt( onepty.item(0).getChildNodes().item(0).getNodeValue());

            NodeList onetemp  = fistElement.getElementsByTagName("temp");
            temp = onetemp.item(0).getChildNodes().item(0).getNodeValue();

            NodeList onehumid  = fistElement.getElementsByTagName("reh");
            humid = onehumid.item(0).getChildNodes().item(0).getNodeValue();

            NodeList onewind  = fistElement.getElementsByTagName("ws");
            wind = onetemp.item(0).getChildNodes().item(0).getNodeValue();


            for(int i = 1; i< 8; i++){
                s += "-------------------------------------------------------------";
                //날씨 데이터를 추출
                //s += "" +i + ": 날씨 정보: ";
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;

                NodeList hourList = fstElmnt.getElementsByTagName("hour");
                s += "시간 = "+  hourList.item(0).getChildNodes().item(0).getNodeValue()+" ,";

                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");
                s += "날씨 = "+  websiteList.item(0).getChildNodes().item(0).getNodeValue()+" ,";

                NodeList nameList  = fstElmnt.getElementsByTagName("temp");
                s += "온도 = "+  nameList.item(0).getChildNodes().item(0).getNodeValue()+" ,";

                NodeList rehList = fstElmnt.getElementsByTagName("reh");
                s += "습도 = "+  rehList.item(0).getChildNodes().item(0).getNodeValue() +" ,";

                NodeList wsList = fstElmnt.getElementsByTagName("ws");
                s += "풍속 = "+  wsList.item(0).getChildNodes().item(0).getNodeValue().substring(0,3) +"\n";

            }
            s += "-------------------------------------------------------------";
            weatherpars();
            textView.setText(s);

            super.onPostExecute(doc);
        }


    }//end inner class - GetXMLTask

    public void weatherpars(){
        w_time.setText("["+(time-3)+"H ~ "+time+"H]");
        w_temp.setText(temp+"ºC");
        w_humid.setText(humid+"%");
        w_wind.setText(wind+"m/s");

        switch (pty) {
            case 0:w_weater.setImageResource(R.drawable.w_sun);break;
            case 1:w_weater.setImageResource(R.drawable.w_rain);break;
            case 2:w_weater.setImageResource(R.drawable.w_rainsnow);break;
            case 3:w_weater.setImageResource(R.drawable.w_snow);break;

        }

    }
}
