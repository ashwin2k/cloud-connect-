package com.example.f1.cloudconnect;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    SharedPreferences.Editor editor;
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
        setContentView(R.layout.settings_layout);
        TextView header=findViewById(R.id.header_text);
        editor=preferences.edit();

        header.setText("Settings");
        RadioButton dark=findViewById(R.id.dark);
        TextView wifival=findViewById(R.id.wifi_val);
        TextView linkval=findViewById(R.id.link_speed_val);
        TextView ipval=findViewById(R.id.ip_val);
        ipval.setText(" "+Utility.getgate(this));
        linkval.setText(Utility.getLinkSpeed(this)+ " MBPS");
        wifival.setText(Utility.getCurrentSSId(this));
        RadioButton light=findViewById(R.id.light);
        if(choice)
        {
            light.setChecked(true);
            dark.setChecked(false);
        }
       else{
            dark.setChecked(true);
            light.setChecked(false);
        }
        RadioGroup rg=findViewById(R.id.radgrp);
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
}
