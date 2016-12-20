package com.sds.study.humidgraph;

/**
 * Created by 석환 on 2016-12-20.
 */


import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Bluetooth_DataThread extends Thread {
    Bluetooth_MainActivity bluetooth_mainActivity;
    BluetoothSocket socket;
    BufferedReader buffr;
    BufferedWriter buffw;
    boolean flag = true;
    String TAG;

    public Bluetooth_DataThread(Bluetooth_MainActivity bluetooth_mainActivity, BluetoothSocket socket) {
        this.bluetooth_mainActivity = bluetooth_mainActivity;
        this.socket = socket;
        TAG = getClass().getName();
        try {
            buffr = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //듣기
    public void listen() {
        String msg = null;
        try {
            msg = buffr.readLine();
            Log.d(TAG, msg);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            message.setData(bundle);
            bluetooth_mainActivity.handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //말하기
    public void send(String msg) {
        try {
            buffw.write(msg);
            buffw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (flag) {
            listen();
        }
    }

}


