package com.example.f1.cloudconnect;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public ArrayList<FTPFile> listfile=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setContentInsetsAbsolute(0, 0);
        mTopToolbar.getContentInsetEnd();
        mTopToolbar.setPadding(0, 0, 0, 0);
        ImageView imageView=(ImageView)findViewById(R.id.imageView3);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MAIN","ok");
                Animation rotate=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                Animation fade=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
                TextView status=(TextView)findViewById(R.id.status_find);

                status.setAnimation(fade);
                status.startAnimation(fade);
                v.setAnimation(rotate);
                status.setText("Finding devices");
                v.startAnimation(rotate);

                final String url="192.169.0.1";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i=new Intent(getApplicationContext(),file_explorer.class);

                        i.putExtra("current",url);
                        startActivity(i);
                    }
                }, 2000);

            }
        });


    }

}
