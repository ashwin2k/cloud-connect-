package com.example.f1.cloudconnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class file_explorer extends AppCompatActivity {

    SharedPreferences.Editor editor;


    TextView funnytext;
    RelativeLayout bot;
    String uproot;
    Animation appear;
    int folder_count;
    int file_count;
    RelativeLayout fold;
    ArrayList<Integer> positions;
    FTPFile[] files;
    int trigger;
    int p;
    Dialog loading;
    RelativeLayout cust;
    RelativeLayout load;
    Dialog av,d;
    String filename;
    String admin;
    int prot=0;
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
    LottieAnimationView lottie;
    downup downloader;SharedPreferences preferences;
    AlertDialog.Builder alertDialogBuilder;
    Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        positions=new ArrayList<>();
        preferences=this.getSharedPreferences("Themes",0);
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
        loading=new Dialog(con);
        loading.setCancelable(false);
        loading.setContentView(R.layout.loadingdia);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCanceledOnTouchOutside(false);
        url=dbsql.getRoot(url_intent);
        root=dbsql.getRoot(url_intent);
        admin=dbsql.getAdmin(url_intent);
        password=dbsql.getPassword(url_intent);
        gate=dbsql.getGateway(url_intent);
        uproot=dbsql.getUploadDir(url_intent);
        setContentView(R.layout.file_explorer_layout);
        lottie=findViewById(R.id.lottie);

        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.app_x);
        bot=findViewById(R.id.bot_bar);
        ImageView upload=findViewById(R.id.upload);
        cust=findViewById(R.id.cust);
        load=findViewById(R.id.load);
        Button setloc=findViewById(R.id.default_location);
        TextView header=findViewById(R.id.header_text);
        TextView help=findViewById(R.id.faq);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.getHelp(con);
            }
        });
        header.setText("Explorer");
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av=new Dialog(con);
                av.setContentView(R.layout.getfilename);
                    d=new Dialog(con);
                    d.setContentView(R.layout.add_select);
                    d.show();
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    RelativeLayout create=d.findViewById(R.id.creadtedir);
                    create.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            av.show();
                            av.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            final EditText getname=av.findViewById(R.id.getnm);
                            Button cre=av.findViewById(R.id.create);
                            cre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    direc_operations mke=new direc_operations(con,admin,password,"MKE");
                                    mke.setFilename(getname.getText().toString());
                                    mke.setDir(url);
                                    mke.execute();
                                    asyn();
                                    av.dismiss();
                                    d.dismiss();
                                }
                            });

                        }
                    });
                    RelativeLayout upload=d.findViewById(R.id.uploadinto);
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openFile(866);
                        }
                    });



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
                       file_count=0;
                       folder_count=0;
                        Log.d("INDEX",file_count+"");
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
                       Log.d("DETAILS",file_count+" files "+folder_count+" folders");

                       if(url.length()>=8)
                       {
                           direc.setText("..."+url.substring(url.lastIndexOf('/'),url.length()));
                       }
                       else
                           direc.setText(url);
                       size.setText(Utility.sizeCap(bytesize));


                       info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                       info.show();
                        info.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                file_count=0;
                                folder_count=0;

                            }
                        });
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
                               direc_operations del=new direc_operations(con,admin,password,"DEL");
                               del.execute(links);
                               asyn();
                               Toast.makeText(getApplicationContext(), "DELETING "+checkedCount+" files",Toast.LENGTH_SHORT).show();
                           }
                       });
                       alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
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
               positions=new ArrayList<>();

               links.clear();
           }
       });

        TextView foldertext=(TextView)findViewById(R.id.foldername);
        TextView headertext=findViewById(R.id.header_text);
        headertext.setText("Explorer");
        funnytext=findViewById(R.id.funny_text);



        //Initial Loading of files in root directory
        asyn();






        }







    @Override
    public void onBackPressed()
    {

        positions=new ArrayList<>();
        folder_count=0;
        file_count=0;
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
        loading.show();
        url=popper(url);
        async=new netAsyn(admin, password, gate, this, new netAsyn.results() {
            @Override
            public void getFiles(FTPFile[] file,int connectionstat) {
                loading.dismiss();
                files=file;
                Utility.setAnim(load,right);
                load.setVisibility(View.INVISIBLE);
                if(files==null){
                    trigger = 1;
                    bot.setAnimation(down);
                    bot.startAnimation(down);
                    lottie.setAnimationFromUrl("https://assets8.lottiefiles.com/packages/lf20_q50fug.json");
                    lottie.setVisibility(View.VISIBLE);
                    lottie.setAnimation(appear);
                    lottie.startAnimation(appear);
                    funnytext.setVisibility(View.VISIBLE);
                    funnytext.setText("Error connecting to your server");
                }
                else if (files.length == 0) {
                    trigger = 1;
                    bot.setAnimation(down);
                    bot.startAnimation(down);
                    lottie.setAnimationFromUrl("https://assets10.lottiefiles.com/datafiles/hYQRPx1PLaUw8znMhjLq2LdMbklnAwVSqzrkB4wG/bag_error.json");
                    lottie.setVisibility(View.VISIBLE);
                    lottie.setAnimation(appear);
                    lottie.startAnimation(appear);
                    funnytext.setVisibility(View.VISIBLE);

                    funnytext.setText("Empty.\nThat's all we got!");
                }
                else
                {
                    if(trigger==1) {
                        bot.setAnimation(up);
                        bot.startAnimation(up);
                    }
                    lottie.setVisibility(View.INVISIBLE);
                    funnytext.setVisibility(View.INVISIBLE);
                    adapter.updatelist(files);
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                }
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
        if(data==null)
            return;
        Uri uri=data.getData();
        try {
            InputStream inputStream=getContentResolver().openInputStream(uri);
            stream_details stream_details=new stream_details();
            stream_details.setInputStream(inputStream);
            stream_details.setFile_name(queryName(getContentResolver(),uri));
            stream_details.setLocation(url);
            upload up=new upload(con,admin,password, preferences.getString("CurrentKey", null));
            up.execute(stream_details);
        } catch (FileNotFoundException e) {
            Log.d("inp","not found");
            e.printStackTrace();
        }


        super.onActivityResult(requestCode, resultCode, data);


    }
    public void asyn(){
        loading.show();

        async=new netAsyn(admin, password, gate, this, new netAsyn.results() {
            @Override
            public void getFiles(FTPFile[] file,int connectionstat) {
                loading.dismiss();
                files=file;
                Utility.setAnim(load,right);
                load.setVisibility(View.INVISIBLE);
                if(connectionstat==0) {

                    lottie.setAnimationFromUrl("https://assets8.lottiefiles.com/packages/lf20_q50fug.json");
                    lottie.setVisibility(View.VISIBLE);
                    lottie.setAnimation(appear);
                    lottie.startAnimation(appear);
                    funnytext.setVisibility(View.VISIBLE);
                    trigger=1;
                    bot.setAnimation(down);
                    bot.startAnimation(down);
                    funnytext.setText("Error connecting to your server");
                    Toast.makeText(con, "Can't connect", Toast.LENGTH_LONG).show();
                }
                else {

                    trigger=0;
                    lottie.setVisibility(View.INVISIBLE);
                    funnytext.setVisibility(View.INVISIBLE);

                    adapter = new grid_adapter(files, getApplicationContext());
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                            loading.show();
                            filename = files[position].getName();
                            url = url + "/" + filename;
                            p=position;
                            loading.show();

                            async = new netAsyn(admin, password, gate, con, new netAsyn.results() {
                                @Override
                                public void getFiles(FTPFile[] file,int connectionstat) {


                                    Utility.setAnim(load,right);
                                    load.setVisibility(View.INVISIBLE);

                                    if (files[p].isDirectory()) {

                                        gridView.setClickable(false);
                                        async = new netAsyn(admin, password, gate, con, new netAsyn.results() {
                                            @Override
                                            public void getFiles(FTPFile[] file, int connectionstat) {
                                                loading.dismiss();
                                                files = file;
                                                gridView.setClickable(true);
                                                Utility.setAnim(load, right);
                                                load.setVisibility(View.INVISIBLE);
                                                if(files==null){
                                                    trigger = 1;
                                                    bot.setAnimation(down);
                                                    bot.startAnimation(down);
                                                    lottie.setAnimationFromUrl("https://assets8.lottiefiles.com/packages/lf20_q50fug.json");
                                                    lottie.setVisibility(View.VISIBLE);
                                                    lottie.setAnimation(appear);
                                                    lottie.startAnimation(appear);
                                                    funnytext.setVisibility(View.VISIBLE);

                                                    adapter.updatelist(files);
                                                    adapter.notifyDataSetChanged();
                                                    gridView.setAdapter(adapter);
                                                }
                                                else if (files.length == 0) {
                                                    trigger = 1;
                                                    bot.setAnimation(down);
                                                    bot.startAnimation(down);
                                                    lottie.setAnimationFromUrl("https://assets10.lottiefiles.com/datafiles/hYQRPx1PLaUw8znMhjLq2LdMbklnAwVSqzrkB4wG/bag_error.json");
                                                    lottie.setVisibility(View.VISIBLE);
                                                    lottie.setAnimation(appear);
                                                    lottie.startAnimation(appear);
                                                    funnytext.setVisibility(View.VISIBLE);
                                                    funnytext.setText("Empty.\nThat's all we got!");
                                                    adapter.updatelist(files);
                                                    adapter.notifyDataSetChanged();
                                                    gridView.setAdapter(adapter);
                                                } else {
                                                    trigger = 0;
                                                    lottie.setVisibility(View.INVISIBLE);

                                                    funnytext.setVisibility(View.INVISIBLE);
                                                    adapter.updatelist(files);
                                                    adapter.notifyDataSetChanged();
                                                    gridView.setAdapter(adapter);
                                                }

                                            }

                                        });

                                        Utility.setAnim(load,left);
                                        load.setVisibility(View.VISIBLE);
                                        async.execute(url);

                                    }
                                    else {
                                        loading.dismiss();

                                        lottie.setVisibility(View.INVISIBLE);
                                        funnytext.setVisibility(View.INVISIBLE);


                                        alertDialogBuilder.setTitle("Confirm download");

                                        alertDialogBuilder.setMessage("Download "+1+" files?");
                                        alertDialogBuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                group_down downloader= new group_down(con,admin, password);
                                                ArrayList g=new ArrayList<String>();
                                                g.add(url);
                                                downloader.execute(g);
                                                Toast.makeText(getApplicationContext(), "Downloading " + url.substring(url.lastIndexOf('/')+1,url.length()), Toast.LENGTH_SHORT).show();
                                                Log.d("DOWN",url);
                                            }
                                        });
                                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        AlertDialog alertDialog=alertDialogBuilder.create();
                                        alertDialog.show();
                                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                url = url.substring(0,url.lastIndexOf('/'));
                                                Log.d("ALERT","Dismissed");
                                            }
                                        });

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
