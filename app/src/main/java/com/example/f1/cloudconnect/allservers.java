package com.example.f1.cloudconnect;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class allservers extends AppCompatActivity {
    ArrayList<String> keylist;
    ArrayList<String> adminlist;
    ArrayList<String> rootlist;
    ArrayList<String> gatelist;
    ArrayList<String> uploadlis;
    list_adapter adapter;
    ListView lis;
    Dialog details;
    DBHelper dbsql;

    TextView sevid;
    TextView gatid;
    Context con;
    ImageView delete;
    TextView admid;
    TextView rootid;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        details=new Dialog(this);
        con=this;
        details.setContentView(R.layout.server_details);
        Utility.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_list);
        TextView header=findViewById(R.id.header_text);
        header.setText("Servers");
        NavigationView nav = findViewById(R.id.nav_view);
        Utility.menuOperations(nav,this,findViewById(android.R.id.content));
        TextView help=findViewById(R.id.faq);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.getHelp(con);
            }
        });
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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        sevid=details.findViewById(R.id.q2);
        gatid=details.findViewById(R.id.w2);
        admid=details.findViewById(R.id.e2);
        rootid=details.findViewById(R.id.r2);

        keylist=new ArrayList<>();
        rootlist=new ArrayList<>();
        gatelist=new ArrayList<>();
        adminlist=new ArrayList<>();
        uploadlis=new ArrayList<>();
        dbsql=new DBHelper(this);
        keylist=dbsql.getAllKeyname();
        rootlist=dbsql.getAllRoots();
        adminlist=dbsql.getAllAdmins();

        delete=details.findViewById(R.id.delete_net);

        gatelist=dbsql.getAllGateway();
        Log.d("SERVERS",keylist.size()+"");
        adapter=new list_adapter(keylist,gatelist,adminlist,rootlist,this);
        lis=findViewById(R.id.sevlist);
        lis.setAdapter(adapter);
        lis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    sevid.setText(keylist.get(position));
                    gatid.setText(gatelist.get(position));
                    admid.setText(adminlist.get(position));
                    rootid.setText(dbsql.getUploadDir(keylist.get(position)));
                     details.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    details.show();
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                    dbsql.deleteKey(keylist.get(position));
                                    keylist=dbsql.getAllKeyname();
                                    adapter.notifyDataSetChanged();
                                    adapter=new list_adapter(keylist,gatelist,adminlist,rootlist,con);
                                    lis.setAdapter(adapter);
                                    details.dismiss();
                        }
                    });
            }
        });
    }
}
