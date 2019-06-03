package com.example.f1.cloudconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.net.ftp.FTPFile.DIRECTORY_TYPE;


public class file_explorer extends AppCompatActivity {


   FTPClient client=null;
    FTPFile[] files;
    String filename;
    group_down grp;
    ArrayList<File> sharefiles;
    ArrayList<Uri> contenturis;
    ArrayList<String > links;
    static final int COD=777;
    GridView gridView;
    String url;
    grid_adapter adapter;
    netAsyn async;
    downup downloader;
    AlertDialog.Builder alertDialogBuilder;
    Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        links=new ArrayList<>();
        con=this;
        String url_intent=getIntent().getStringExtra("current");
        url=url_intent;
        setContentView(R.layout.file_explorer_layout);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
        ImageView upload=findViewById(R.id.upload);
        TextView header=findViewById(R.id.header_text);
        header.setText("Explorer");
        alertDialogBuilder = new AlertDialog.Builder(this);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    openFile(866);


            }
        });

        setSupportActionBar(mTopToolbar);
        mTopToolbar.setContentInsetsAbsolute(0, 0);
        mTopToolbar.getContentInsetEnd();
        mTopToolbar.setPadding(0, 0, 0, 0);

        gridView=findViewById(R.id.gridview);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
       gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
           @Override
           public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
               final int checkedCount = gridView.getCheckedItemCount();
               // Set the CAB title according to total checked items
               mode.setTitle(checkedCount + " Selected");
               if(checked)
                     links.add(url+'/'+files[position].getName());
               else
                   links.remove(url+'/'+files[position].getName());
               Log.d("Long","press"+links);

               ImageView imageView=findViewById(R.id.download);
               imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        alertDialogBuilder.setTitle("Confirm download");
                        alertDialogBuilder.setMessage("Download "+links.size()+" files?");
                        alertDialogBuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                grp=new group_down(getApplicationContext());
                                grp.execute(links);
                                Toast.makeText(getApplicationContext(), "Downloading "+checkedCount+" files",Toast.LENGTH_SHORT).show();
                            }
                        });
                       alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               finish();
                           }
                       });
                       AlertDialog alertDialog=alertDialogBuilder.create();
                       alertDialog.show();


                   }
               });
               final ImageView share=findViewById(R.id.share);
               share.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       contenturis=new ArrayList<>();
                       sharefiles=new ArrayList<>();
                       grp=new group_down(getApplicationContext());
                       try {
                           contenturis=grp.execute(links).get();
                       } catch (ExecutionException | InterruptedException e) {
                           e.printStackTrace();
                       }
                       /*for(File fi:sharefiles)
                       {
                           try {
                               Uri m=Utility.convertFileToContentUri(con,fi);
                               Log.d("FILES","kk"+sharefiles.size());
                               contenturis.add(m);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }*/
                       Intent shareintent=new Intent();
                       shareintent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
                       shareintent.setType("*/*");
                       shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       shareintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,contenturis);
                       startActivity(Intent.createChooser(shareintent, "Send to"));

                   }
               });
               ImageView delete=findViewById(R.id.delete);
               delete.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       direc_operations del=new direc_operations();
                       del.execute(links);

                   }
               });


           }

           @Override
           public boolean onCreateActionMode(ActionMode mode, Menu menu) {
               return true;
           }

           @Override
           public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               return true;
           }

           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               return false;
           }

           @Override
           public void onDestroyActionMode(ActionMode mode) {
                        links.clear();
           }
       });

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
    private void openFile(int  CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri=data.getData();
        try {
            InputStream inputStream=getContentResolver().openInputStream(uri);
            stream_details stream_details=new stream_details();
            stream_details.setInputStream(inputStream);
            stream_details.setFile_name(queryName(getContentResolver(),uri));
            stream_details.setLocation(url);
            upload up=new upload(con);
            up.execute(stream_details);
        } catch (FileNotFoundException e) {
            Log.d("inp","not found");
            e.printStackTrace();
        }


        super.onActivityResult(requestCode, resultCode, data);


    }
    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

}
