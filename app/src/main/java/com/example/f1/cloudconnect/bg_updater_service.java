package com.example.f1.cloudconnect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class bg_updater_service extends Service {
    ArrayList<String> lis;
    int len;
    String admin;
    String pass;
    SharedPreferences preferences;
    ArrayList<RecursiveFileObserver> fileObserver;
    PendingIntent pending;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {

        Intent stopSelf = new Intent(this, AutoUpload.class);

        PendingIntent pStopSelf = PendingIntent.getActivity(this,0,stopSelf,PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel("12312", "Service", NotificationManager.IMPORTANCE_NONE);

            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder notificationbuild = new NotificationCompat.Builder(this,"12312");
        Notification notification=   notificationbuild.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Checking for files")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .addAction(R.mipmap.ic_launcher,"Manage",pStopSelf)
                .build();


        startForeground(1337, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBHelper dbsql=new DBHelper(this);
        preferences=this.getSharedPreferences("Themes",0);
        Log.d("SERVICE","IN SERVICE");
        admin=dbsql.getAdmin(preferences.getString("CurrentKey",null));
        pass=dbsql.getPassword(preferences.getString("CurrentKey",null));
        fileObserver=new ArrayList<>();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        lis=dbsql.getAllDirectories();
        len=lis.size();
        for(int i=0;i<len;i++) {
            fileObserver.add(new RecursiveFileObserver(lis.get(i) + "/", this,admin,pass));
            }
        for(RecursiveFileObserver m:fileObserver)
            m.startWatching();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fileObserver!=null) {
            for (RecursiveFileObserver m : fileObserver)
                m.stopWatching();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}

