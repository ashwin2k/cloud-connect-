package com.example.f1.cloudconnect;

import android.content.Context;
import android.content.SharedPreferences;
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
    String code;
    Context mContext;
    String d;
    boolean m;
    String url;
    String fname="New Directory";
    DBHelper dbsql;
    public direc_operations(Context context, String admn, String pass,String code)
    {
        mContext=context;
        this.admin=admn;
        this.password=pass;
        this.code=code;
    }
    public void setFilename(String name)
    {
        this.fname=name;
    }
    public void setDir(String url)
    {
        this.url=url;
    }
    @Override
    protected Void doInBackground(ArrayList<String>... strings) {
        SharedPreferences preferences = mContext.getSharedPreferences("Themes", 0);
        String gate=preferences.getString("CurrentKey","null");
        dbsql=new DBHelper(mContext);
        String g=dbsql.getGateway(gate);
        Log.d("GATE",g);
        FTPClient client=null;
        if(client==null)
        {
            client = new FTPClient();
            try {
                client.connect(g);
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
        if(code.equals("DEL")) {
            boolean as = false;
            try {
                for (String s : strings[0]) {
                    Log.d("DELETE ", s);
                    as = client.deleteFile(s);
                    if(!as)
                        as=client.removeDirectory(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(as);
        }
        if(code.equals("MKE"))
        {
            try {

                m=client.makeDirectory(url+'/'+fname);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("MKE",m+"      "+url+fname);
        }
        return null;
    }

}
