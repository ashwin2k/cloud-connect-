package com.example.f1.cloudconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    SharedPreferences.Editor editor;


    TextView funnytext;
    RelativeLayout bot;
    String uproot;
    Animation appear;
    ArrayList<Integer> positions;
    FTPFile[] files;
    ImageView beer;
    int trigger;
    int p;
    RelativeLayout cust;
    RelativeLayout load;
    String filename;
    String admin;
    String password;
    boolean val=false;
    String gate;
    String root;
    group_down grp;
    ArrayList<File> sharefiles;
    ArrayList<Uri> contenturis;
    ArrayList<String > links;
    static final int COD=777;
    GridView gridView;
    String url;
    Animation up;
    Animation right;
    Animation left;
    grid_adapter adapter;
    netAsyn async;
    DBHelper dbsql;
    Animation down;
    downup downloader;
    AlertDialog.Builder alertDialogBuilder;
    Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        positions=new ArrayList<>();
        SharedPreferences preferences=this.getSharedPreferences("Themes",0);
        editor=preferences.edit();
        down=AnimationUtils.loadAnimation(this,R.anim.down);
        up=AnimationUtils.loadAnimation(this,R.anim.up);
        right=AnimationUtils.loadAnimation(this,R.anim.right);
        left=AnimationUtils.loadAnimation(this,R.anim.left);
        appear=AnimationUtils.loadAnimation(this,R.anim.appear);
        Utility.changeTheme(this);
        super.onCreate(savedInstanceState);
        links=new ArrayList<>();
        trigger=0;
        con=this;
        dbsql=new DBHelper(this);
        final String url_intent=getIntent().getStringExtra("current");

        url=dbsql.getRoot(url_intent);
        root=dbsql.getRoot(url_intent);

        admin=dbsql.getAdmin(url_intent);
        password=dbsql.getPassword(url_intent);
        gate=dbsql.getGateway(url_intent);
        uproot=dbsql.getUploadDir(url_intent);
        setContentView(R.layout.file_explorer_layout);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
        bot=findViewById(R.id.bot_bar);
        ImageView upload=findViewById(R.id.upload);
      cust=findViewById(R.id.cust);
      load=findViewById(R.id.load);
        Button setloc=findViewById(R.id.default_location);
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
        bot=findViewById(R.id.bot_bar);


        NavigationView nav = findViewById(R.id.nav_view);
        Utility.menuOperations(nav,con,findViewById(android.R.id.content));

        final RelativeLayout content = findViewById(R.id.ddcontent);

        DrawerLayout drawerLayout = findViewById(R.id.draw_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        if(uproot.equals(" "))
        {
            cust.setVisibility(View.VISIBLE);
        }
        else {
            cust.setVisibility(View.INVISIBLE);

        }
        setloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbsql.addKey(url_intent,gate,admin,password,"",url);
                Log.d("UPDATE ROOT",url);
                Animation d=AnimationUtils.loadAnimation(con,R.anim.right);
                cust.setAnimation(d);
                cust.startAnimation(d);
                cust.setVisibility(View.INVISIBLE);

            }
        });
        drawerLayout.addDrawerListener(actionBarDrawerToggle);





        gridView=findViewById(R.id.gridview);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
       gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
           @Override
           public void onItemCheckedStateChanged(ActionMode mode, final int position, long id, boolean checked) {
               final int checkedCount = gridView.getCheckedItemCount();

               // Set the CAB title according to total checked items
               mode.setTitle(checkedCount + " Selected");
               if(checked) {
                   positions.add(position);
                   links.add(url + '/' + files[position].getName());
               }
               else {
                   positions.remove(new Integer(position));
                   links.remove(url + '/' + files[position].getName());
               }
               Log.d("Long","press"+links);
               ImageView info=findViewById(R.id.copy);
               info.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       long bytesize=0;
                       int folder_count=0;
                       int file_count=0;

                       for(int i:positions)
                   {
                       bytesize+=files[i].getSize();
                       if(files[i].isFile())
                           file_count++;
                       if(files[i].isDirectory())
                           folder_count++;
                   }

                       Dialog info=new Dialog(con);
                       info.setContentView(R.layout.info_dialog);
                       TextView size=info.findViewById(R.id.size_val);
                       TextView direc=info.findViewById(R.id.pd_val);
                       TextView selinfo=info.findViewById(R.id.selected_info);
                       selinfo.setText(file_count+" files and "+folder_count +" folders");
                       size.setText(Utility.sizeCap(bytesize));
                       info.show();
                       folder_count=0;
                       file_count=0;
                       Log.d("DETAILS",file_count+" files "+folder_count+" folders");
                   }
               });

               ImageView imageView=findViewById(R.id.download);
               imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        alertDialogBuilder.setTitle("Confirm download");
                        alertDialogBuilder.setMessage("Download "+links.size()+" files?");
                        alertDialogBuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                grp=new group_down(getApplicationContext(),admin,password);
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
                       grp=new group_down(getApplicationContext(),admin,password);
                       try {
                           contenturis=grp.execute(links).get();
                       } catch (ExecutionException | InterruptedException e) {
                           e.printStackTrace();
                       }

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

                       alertDialogBuilder.setTitle("Confirm delete");
                       alertDialogBuilder.setMessage("Delete "+links.size()+" files?");
                       alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               direc_operations del=new direc_operations(con,admin,password);
                               del.execute(links);
                               Toast.makeText(getApplicationContext(), "DELETING "+checkedCount+" files",Toast.LENGTH_SHORT).show();
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
        funnytext=findViewById(R.id.funny_text);
        beer=findViewById(R.id.beer_mug);



        //Initial Loading of files in root directory

        async=new netAsyn(admin, password, gate, this, new netAsyn.results() {
            @Override
            public void getFiles(FTPFile[] file,int connectionstat) {

                files=file;
                Utility.setAnim(load,right);
                load.setVisibility(View.INVISIBLE);
                if(connectionstat==0) {


                    beer.setImageResource(R.drawable.pooky);
                    beer.setVisibility(View.VISIBLE);
                    beer.setAnimation(appear);
                    beer.startAnimation(appear);
                    funnytext.setVisibility(View.VISIBLE);
                    trigger=1;
                    bot.setAnimation(down);
                    bot.startAnimation(down);
                    funnytext.setText("Pooky is sad.\n" + "He could'nt connect to your server right now");
                    Toast.makeText(con, "Can't connect", Toast.LENGTH_LONG).show();
                }
                else {

                    trigger=0;
                    beer.setVisibility(View.INVISIBLE);
                    funnytext.setVisibility(View.INVISIBLE);

                    adapter = new grid_adapter(files, getApplicationContext());
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                            filename = files[position].getName();
                            url = url + "/" + filename;
                            p=position;
                            async = new netAsyn(admin, password, gate, con, new netAsyn.results() {
                                @Override
                                public void getFiles(FTPFile[] file,int connectionstat) {
                                    Log.d("DELEGATE",file.length+"   "+url);
                                    Utility.setAnim(load,right);
                                    load.setVisibility(View.INVISIBLE);

                                    if (files[p].isDirectory()) {

                                        async=new netAsyn(admin, password, gate, con, new netAsyn.results() {
                                            @Override
                                            public void getFiles(FTPFile[] file,int connectionstat) {
                                                files=file;
                                                Utility.setAnim(load,right);
                                                load.setVisibility(View.INVISIBLE);
                                                if(files.length==0)
                                                {
                                                    trigger=1;
                                                    bot.setAnimation(down);
                                                    bot.startAnimation(down);
                                                    beer.setImageResource(R.drawable.beer_mug);
                                                    beer.setVisibility(View.VISIBLE);
                                                    beer.setAnimation(appear);
                                                    beer.startAnimation(appear);
                                                    funnytext.setVisibility(View.VISIBLE);
                                                    funnytext.setText("Empty.Just like my beer mug.");
                                                }
                                                else{
                                                    trigger=0;
                                                    beer.setVisibility(View.INVISIBLE);
                                                    funnytext.setVisibility(View.INVISIBLE);
                                                }
                                                adapter.updatelist(files);
                                                adapter.notifyDataSetChanged();
                                                gridView.setAdapter(adapter);
                                            }
                                        });
                                        Utility.setAnim(load,left);
                                        load.setVisibility(View.VISIBLE);
                                        async.execute(url);
                                    }
                                    else {
                                        beer.setVisibility(View.INVISIBLE);
                                        funnytext.setVisibility(View.INVISIBLE);
                                        downloader = new downup(admin, password,con);
                                        downloader.execute(url);
                                        Toast.makeText(getApplicationContext(), "Downloading" + url, Toast.LENGTH_SHORT).show();

                                        url = popper(url);
                                    }
                                }
                            });
                            async.execute(url);

                        }
                    });

                }
            }
        });

            Utility.setAnim(load,left);
            load.setVisibility(View.VISIBLE);
            async.execute(url);





        }







    @Override
    public void onBackPressed()
    {


        if(url.equals(root)||url.equals(root+'/')) {

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

        url=popper(url);
        async=new netAsyn(admin, password, gate, this, new netAsyn.results() {
            @Override
            public void getFiles(FTPFile[] file,int connectionstat) {
                Log.d("DELEGATE",file.length+"");
                files=file;
                Utility.setAnim(load,right);
                load.setVisibility(View.INVISIBLE);
                if(files.length==0)
                {

                    bot.setVisibility(View.INVISIBLE);
                    beer.setImageResource(R.drawable.beer_mug);
                    beer.setVisibility(View.VISIBLE);
                    beer.setAnimation(appear);
                    beer.startAnimation(appear);
                    funnytext.setVisibility(View.VISIBLE);
                    funnytext.setText("Empty.Just like my beer mug.");
                }
                else
                {
                    if(trigger==1) {
                        bot.setAnimation(up);
                        bot.startAnimation(up);
                    }

                    beer.setVisibility(View.INVISIBLE);
                    funnytext.setVisibility(View.INVISIBLE);
                }adapter.updatelist(files);
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
            }
        });
        Utility.setAnim(load,left);
        load.setVisibility(View.VISIBLE);
             async.execute(url);





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
            upload up=new upload(con,admin,password);
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
