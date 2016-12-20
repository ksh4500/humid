package com.sds.study.humidgraph;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String TAG;
    MyHelper helper;/*데이터 베이스 구축*/
    static SQLiteDatabase db;/*데이터 베이스 쿼리문 제어*/
    GalleryAdapter adapter;

    int img[]={
            R.drawable.t1,
            R.drawable.t2,
            R.drawable.t3,
            R.drawable.t4,
            R.drawable.t5,
            R.drawable.t6,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();

        //drawable 있는 이미지를 galleryList에 추가하는 작업

        adapter = new GalleryAdapter(this);


        for(int i = 1; i<6; i++){
            adapter.addItem(getResources().getIdentifier("g"+i, "drawable",this.getPackageName()));
        }

        final ImageView iv = (ImageView)findViewById(R.id.img01);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"눌렀어?",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(getApplicationContext(),DetailActivity.class);
                startActivity(intent);
            }
        });

        Gallery gallery = (Gallery)findViewById(R.id.gallery01);

        gallery.setAdapter(adapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
               // Integer galleryItem = adapter.getGalleryItem(position);
              //  iv.setImageResource(galleryItem);
                iv.setImageResource(img[position]);
            }
        });
        ImageView bluetooth=(ImageView)findViewById(R.id.bluetooth);

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"블루투스 리스트 화면으로 이동",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Bluetooth_MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView weather=(ImageView)findViewById(R.id.weather);

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"날씨 리스트 화면으로 이동",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Weather.class);
                startActivity(intent);
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
        System.runFinalizersOnExit(true);
        System.exit(0);

    }
}
