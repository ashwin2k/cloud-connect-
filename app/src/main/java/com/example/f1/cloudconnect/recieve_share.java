package com.example.f1.cloudconnect;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class recieve_share extends AppCompatActivity {
    Uri uri;

    stream_details stream_details;
    Context con;
    View dial;
    TextView filenm;
    String admin;
    String pass;
    DBHelper dbsql;
    TextView uploc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        con=this;
        SharedPreferences preferences=this.getSharedPreferences("Themes",0);
        Boolean choice=preferences.getBoolean("CurrentTheme",false);
        dbsql=new DBHelper(this);

        admin=dbsql.getAdmin(preferences.getString("CurrentKey",null));
        pass=dbsql.getPassword(preferences.getString("CurrentKey",null));

        if(choice)
        {
            this.setTheme(R.style.AppThemeLight);
        }
        else
            this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_dialog);
        Intent share=getIntent();
        uri=share.getParcelableExtra(Intent.EXTRA_STREAM);
        TextView header=findViewById(R.id.header_text);
        header.setText("Upload");
        InputStream file= null;
        try {
            file = getContentResolver().openInputStream(uri);
            stream_details=new stream_details();
            stream_details.setInputStream(file);

            stream_details.setFile_name(file_explorer.queryName(getContentResolver(),uri));
            stream_details.setLocation(dbsql.getUploadDir(preferences.getString("CurrentKey",null)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }






        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm Upload?");
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog, null);
        alert.setView(alertLayout);
        uploc=alertLayout.findViewById(R.id.uploc);
        filenm=alertLayout.findViewById(R.id.filename);
        String txt=file_explorer.queryName(getContentResolver(),uri);
        if(txt.length()>8)
        {
            filenm.setText(txt.substring(0,8)+"...");
        }
        else
            filenm.setText(txt);
        uploc.setText(dbsql.getUploadDir(preferences.getString("CurrentKey",null)));
        alert.setCancelable(false);
        alert.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                    upload up=new upload(con,admin,pass);
                    up.execute(stream_details);
                    finish();


            }
        });
        AlertDialog dia= alert.create();
        dia.show();


    }
}
