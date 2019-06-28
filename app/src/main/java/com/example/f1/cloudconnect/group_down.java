package com.example.f1.cloudconnect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class group_down extends AsyncTask<ArrayList<String >,Integer,ArrayList<Uri>> {
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder,zBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    ArrayList<Uri> files;
    String dwld_link;
    DBHelper dbsql;
    int current;
    boolean login;
    boolean status;
    FileOutputStream fos;
    int progress=0;
    int size=100;
    File output;
    String password;
    String admin;
    public group_down(Context context,String admn,String pass)
    {
        mContext=context;
        this.admin=admn;
        this.password=pass;
    }
    public void notifier(ArrayList<String> arrayLists)
    {

        size=arrayLists.size();
        mBuilder = new NotificationCompat.Builder(mContext);
        zBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Downloading")
                .setContentText(arrayLists.size()+" files")
                .setAutoCancel(false)
                .setOngoing(true)
                .setProgress(arrayLists.size(),progress,true);

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
    protected ArrayList<Uri> doInBackground(ArrayList<String>... arrayLists) {
        SharedPreferences preferences = mContext.getSharedPreferences("Themes", 0);
        String gate=preferences.getString("CurrentKey","null");
        dbsql=new DBHelper(mContext);
        String g=dbsql.getGateway(gate);
        notifier(arrayLists[0]);
        files=new ArrayList<>();
        FTPClient client = null;
        if (client == null) {
            client = new FTPClient();
            try {
                client.connect(g);
                login = client.login(admin, password);
                client.setFileType(FTP.BINARY_FILE_TYPE);

            } catch (IOException e) {

                e.printStackTrace();
            }

        }


        // If login is true notify user

        if (login) {

            Log.i("NAME1","Connection established...");



        } else {
            Log.i("NAME1","Connection failed...");
        }
        fos=null;
        current=1;
        for(String link: arrayLists[0])
        {
            try {
                client.changeWorkingDirectory(extract(link));
                fos = new FileOutputStream("/storage/emulated/0/"+popper(link));
                Log.i("NAME1",popper(link));
                status=client.retrieveFile(popper(link), fos);
                if(status) {
                    publishProgress(current);
                    current = current + 1;
                }
                Log.d("DOWN", "DOWNLOADING"+link);
                files.add(Uri.parse("/storage/emulated/0/"+popper(link)));
                Log.d("FILES"," "+files.size());
                if(fos!=null)
                    fos.close();

            } catch (IOException e) {
                e.printStackTrace(); Log.i("NAME1","ddddd");
            }

        }
        return files;
    }
    @Override
    protected void onPostExecute(ArrayList<Uri> aVoid) {
        mNotificationManager.cancel(0);

        zBuilder.setContentTitle("Download Completed")
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentInfo(size+" files have been downloaded");
        if(current<size)
            zBuilder.setContentText(size-current+" files failed to download");
        mNotificationManager.notify(1,zBuilder.build());
        super.onPostExecute(aVoid);

        Log.d("new", "1over");
        try {
            if(fos!=null)
                 fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        mBuilder.setProgress(size,values[0],false)
                .setOngoing(true);
        mNotificationManager.notify(0,mBuilder.build());
        super.onProgressUpdate(values);
    }

    String popper(String str)
    {
        int len=str.length();
        int new_len = 0;
        for(int i=len-1;str.charAt(i)!='/';i--)
        {
            new_len=i;
        }
        return str.substring(new_len,len);
    }
    String extract(String str)
    {
        int len=str.length();
        int new_len = 0;
        for(int i=len-1;str.charAt(i)!='/';i--)
        {
            new_len=i;
        }
        return str.substring(0, new_len-1);
    }
}
