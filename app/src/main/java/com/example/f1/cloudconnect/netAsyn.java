package com.example.f1.cloudconnect;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

public class netAsyn extends AsyncTask<String,String,FTPFile[]> {
    FTPClient client = null;
    FTPFile[] dir = null;
    String choice;

    @Override
    protected FTPFile[] doInBackground(String... strings) {

        if (client == null) {
            client = new FTPClient();
            try {
                client.connect("192.168.0.1");
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

        boolean login = false;
        try {
            login = client.login("admin1", "2d5dg5yn");
            Log.d("new", "Connection established...");
            Log.d("new", client.printWorkingDirectory());

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
    protected void onCancelled() {
        Log.d("new", "over11");
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(FTPFile[] ftpFiles) {
        Log.d("new", "over");
    }

   }