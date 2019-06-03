package com.example.f1.cloudconnect;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class bg_updater_service extends Service {
    ArrayList<String> lis;
    int len;
    ArrayList<RecursiveFileObserver> fileObserver;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fileObserver=new ArrayList<>();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        DBHelper dbsql=new DBHelper(this);
        lis=dbsql.getAllDirectories();
        len=lis.size();
        for(int i=0;i<len;i++) {
            fileObserver.add(new RecursiveFileObserver(lis.get(i) + "/", this));
            }
        for(RecursiveFileObserver m:fileObserver)
            m.startWatching();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(RecursiveFileObserver m:fileObserver)
            m.stopWatching();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}

