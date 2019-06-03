package com.example.f1.cloudconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.io.File;
import java.util.ArrayList;

public class AutoUpload extends AppCompatActivity {
    ArrayAdapter adapter;
    ArrayList<String> lis;
    ListView direc;
    Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autoupload_layout);
        lis=new ArrayList<>();
        con=this;
        final Intent i=new Intent(this,bg_updater_service.class);
        Switch toggle=findViewById(R.id.switch1);
        DBHelper dbsql=new DBHelper(this);
        direc=findViewById(R.id.direc_list);
        lis=dbsql.getAllDirectories();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,lis);
        direc.setAdapter(adapter);
        Button add=findViewById(R.id.addLis);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
            }
        });
        Button del=findViewById(R.id.delLis);
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
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                   startService(i);
                else
                    stopService(i);
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                Log.i("Test", "Result URI " + data.getData());
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(data.getData(),
                        DocumentsContract.getTreeDocumentId(data.getData()));
                String path = Utility.getPath(this, docUri);
                Log.i("Test", "Result URI " + data.getData().getPath());
                DBHelper dbsql=new DBHelper(this);
                dbsql.addDirectory(path);
                lis=dbsql.getAllDirectories();
                adapter.notifyDataSetChanged();
                adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,lis);
                direc.setAdapter(adapter);
                break;
        }
    }
}
