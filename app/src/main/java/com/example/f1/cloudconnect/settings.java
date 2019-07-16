package com.example.f1.cloudconnect;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    SharedPreferences.Editor editor;
    Boolean choice;
    Dialog thm;
    boolean val=false;
    Context con;
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences=this.getSharedPreferences("Themes",0);
        choice=preferences.getBoolean("CurrentTheme",false);
        con=this;
        Utility.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);

        TextView help=findViewById(R.id.faq);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.getHelp(con);
            }
        });
        NavigationView nav =findViewById(R.id.nav_view);
        Utility.menuOperations(nav,con,findViewById(android.R.id.content));

        final RelativeLayout content = findViewById(R.id.dd);

        DrawerLayout drawerLayout = findViewById(R.id.draw_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        TextView header=findViewById(R.id.header_text);
        editor=preferences.edit();

        header.setText("Settings");
        thm=new Dialog(this);
        thm.setContentView(R.layout.thme_select);
        thm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        TextView wifival=findViewById(R.id.a2);
        TextView linkval=findViewById(R.id.b3);
        TextView ipval=findViewById(R.id.b2);
        ipval.setText(""+Utility.getgate(this));
        linkval.setText(Utility.getLinkSpeed(this)+"");
        wifival.setText(Utility.getCurrentSSId(this));

        RelativeLayout themedia=findViewById(R.id.theme_info);
        themedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton dark=thm.findViewById(R.id.dark);
                RadioButton light=thm.findViewById(R.id.light);
                if(choice)
                {
                    light.setChecked(true);
                    dark.setChecked(false);
                }
                else{
                    dark.setChecked(true);
                    light.setChecked(false);
                }
                thm.show();
                RadioGroup rg=thm.findViewById(R.id.radgrp);
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId)
                        {
                            case R.id.light:
                                editor.putBoolean("CurrentTheme",true);
                                editor.apply();
                                finishAffinity();

                                break;
                            case R.id.dark:
                                editor.putBoolean("CurrentTheme",false);
                                editor.apply();
                                finishAffinity();

                                break;
                        }
                    }
                });

            }
        });
        RelativeLayout gateinf=findViewById(R.id.server_info);
        gateinf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sev=new Intent(con,allservers.class);
                startActivity(sev);
            }
        });
        RelativeLayout mail=findViewById(R.id.suggest);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email=new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:ashwin102000@gmail.com"));
                startActivity(Intent.createChooser(email, "Send feedback"));

            }
        });



    }
    @Override
    public void onBackPressed() {
        if(val)
        {
            finishAffinity();
            return;
        }
        val=true;
        Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                val=false;
            }
        },2000);
        return;
    }

}
