package com.sds.study.humidgraph;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by song_kang on 2016-12-13.
 */

public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("service","onCreate 실행");
    }

    @Override
    public void onDestroy() {
        Log.d("service","onDestroy 실행");
        mRunning = false;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this);
            builder.setSmallIcon(R.drawable.hanger);
            builder.setTicker("Notification");
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.hanger));
            builder.setContentTitle("세탁물건조알리미");
            builder.setContentText("빨래가 다 말랐습니다.\n 환풍기 가동이 중지됩니다.");
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setAutoCancel(true);

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);

            stackBuilder.addParentStack(MainActivity.class);


            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0, PendingIntent.FLAG_UPDATE_CURRENT
                    );

            builder.setContentIntent(resultPendingIntent);

            Notification notification = builder.build();
            manager.notify(1, notification);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }
    };

    protected boolean mRunning;

    // 제일 중요한 메서드! (서비스 작동내용을 넣어준다.)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","onStartCommand 실행");
        //final int time = intent.getIntExtra("time", 0);
        //Toast.makeText(this, "안녕~ 난 서비스 : "+time, 0).show();
        //handler 통한 Thread 이용
        new Thread(new Runnable() {
            @Override
            public void run() {
                mRunning = true;
                while(mRunning) {
                    //SystemClock.sleep(1000);
                    mHandler.sendEmptyMessage(0);
                    mRunning = false;
                }
            }
        }).start();

        return START_STICKY_COMPATIBILITY;
    }
}
