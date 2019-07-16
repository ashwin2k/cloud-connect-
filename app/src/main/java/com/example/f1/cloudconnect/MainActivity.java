package com.example.f1.cloudconnect;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
            public ArrayList<FTPFile> listfile = null;
            ArrayList<String> keylist;
            ArrayList<String> adminlist;
            ArrayList<String> rootlist;
            EditText sendadmin;
            ArrayList<String> gatelist;
            Switch anonymous;
            boolean val=false;
            DBHelper dbsql;
            EditText sendpass;
            ListView prevloc;
            EditText name;
            Dialog dia,f;
            Intent men;
            Context con;
            EditText admn;
            EditText pwrd;
            String admin;
            String pass;
            String url;
            EditText gate;
            TextView pri;
            TextView secon;
            TextView ter;
            Dialog adminpass;
            String cur_key;
            Button sendadminpass;
            list_adapter list_adapter;
            Dialog manual;
            SharedPreferences.Editor editor;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                SharedPreferences preferences = this.getSharedPreferences("Themes", 0);
                editor = preferences.edit();

                cur_key = preferences.getString("CurrentKey", null);
                Log.d("PREF", cur_key + "   ");
                Utility.changeTheme(this);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                con = this;
                dbsql = new DBHelper(con);


                keylist = dbsql.getAllKeyname();
                rootlist = dbsql.getAllRoots();
                adminlist = dbsql.getAllAdmins();
                gatelist = dbsql.getAllGateway();
                ImageView wifi = findViewById(R.id.imageView);
                keylist = new ArrayList<>();
                rootlist = new ArrayList<>();
                gatelist = new ArrayList<>();
                adminpass = new Dialog(this);
                adminpass.setContentView(R.layout.edit_adminpass);
                adminlist = new ArrayList<>();
                dbsql = new DBHelper(this);
                keylist = dbsql.getAllKeyname();
                rootlist = dbsql.getAllRoots();
                adminlist = dbsql.getAllAdmins();
                gatelist = dbsql.getAllGateway();
                list_adapter = new list_adapter(keylist, gatelist, adminlist, rootlist, this);
                TextView help=findViewById(R.id.faq);
                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utility.getHelp(con);
                    }
                });

                Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
                TextView header = findViewById(R.id.header_text);
                header.setText("Welcome!");
                setSupportActionBar(mTopToolbar);
                mTopToolbar.setContentInsetsAbsolute(0, 0);
                mTopToolbar.getContentInsetEnd();
                mTopToolbar.setPadding(0, 0, 0, 0);
                ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, 4323);
                }
                NavigationView nav = findViewById(R.id.nav_view);
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
                TextView rh=findViewById(R.id.routerhelp);
                rh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(con,ConnectHelp.class));
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Log.d("MAIN", "ok");
                        Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                        TextView status = (TextView) findViewById(R.id.status_find);

                        status.setAnimation(fade);
                        status.startAnimation(fade);
                        v.setAnimation(rotate);
                        status.setText("Finding devices");



                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                dia = new Dialog(con);

                                dia.setContentView(R.layout.net_select_dia);
                                dia.setCanceledOnTouchOutside(false);
                                dia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                MaterialCardView nxt = dia.findViewById(R.id.inc);
                                pri = nxt.findViewById(R.id.prim);
                                secon = nxt.findViewById(R.id.sec);
                                ter = nxt.findViewById(R.id.tert);
                                secon.setText(Utility.getgate(con));

                                prevloc = dia.findViewById(R.id.prevlist);
                                prevloc.setAdapter(list_adapter);
                                prevloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent i = new Intent(con, file_explorer.class);
                                        editor.putString("CurrentKey",keylist.get(position));
                                        editor.apply();
                                        i.putExtra("current", keylist.get(position));
                                            Log.d("PREV REC", keylist.get(position));
                                        startActivity(i);

                                    }
                                });
                                pri.setText(Utility.getCurrentSSId(con));
                                ter.setText(Utility.getLinkSpeed(con) + "");
                                dia.show();


                                nxt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        adminpass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        adminpass.show();
                                        sendadmin = adminpass.findViewById(R.id.get_admin_send);
                                        sendpass = adminpass.findViewById(R.id.get_password_send);
                                        Switch ano = adminpass.findViewById(R.id.anonymous);
                                        ano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    sendadmin.setText("");
                                                    sendadmin.setEnabled(false);
                                                    sendpass.setText("");
                                                    sendpass.setEnabled(false);
                                                } else {
                                                    sendadmin.setText("");
                                                    sendadmin.setEnabled(true);
                                                    sendpass.setText("");
                                                    sendpass.setEnabled(true);
                                                }
                                            }
                                        });
                                        sendadminpass = adminpass.findViewById(R.id.sendadmin);
                                        sendadminpass.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                url = Utility.getgate(con);

                                                admin = sendadmin.getText().toString();
                                                pass = sendpass.getText().toString();
                                                Log.d("URL",url+"");
                                                if(url!=null) {
                                                    Log.d("URL",url+"ex");

                                                    dbsql.addKey(Utility.getCurrentSSId(con), url, admin, pass, "", " ");
                                                    editor.putString("CurrentKey", Utility.getCurrentSSId(con));
                                                    editor.apply();
                                                    Intent i = new Intent(getApplicationContext(), file_explorer.class);
                                                    i.putExtra("current", Utility.getCurrentSSId(con));
                                                    startActivity(i);
                                                    dia.dismiss();
                                                }
                                                else
                                                {
                                                    f=new Dialog(con);
                                                    f.setContentView(R.layout.disconnect);
                                                    f.setCancelable(false);
                                                    f.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    f.show();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            f.dismiss();
                                                        }
                                                    },6000);
                                                }
                                            }
                                        });

                                    }
                                });

                                Button other = dia.findViewById(R.id.otherBut);
                                other.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        manual = new Dialog(con);
                                        manual.setContentView(R.layout.key_edit);
                                        manual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        manual.show();
                                        anonymous = manual.findViewById(R.id.anonymous_admn);
                                        name = manual.findViewById(R.id.get_name);
                                        gate = manual.findViewById(R.id.get_gate);
                                        admn = manual.findViewById(R.id.get_admin);
                                        pwrd = manual.findViewById(R.id.get_password);
                                        Button conext = manual.findViewById(R.id.extcon);
                                        anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    admn.setText("");
                                                    admn.setEnabled(false);
                                                    pwrd.setText("");
                                                    pwrd.setEnabled(false);
                                                } else {
                                                    if (isChecked) {
                                                        admn.setText("");
                                                        admn.setEnabled(true);
                                                        pwrd.setText("");
                                                        pwrd.setEnabled(true);
                                                    }
                                                }
                                            }
                                        });
                                        conext.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                dbsql.addKey(name.getText().toString(), gate.getText().toString(), admn.getText().toString(), pwrd.getText().toString(), ""," ");
                                                editor.putString("CurrentKey", name.getText().toString());
                                                editor.apply();
                                                Log.d("CONEXT", gate.getText().toString() + admn.getText().toString() + pwrd.getText().toString());
                                                Intent i = new Intent(getApplicationContext(), file_explorer.class);
                                                i.putExtra("current", name.getText().toString());
                                                startActivity(i);
                                                manual.dismiss();
                                                dia.dismiss();
                                            }
                                        });
                                    }
                                });
                                /*   */
                            }
                        }, 2000);


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
