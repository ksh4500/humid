package com.sds.study.humidgraph;

/**
 * Created by 석환 on 2016-12-20.
 */


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.chainsaw.Main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Bluetooth_MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final int REQUEST_ENABLE_BLUETOOTH = 1;
    static final int REQUEST_ACCESS_PERMISSION = 2;
    BluetoothAdapter bluetoothAdapter;
    String TAG;
    //시스템이 앱들에게 인텐트를 방송할때, 그 방송을 받을 수 있는 컴포넌트
    BroadcastReceiver receiver;
    ListView listView;

    Bluetooth_ListAdapter listAdapter;
    String UUID = "00001101-0000-1000-8000-00805f9b34fb";
    BluetoothSocket socket;
    Thread connectThread;
    Handler handler;
    Bluetooth_DataThread dataThread;

    String mac;
    String devicename;
    TextView currentdevice;
    TextView currentmac;
    MyHelper helper;/*데이터 베이스 구축*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
        setContentView(R.layout.bluetooth_activity);
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new Bluetooth_ListAdapter(this);
        listView.setAdapter(listAdapter);//리스트뷰와 어댑터 연결
        listView.setOnItemClickListener(this);
        checkSupportDevice();
        checkEnableBluetooth();
        currentdevice = (TextView) findViewById(R.id.currentdevice);
        currentmac = (TextView) findViewById(R.id.currentmac);

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //필터링 하고자 하는 바로 그 액션이라면...
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Toast.makeText(MainActivity.this , bluetoothDevice.getUuids()+"발견했어!!", Toast.LENGTH_SHORT).show()
                    listAdapter.list.add(bluetoothDevice);
                    listAdapter.notifyDataSetChanged(); //갱신
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter); //리시버 등록!!


        handler = new Handler() {
            public void handleMessage(Message message) {
                currentdevice.setText( devicename);
               currentmac.setText(mac);
                String msg = message.getData().getString("msg");
                //Log.d(TAG, msg);
                String[] datas=msg.split(",");
                int[] tdata=new int[3];
                int[] hdata=new int[3];
                hdata[0]=Integer.parseInt(datas[0]);
                tdata[0]=Integer.parseInt(datas[1]);
                hdata[1]=Integer.parseInt(datas[2]);
                tdata[1]=Integer.parseInt(datas[3]);
                hdata[2]=Integer.parseInt(datas[4]);
                tdata[2]=Integer.parseInt(datas[5]);

                String[] weight=new String[3];
                for(int i=0;i<tdata.length;i++) {
                    Cursor cursor=MainActivity.db.rawQuery("select * from humidair where temp=?",new String[]{Integer.toString(tdata[i])});
                    if(cursor.moveToNext()){
                        weight[i]=Double.toString(cursor.getFloat(cursor.getColumnIndex("weight"))*hdata[i]);//상대습도->절대습도
                    }
                }
                String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

                StringBuffer sql = new StringBuffer();
                sql.append("insert into datasheet(MacAddress,humidity1,temperature1,regdate) ");
                sql.append("values(?,?,?,?)");

                MainActivity.db.execSQL(sql.toString(), new String[]{mac,
                        weight[0], Integer.toString(tdata[0]),
                        time});

                Log.d(TAG,"입력성공");
              /*
                Cursor cursor=db.rawQuery("select * from datasheet",null);
                while (cursor.moveToNext()){
                    int hum1=cursor.getInt(cursor.getColumnIndex("humidity1"));
                    int temper1=cursor.getInt(cursor.getColumnIndex("temperature1"));
                    int hum2=cursor.getInt(cursor.getColumnIndex("humidity2"));
                    int temper2=cursor.getInt(cursor.getColumnIndex("temperature2"));
                    int hum3=cursor.getInt(cursor.getColumnIndex("humidity3"));
                    int temper3=cursor.getInt(cursor.getColumnIndex("temperature3"));

                }
*/
                //시간 형식 yyyy/mm/dd/xx:xx:xx
              //  txt_data.setText("습도1는 " + hdata1 + " %  온도1는 " + tdata1 + "습도2는" + hdata2 + "온도2는" + tdata2 + "습도3는" + hdata3 + "온도3는" + tdata3 + "시간은?" + time + "macaddress= " + mac);

            }
        };
    }

    /*---------------------------------------------------------
        디바이스가 블루투스 지원하는지 여부 체크
        ---------------------------------------------------------*/
    public void checkSupportDevice() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showAlert("안내", "이 디바이스는 블루투스를 지원하지 않습니다");
            finish();
            return;
        }
    }

    /*---------------------------------------------------------
       활성화가 안되어 있다면, 활성화하도록 요청
       ---------------------------------------------------------*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == RESULT_CANCELED) {
                    showAlert("경고", "블루투스를 켜셔야 합니다");
                }
                break;
        }
    }

    public void checkEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    /*---------------------------------------------------------
    현재 우리 앱이 권한이 취득되어 있는지 체크
    ACCESS_COARSE_LOCATION
    ---------------------------------------------------------*/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_PERMISSION:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showAlert("경고", "권한을 주셔야 사용이 가능합니다.");
                }
                break;
        }
    }

    public void checkAccessPermission() {
        int accessPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (accessPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_ACCESS_PERMISSION);
        } else {
            //검색 시작...
            scanDevice();
        }
    }


    /*---------------------------------------------------------
    기기 검색
    브로드 케스트 리시버 등록과 동시에 검색 시작!!
     ACTION_FOUND 인텐트를 받겠다고,,필터로 등록..
    ---------------------------------------------------------*/
    public void scanDevice() {
        bluetoothAdapter.startDiscovery(); //검색시작!!
    }


    /*---------------------------------------------------------
     검색된 기기 목록을 통해 접속을 시도...
     주의!! - 현재 진행중인 검색은 종료해야 한다... cancelDiscovery()...
     BluetoothSocket 필요..., UUID
     ---------------------------------------------------------*/
    public void connectDeivce(final BluetoothDevice device) {
        bluetoothAdapter.cancelDiscovery(); //검색종료

        //Toast.makeText(this, device.getName() + "을 접속할까요?", Toast.LENGTH_SHORT).show();

        try {
            socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //쓰레드를 이용하여 접속 시도!!
        connectThread = new Thread() {
            public void run() {
                try {
                    socket.connect(); //접속 시도!!


                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "접속 성공");
                    message.setData(bundle);



                    //  handler.sendMessage(message);

                    //소켓으로부터 스트림 뽑아서 데이터 주고 받자...
                    dataThread = new Bluetooth_DataThread(Bluetooth_MainActivity.this, socket);
                    dataThread.start();


                } catch (IOException e) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "접속 실패");
                    message.setData(bundle);

                    handler.sendMessage(message);

                    e.printStackTrace();
                }
            }
        };

        connectThread.start();
    }


    /*---------------------------------------------------------
   메세지 창 띄우기
   ---------------------------------------------------------*/
    public void showAlert(String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title).setMessage(msg).show();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.bt_scan:
                checkAccessPermission();
                break;


        }
    }

    /*Cursor cursor=db.rawQuery("select * from humidair",null);
        while (cursor.moveToNext()){
            int temp=cursor.getInt(cursor.getColumnIndex("temp"));
            float weight=cursor.getFloat(cursor.getColumnIndex("weight"));
            Log.d(TAG,"temp"+temp+"weight"+weight);
        }*/
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        //선택한 아이템의 address를 추출하여, 그 아이템이 몇번째
        //아이템인지 조사한 후, 같은 index에 있는 Device를 추출!!
        TextView txt_address = (TextView) view.findViewById(R.id.txt_address);
        String address = txt_address.getText().toString();

        for (int i = 0; i < listAdapter.list.size(); i++) {
            BluetoothDevice device = listAdapter.list.get(i);

            if (device.getAddress().equals(address)) {
                devicename=device.getName();
                mac = device.getAddress();
                connectDeivce(device); //발견과 동시에 넘기기!!

                break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}