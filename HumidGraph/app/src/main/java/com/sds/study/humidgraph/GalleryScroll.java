package com.sds.study.humidgraph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryScroll extends AppCompatActivity {

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
        setContentView(R.layout.gallery_main);

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

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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

    }
}