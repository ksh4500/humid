package com.sds.study.humidgraph;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 안드로이드에서는 sqlite데이터베이스의 위치가 이미 data/data/앱페키지/databases로 정해져 있기 때문에
 * 오직 SQLiteOpenHelper라는 클래스를 통해 접근 및 제어해야 한다.
 * (즉, 임의로 개발자가 디렉토리에 접근 불가
 * String name에는 생성할 db파일명
 * int version에는 최초의 버전 넘버 3
 */

public class MyHelper extends SQLiteOpenHelper {
    String TAG;
    Context context;
    public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
        TAG=this.getClass().getName();
    }
    //데이터 베이스가 최초에 생성될때 호출....... 즉 파일이 존재하지 않을 때 이 메서드 호출
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG,"onCreate호출");
        //어플리케이션에 필요한 테이블은 이시점에 구축하자
        StringBuffer sql=new StringBuffer();
        sql.append("create table humidair(");
        sql.append("temp integer,");
        sql.append("weight real");
        sql.append(");");
        sqLiteDatabase.execSQL(sql.toString());
        Log.d(TAG,"Database 구축");
        /*sql.setLength(0);
        sql.append("create table IF NOT EXISTS humidair(");
        sql.append("temp integer,");
        sql.append("weight real");
        sql.append(");");
        sqLiteDatabase.execSQL(sql.toString());*/
        regist(sqLiteDatabase);
    }
    public void regist(SQLiteDatabase db){
        /*이앱이 보유한 sqlite파일에 인서트 하면 된다.*/
        Log.d(TAG,"regist");
        try{
            AssetManager am=context.getAssets();
            InputStream is=am.open("chartForHumidAir.xls");
            Log.d(TAG,"is");
            Workbook wb=Workbook.getWorkbook(is);
            Log.d(TAG,"wb");
            Sheet sheet=wb.getSheet(0);
            Log.d(TAG,"sheet");
            int row= sheet.getRows();
            String data="";
            StringBuffer sql=new StringBuffer();
            int count=0;
            for(int i=1;i<row;i++){
                sql.setLength(0);
                sql.append("insert into humidair(temp,weight) ");
                sql.append("values(?,?)");
                Log.d(TAG,sheet.getCell(0,i).getContents()+","+sheet.getCell(1,i).getContents());
                db.execSQL(sql.toString(),new String[]{sheet.getCell(0,i).getContents(),sheet.getCell(1,i).getContents()});
                count++;
            }
            Log.d(TAG,"인서트 문 수행 완료");
            Log.d(TAG,"인서트 숫자"+count);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //데이터 베이스 파일이 이미 있고, 버전 숫자가 틀린 경우 개발자가 무언가를 db에 변경하겠다는 의도로 아래의 메서드 호출
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG,"onUgrade호출");
    }
}
