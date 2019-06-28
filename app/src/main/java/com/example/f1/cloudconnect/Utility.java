package com.example.f1.cloudconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.Formatter;

import static android.content.Context.WIFI_SERVICE;


public class Utility {

    public static void getHelp(Context context)
    {
        Intent i=new Intent(context,HelpActivity.class);
        context.startActivity(i);
    }
    public static void setAnim(RelativeLayout r, Animation anim)
    {
        r.setAnimation(anim);
        r.startAnimation(anim);
    }


    public static String getCurrentSSId(Context context)
    {
        String ssid=null;
        ConnectivityManager conn=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nin=conn.getActiveNetworkInfo();
        if (nin == null) {
            return null;
        }

        if (nin.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

                ssid = connectionInfo.getSSID();


        }
        return ssid;
    }
    public static String ftype(String file)
    {
        int len=file.length();
        return file.substring(file.indexOf('.')+1,len);
    }
    public static int getLinkSpeed(Context context)
    {
        int ssid=0;
        ConnectivityManager conn=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nin=conn.getActiveNetworkInfo();
        if (nin == null) {
            return 0;
        }

        if (nin.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

            ssid = connectionInfo.getLinkSpeed();


        }
        return ssid;
    }
    public static String getgate(Context context)
    {
        final WifiManager manager = (WifiManager)context.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        int ipAddress = dhcp.gateway;
        ipAddress = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
                Integer.reverseBytes(ipAddress) : ipAddress;
        byte[] ipAddressByte = BigInteger.valueOf(ipAddress).toByteArray();
        try {
            InetAddress myAddr = InetAddress.getByAddress(ipAddressByte);
            return myAddr.getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Log.e("Wifi Class", "Error getting Hotspot IP address ", e);
        }
        return "null";
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
    public static String rawDirectory(String path)
    {
        String result;
        int len=path.length();
        result=path.substring(0,len-1);
        return result;
    }
    public static Boolean wifiEnabledCheck(Context context)
    {
        WifiManager mng=(WifiManager)context.getSystemService(WIFI_SERVICE);
        return mng.isWifiEnabled();
    }
    public static void menuOperations(NavigationView nav, final Context con, final View view) {
        final LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences preferences = con.getSharedPreferences("Themes", 0);
        final String cur_key = preferences.getString("CurrentKey", " ");
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DBHelper dbsql = new DBHelper(con);
                int id = item.getItemId();
                if (id == R.id.setting) {
                    Log.d("MAX", "add");
                    Intent men = new Intent(con, settings.class);
                    con.startActivity(men);
                }
                if (id == R.id.add) {
                    Log.d("MAX", "add");
                    Intent men = new Intent(con, MainActivity.class);
                    con.startActivity(men);
                }
                if (id == R.id.backup) {
                    boolean servsize=dbsql.checkExist(cur_key, 0);

                    if(servsize) {
                        Log.d("MAX", "setting");
                        Intent men = new Intent(con, AutoUpload.class);
                        con.startActivity(men);
                    }
                   else{

                        Snackbar.make(view,"Please add a server and try again!",Snackbar.LENGTH_LONG).setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Call your action method here

                            }
                        }).show();
                    }
                }
                if(id==R.id.exp){
                    boolean servsize=dbsql.checkExist(cur_key, 0);

                    if (servsize) {
                        Intent ff = new Intent(con, file_explorer.class);
                        ff.putExtra("current", cur_key);
                        con.startActivity(ff);
                    }
                    else{
                        Snackbar.make(view,"Please add a server and try again!",Snackbar.LENGTH_LONG).setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();


                    }
                }
                return true;
            }
        });


    }
    public static void changeTheme(Context context)
    {
        SharedPreferences preferences=context.getSharedPreferences("Themes",0);
        boolean choice=preferences.getBoolean("CurrentTheme",false);
        if(choice)
        {
            context.setTheme(R.style.AppThemeLight);
        }
        else
            context.setTheme(R.style.AppTheme);
    }
    public static String sizeCap(double size)
    {
        DecimalFormat df=new DecimalFormat("#.##");
        int degree=0;
        double sizecat=0;
        while(size>1000) {

                size = (size / 1024);
                sizecat = size;
                degree++;

        }
        switch(degree)
        {
            case 0:
                return df.format(sizecat)+" B";
            case 1:
                return df.format(sizecat)+" KB";
            case 2:
                return df.format(sizecat)+" MB";
            case 3:
                return df.format(sizecat)+" GB";
            case 4:
                return df.format(sizecat)+" TB";
        }
        return (df.format(sizecat)+"");
    }


























    public static String getPath(final Context context, final Uri uri) throws URISyntaxException {
        File file=new File(uri.getPath());
        Log.d("PATH",file.getAbsolutePath());
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O)
        {
            return getFilePath2(context,uri);
        }
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    @SuppressLint("NewApi")
    public static String getFilePath2(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */




}
