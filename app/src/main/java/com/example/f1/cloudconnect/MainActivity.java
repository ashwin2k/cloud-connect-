package com.example.f1.cloudconnect;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

        public class MainActivity extends AppCompatActivity {
            public ArrayList<FTPFile> listfile=null;
            Dialog dia;
            Intent men;
            Context con;
            String url;
            TextView pri;
            TextView secon;
            TextView ter;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                SharedPreferences preferences=this.getSharedPreferences("Themes",0);
                Boolean choice=preferences.getBoolean("CurrentTheme",false);
                if(choice)
                {
                    this.setTheme(R.style.AppThemeLight);
                }
                else
                    this.setTheme(R.style.AppTheme);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                con=this;
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
        TextView header=findViewById(R.id.header_text);
        header.setText(R.string.act_name);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setContentInsetsAbsolute(0, 0);
        mTopToolbar.getContentInsetEnd();
        mTopToolbar.setPadding(0, 0, 0, 0);
        ImageView imageView=(ImageView)findViewById(R.id.imageView3);
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 4323);
                } NavigationView nav=findViewById(R.id.nav_view);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.help){

                    Log.d("MAX","add");
                    men=new Intent(getApplicationContext(),AutoUpload.class);
                    startActivity(men);
                }
                if(id==R.id.add){
                    Log.d("MAX","add");
                men=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(men);
                }
                if(id==R.id.setting) {
                    Log.d("MAX", "setting");
                    men = new Intent(getApplicationContext(), settings.class);
                    startActivity(men);
                }
                return true;
            }
        });
        final RelativeLayout content=findViewById(R.id.dd);

        DrawerLayout drawerLayout=findViewById(R.id.draw_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startService(new Intent(con,bg_updater_service.class));
                Log.d("MAIN","ok");
                Animation rotate=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                Animation fade=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
                TextView status=(TextView)findViewById(R.id.status_find);

                status.setAnimation(fade);
                status.startAnimation(fade);
                v.setAnimation(rotate);
                status.setText("Finding devices");
                DBHelper dbsql=new DBHelper(con);
                url=Utility.getgate(con);
                dbsql.addKey("MAIN",url,"admin","2d5dg5yn","usb1_1");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dia=new Dialog(con);
                        dia.setContentView(R.layout.net_select_dia);
                        dia.show();
                        pri=dia.findViewById(R.id.prim);
                        secon=dia.findViewById(R.id.sec);
                        ter=dia.findViewById(R.id.tert);
                        secon.setText(Utility.getgate(con));
                        RelativeLayout nxt=dia.findViewById(R.id.inc);
                        pri.setText(Utility.getCurrentSSId(con));
                        ter.setText(Utility.getLinkSpeed(con)+"");
                        nxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(getApplicationContext(),file_explorer.class);
                                i.putExtra("current",url);
                                startActivity(i);
                            }
                        });
                       /*   */
                    }
                }, 2000);


            }
        });


    }

}
