package com.example.f1.cloudconnect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.IOException;

public class netAsyn extends AsyncTask<String,String,FTPFile[]> {
    public interface results
    {
        void getFiles(FTPFile[] file,int connectionstat);
    }
    results delegate=null;
    FTPClient client = null;

    FTPFile[] dir = null;
    Activity activity;
    String choice;
    String gate;
    int connection=0;
    Boolean connectstat;
    String password;
    String admin;
    Context mContext;
    DBHelper dbsql;
    public netAsyn(String admn,String pass,String gateway,Context context,results act)
    {
        this.mContext=context;
        this.admin=admn;
        delegate=act;
        this.password=pass;
        this.gate=gateway;

    }
    @Override
    protected FTPFile[] doInBackground(String... strings) {
        SharedPreferences preferences = mContext.getSharedPreferences("Themes", 0);
        String gate=preferences.getString("CurrentKey","null");
        dbsql=new DBHelper(mContext);
        String g=dbsql.getGateway(gate);
        Log.d("GATE",g);
        FTPFile f=new FTPFile();
        connection=1;
        Log.d("FTP","in do");

        if (client == null) {
            Log.d("FTP","null");
            client = new FTPClient();
            client.setControlKeepAliveTimeout(10);
            client.setConnectTimeout(10000);
            try {
                client.connect(g);
                connection=1;
                Log.d("new", "Connection established...");


            } catch (IOException e) {
                connection=0;
                e.printStackTrace();
                try {
                    client.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Log.d("new", "Connection failed...");
                return null;
            }

        }

        boolean login = false;
        try {
            login = client.login(admin, password);
            if(login) {
                connection = 1;
                Log.d("new", "Connection established...");
            }
            else
            {
                connection=0;
                client.disconnect();

                Log.d("new", "Connection failed...");

            }

            if (isCancelled()) { Log.d("new", "over");
                Log.d("new", "over");

                return null;
            }
            client.changeWorkingDirectory(strings[0]);
            dir = client.listFiles();

            for (FTPFile file : dir) {
                Log.d("new", file.getName() + "     " + file.getLink());
            }



            if(strings[0].equals("__BACK__"))
            {
                client.changeToParentDirectory();
                dir = client.listFiles();
                client.disconnect();
                for (FTPFile file : dir) {
                    Log.d("new1", file.getName() + "     " + file.getLink());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dir;
    }


    @Override
    protected void onPostExecute(FTPFile[] ftpFiles)
    {
        super.onPostExecute(ftpFiles);
        delegate.getFiles(dir,connection);

        Log.d("new", "over");
    }

   }