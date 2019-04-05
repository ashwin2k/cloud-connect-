package com.example.f1.cloudconnect;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;


public class downup extends AsyncTask<String,Void,Void> {
    String dwld_link;
    boolean login;
    FileOutputStream fos;
    File output;

    @Override
    protected Void doInBackground(String... strings) {
        FTPClient client = null;
        if (client == null) {
            client = new FTPClient();
            try {
                client.connect("192.168.0.1");
                login = client.login("admin1", "2d5dg5yn");
            } catch (IOException e) {

                e.printStackTrace();
            }

        }


        // If login is true notify user

        if (login) {

            System.out.println("Connection established...");


        } else {
            System.out.println("Connection fail...");
        }
        dwld_link = strings[0];
        fos = null;

        try {

            fos = new FileOutputStream("/storage/emulated/0/"+popper(strings[0]));
          Log.i("NAME1","dd42ddd");
            client.retrieveFile(dwld_link, fos);
            System.out.println("downloading 	...");


            if(fos!=null)
                 fos.close();
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace(); Log.i("NAME1","ddddd");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Log.d("new", "1over");
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
