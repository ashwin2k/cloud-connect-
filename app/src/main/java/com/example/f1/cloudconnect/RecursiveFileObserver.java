package com.example.f1.cloudconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.FileObserver;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class RecursiveFileObserver extends FileObserver {

    String aboslutePath;
    ArrayList<stream_details> links=new ArrayList<>();
    SharedPreferences preferences;
    File upload_file;
    DBHelper dbsql;
    InputStream in;
    String admin;
    String pass;
    Context context;
    public RecursiveFileObserver(String path,Context con,String admn,String pwd) {
        super(path,CREATE);
        Log.d("FileObserver",path);
        aboslutePath = path;
        this.context=con;
        this.admin=admn;
        this.pass=pwd;
    }

    @Override
    public void onEvent(int event, String path) {
        dbsql=new DBHelper(context);
        preferences=context.getSharedPreferences("Themes",0);
        Log.d("FILE","Event Triggered" +event);
        switch (event) {
            case FileObserver.DELETE_SELF:
                Log.d("FILE","DELETED SELF");
                break;
            case FileObserver.MODIFY:
                Log.d("FILE","MODIFIED");
                break;
            case FileObserver.CREATE:
                upload_file=new File(aboslutePath+path);
                try {
                    in=new FileInputStream(upload_file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                stream_details sd=new stream_details();
                sd.setLocation(dbsql.getUploadDir(preferences.getString("CurrentUploadKey","")));
                sd.setFile_name(path);
                sd.optional_link=aboslutePath+path;
                sd.setInputStream(in);
                Log.d("FILE","CREATED at "+path);
                Log.d("FILE","READY FOR UPLOAD ");
                upload up=new upload(context,admin,pass,preferences.getString("CurrentUploadKey",""));
                up.execute(sd);
                break;
            case FileObserver.DELETE:
                Log.d("FILE","DELTED");
                break;
        }

    }
    void upload(stream_details sdf)
    {
        upload up=new upload(context,admin,pass,preferences.getString("CurrentUploadKey",""));
        up.execute(sdf);
    }
}