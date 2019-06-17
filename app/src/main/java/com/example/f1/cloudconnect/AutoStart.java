package com.example.f1.cloudconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class AutoStart extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context,bg_updater_service.class);
        Log.d("RECIEVER","RECIEVD");
        context.startForegroundService(i);

    }
}
