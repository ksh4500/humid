package com.sds.study.humidgraph;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    TextView textView;
    Document doc = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        textView = (TextView)findViewById(R.id.weathertextview);
    }

    public void onClick(View view){
        GetXMLTask task = new GetXMLTask();
        task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=61&gridy=125");

    }



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

            for(int i = 0; i< nodeList.getLength(); i++){

                //날씨 데이터를 추출
                s += "" +i + ": 날씨 정보: ";
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                NodeList nameList  = fstElmnt.getElementsByTagName("temp");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                s += "온도 = "+ ((Node) nameList.item(0)).getNodeValue() +" ,";


                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");
                //<wfKor>맑음</wfKor> =====> <wfKor> 태그의 첫번째 자식노드는 TextNode 이고 TextNode의 값은 맑음
                s += "날씨 = "+  websiteList.item(0).getChildNodes().item(0).getNodeValue() +"\n";


            }

            textView.setText(s);

            super.onPostExecute(doc);
        }


    }//end inner class - GetXMLTask
}
