package com.example.f1.cloudconnect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class direc_operations extends AsyncTask<ArrayList<String>,Void,Void> {
    String password;
    String admin;
    Context mContext;
    public direc_operations(Context context, String admn, String pass)
    {
        mContext=context;
        this.admin=admn;
        this.password=pass;
    }
    @Override
    protected Void doInBackground(ArrayList<String>... strings) {

        FTPClient client=null;
        if(client==null)
        {
            client = new FTPClient();
            try {
                client.connect("192.168.0.1");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        boolean login = false;
        try {
            login = client.login(admin, password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If login is true notify user

        if (login) {

            System.out.println("Connection established...");

            // Try to logout and return the respective boolean value

        } else {
            System.out.println("Connection fail...");
        }

        boolean as= false;
        try {
            for(String s:strings[0]) {
                Log.d("DELETE ",s);
                as = client.deleteFile(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(as);
        return null;
    }

}
