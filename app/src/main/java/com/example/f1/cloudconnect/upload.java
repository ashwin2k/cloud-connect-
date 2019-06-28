package com.example.f1.cloudconnect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;

public class upload extends AsyncTask<stream_details,Integer,Void> {
    private Context mContext;
    int size=100;
    int progress=0;
    String status="failed";
    String notify_name;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder,zBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    String password;
    String upload;
    String admin;
    DBHelper dbsql;
    public upload(Context context,String admn,String pass,String uploadkey)
    {
        this.upload=uploadkey;
        mContext=context;
        this.admin=admn;
        this.password=pass;
    }
    public void notifier(stream_details[] stream_details)
    {

        size=stream_details.length;
        mBuilder = new NotificationCompat.Builder(mContext);
        zBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Uploading")
                .setContentText(size+" files")
                .setAutoCancel(false)
                .setOngoing(true)
                .setProgress(size,progress,true);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Downloads", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            zBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 , mBuilder.build());


    }
    @Override
    protected Void doInBackground(stream_details... details) {
        SharedPreferences preferences = mContext.getSharedPreferences("Themes", 0);
        String gate=upload;
        dbsql=new DBHelper(mContext);
        String g=dbsql.getGateway(gate);
        notifier(details);

        FTPClient client=null;
        if(client==null)
        {
            client = new FTPClient();

            try {
                client.connect(g);
            } catch (IOException e) {
                status="failed";
                dbsql.addUpload(details[0].getFile_name(),details[0].optional_link,details[0].getFile_name(),details[0].getLocation());
                e.printStackTrace();
            }

        }
        boolean login = false;
        try {
            login = client.login(admin, password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            status="failed";
            e.printStackTrace();
            dbsql.addUpload(details[0].getFile_name(),details[0].optional_link,details[0].getFile_name(),details[0].getLocation());        }

        // If login is true notify user

        if (login) {

            Log.d("U1P","LOGGED IN");

            // Try to logout and return the respective boolean value

        } else {
            System.out.println("Connection fail...");
            status="failed";
            dbsql.addUpload(details[0].getFile_name(),details[0].optional_link,details[0].getFile_name(),details[0].getLocation());        }

             String name=details[0].getLocation()+'/'+details[0].getFile_name();


        try {
            boolean res= client.storeFile(name,details[0].getInputStream());
            int current=0;
            if(!res)
            {
                status="failed";
                Log.d("U1P","PROBLEM UPLOADING AT "+name);
                dbsql.addUpload(details[0].getFile_name(),details[0].optional_link,details[0].getFile_name(),details[0].getLocation());            }
            else {
                status="complete";
                Log.d("U1P", "UPLOADING AT " + name);
            }
            publishProgress(current);
            current=current+1;
        } catch (IOException e) {
            status="failed";
            e.printStackTrace();
            dbsql.addUpload(details[0].getFile_name(),details[0].optional_link,details[0].getFile_name(),details[0].getLocation());            Log.d("U1P","foot");
        }
        finally {

            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mBuilder.setProgress(size,values[0],false)
                .setOngoing(true);
        mNotificationManager.notify(0,mBuilder.build());
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Void aVoid) {

        zBuilder.setContentTitle("Upload "+status)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentInfo(size+" files have been uploaded");
        mNotificationManager.notify(1,zBuilder.build());
        super.onPostExecute(aVoid);

        Log.d("new", "1over");

    }
}
