package com.example.f1.cloudconnect;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.net.ftp.FTPFile.DIRECTORY_TYPE;

public class file_explorer extends AppCompatActivity {

   FTPClient client=null;
    FTPFile[] files;
    String filename;
    GridView gridView;
    String url;
    grid_adapter adapter;
    netAsyn async;
    downup downloader;
    FloatingActionButton fab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url_intent=getIntent().getStringExtra("current");
        url=url_intent;
        setContentView(R.layout.file_explorer_layout);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setContentInsetsAbsolute(0, 0);
        mTopToolbar.getContentInsetEnd();
        mTopToolbar.setPadding(0, 0, 0, 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        gridView=findViewById(R.id.gridview);



        TextView foldertext=(TextView)findViewById(R.id.foldername);
        TextView headertext=findViewById(R.id.header_text);
        headertext.setText("Explorer");

        async=new netAsyn();
        //Initial Loading of files in root directory
        try {
            files=async.execute(url).get();
            url="usb1_1";


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        async.cancel(true);
        async=new netAsyn();
        try {
            files=async.execute(url).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } async.cancel(true);
        adapter=new grid_adapter(files,getApplicationContext());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filename=files[position].getName();
                url=url+"/"+filename;
                async=new netAsyn();
                if(files[position].isDirectory()) {
                    try {
                        files = async.execute(url).get();

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    async.cancel(true);
                    adapter.updatelist(files);
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                }
                else
                {
                    downloader=new downup();
                    downloader.execute(url);
                    Toast.makeText(getApplicationContext(), "Downloading"+url,Toast.LENGTH_SHORT).show();
                    downloader.cancel(true);
                    url=popper(url);
                }
            }
        });


    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    public void onBackPressed()
    {
        url=popper(url);
        async=new netAsyn();
        try {
            files = async.execute(url).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        async.cancel(true);
        adapter.updatelist(files);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);

    }
    String popper(String str)
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

