package com.example.f1.cloudconnect;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class AutoUpload extends AppCompatActivity {
    ArrayAdapter adapter;
    DBHelper dbsql;
    Dialog d;
    boolean val=false;
    ListView servers;
    PendingIntent auto;
    ArrayList<String> lis;
    list_adapter list_adapter;
    AlarmManager alarmManager;
    SharedPreferences preferences;
    ArrayList<String> keylist;
    ArrayList<String> adminlist;
    ArrayList<String> rootlist;
    ArrayList<String> gatelist;
    SharedPreferences.Editor editor;
    ListView direc;
    String admin;
    Calendar cal;
    String pass;
    Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.changeTheme(this);
        setContentView(R.layout.autoupload_layout);
        lis=new ArrayList<>();
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 0);
        con=this;
        TextView header=findViewById(R.id.header_text);
        header.setText("Backup");
        preferences=this.getSharedPreferences("Themes",0);
        editor=preferences.edit();
        dbsql=new DBHelper(this);
        String cur_key=preferences.getString("CurrentKey",null);
        admin=dbsql.getAdmin(preferences.getString("CurrentKey",null));
        pass=dbsql.getPassword(preferences.getString("CurrentKey",null));
        keylist = dbsql.getAllKeyname();
        rootlist = dbsql.getAllRoots();
        adminlist = dbsql.getAllAdmins();
        gatelist = dbsql.getAllGateway();
        list_adapter = new list_adapter(keylist, gatelist, adminlist, rootlist, this);
        d=new Dialog(this);
        d.setContentView(R.layout.backup_server_dialog);
        servers=d.findViewById(R.id.servselect);
        servers.setAdapter(list_adapter);
        final Intent i=new Intent(this,bg_updater_service.class);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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

        Switch toggle=findViewById(R.id.switch1);
        final DBHelper dbsql=new DBHelper(this);
        direc=findViewById(R.id.direc_list);
        lis=dbsql.getAllDirectories();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,lis);
        direc.setAdapter(adapter);
        RelativeLayout add=findViewById(R.id.addLis);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
            }
        });
        RelativeLayout del=findViewById(R.id.delLis);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbsql=new DBHelper(con);
                dbsql.deleteALlDirec();
                lis=dbsql.getAllDirectories();
                    adapter.notifyDataSetChanged();
                adapter=new ArrayAdapter(con,android.R.layout.simple_list_item_1,lis);
                direc.setAdapter(adapter);
            }
        });
        TextView help=findViewById(R.id.faq);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.getHelp(con);
            }
        });
        toggle.setChecked(isMyServiceRunning(bg_updater_service.class));
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    d.show();
                    servers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            editor.putString("CurrentUploadKey",keylist.get(position));
                            editor.commit();
                            if(dbsql.getUploadDir(keylist.get(position)).equals("") || dbsql.getUploadDir(keylist.get(position)).equals(" ")){
                                final Snackbar bar=Snackbar.make(findViewById(android.R.id.content),"Upload directory is not specified. \nThis may cause problems in some servers",Snackbar.LENGTH_INDEFINITE);
                                Snackbar.SnackbarLayout barlay=(Snackbar.SnackbarLayout) bar.getView();
                                bar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bar.dismiss();
                                    }
                                });
                                barlay.setBackgroundColor(Color.parseColor("#ff0000"));
                                bar.show();
                            }
                            startService(i);
                            d.dismiss();
                        }
                    });


                }
                else
                    stopService(i);
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
              //  Log.i("Test", "Result URI " + data.getData());
                if(data==null)
                    break;
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(data.getData(),
                        DocumentsContract.getTreeDocumentId(data.getData()));
                String path = null;
                try {
                    path = Utility.getPath(this, docUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.i("Test", "Result URI1 " + path);
                DBHelper dbsql=new DBHelper(this);
                dbsql.addDirectory(path);
                lis=dbsql.getAllDirectories();
                adapter.notifyDataSetChanged();
                adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,lis);
                direc.setAdapter(adapter);
                break;
        }
    }
    public  boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
