package com.example.f1.cloudconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="Keys";
    private static final int DB_VERSION=3;
    private static  int DB_TABLE;
    public DBHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE NetworkKeyDB(KEYNAME VARCHAR PRIMARY KEY,GATEWAY VARCHAR,ADMIN VARCHAR,PASSWORD VARCHAR,ROOT VARCHAR,UPLOAD VARCHAR)");

        db.execSQL("CREATE TABLE Directories(ID INTEGER PRIMARY KEY AUTOINCREMENT,PATH VARCHAR)");
        db.execSQL("CREATE TABLE Upload(ID INTEGER PRIMARY KEY AUTOINCREMENT,KEYNAME VARCHAR,STREAM VARCHAR,FILENAME VARCHAR,ROOT VARCHAR)");

    }
    void addUpload(String key,String stream,String filename,String root)
    {
        Log.d("Upload","ERROE");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("KEYNAME",key);
        values.put("STREAM",stream);
        values.put("FILENAME",filename);
        values.put("ROOT",root);
        if(checkExist(filename,2)) {
            db.update("Upload",values,"FILENAME =?",new String[]{filename});
            db.close();
            return;
        }
        db.insert("Upload",null,values);
        db.close();
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS NetworkKeyDB");
            db.execSQL("DROP TABLE IF EXISTS Upload");
            db.execSQL("DROP TABLE IF EXISTS Directories");
            onCreate(db);
    }
    void addKey(String key,String Gate,String Admin,String Pass,String rootdir,String upload)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put("KEYNAME",key);
        values.put("GATEWAY",Gate);
        values.put("UPLOAD",upload);
        values.put("ADMIN",Admin);
        values.put("PASSWORD",Pass);
        values.put("ROOT",rootdir);
        if(checkExist(key,0)) {
            db.update("NetworkKeyDB",values,"KEYNAME =?",new String[]{key});
            db.close();
            return;
        }
        db.insert("NetworkKeyDB",null,values);
        db.close();
}


    void addDirectory(String direc)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put("PATH",direc);

        if(checkExist(direc,1))
        {
            Log.i("Test", "Existes");
            db.update("Directories",values,"PATH =?",new String[]{direc});
            db.close();
            return;
        }
        db.insert("Directories",null,values);
        Log.i("Test", "Inserting");
        db.close();
    }
    boolean checkExist(String name,int tabid)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        if(tabid==0) {
            Cursor cursor = db.rawQuery("SELECT KEYNAME FROM NetworkKeyDB WHERE KEYNAME=?", new String[]{name});
            if (cursor.getCount() > 0)
                return true;
            else if(cursor==null)
                return true;
            else
                return false;
        }
        else if(tabid==2)
        {
            Cursor cursor = db.rawQuery("SELECT FILENAME FROM Upload WHERE FILENAME=?", new String[]{name});
            if (cursor.getCount() > 0)
                return true;
            else if(cursor==null)
                return true;
            else
                return false;
        }
        else{
            Cursor cursor = db.rawQuery("SELECT PATH FROM Directories WHERE PATH=?", new String[]{name});
            if (cursor.getCount() > 0)
                return true;
            else if(cursor==null)
                return true;
            else
                return false;
        }
    }
    ArrayList<String> getAllDirectories()
    {
        ArrayList<String> result=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Directories", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            result.add(res.getString(res.getColumnIndex("PATH")));
            res.moveToNext();
        }
        return result;
    }
    ArrayList<String> getAllKeyname()
    {
        ArrayList<String> result=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from NetworkKeyDB", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            result.add(res.getString(res.getColumnIndex("KEYNAME")));
            res.moveToNext();
        }
        db.close();
        return result;
    }
    ArrayList<String> getAllGateway()
    {
        ArrayList<String> result=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from NetworkKeyDB", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            result.add(res.getString(res.getColumnIndex("GATEWAY")));
            res.moveToNext();
        }
        db.close();
        return result;
    }
    ArrayList<String> getAllAdmins()
    {
        ArrayList<String> result=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from NetworkKeyDB", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            result.add(res.getString(res.getColumnIndex("ADMIN")));
            res.moveToNext();
        }
        db.close();
        return result;
    }
    ArrayList<String> getAllRoots()
    {
        ArrayList<String> result=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from NetworkKeyDB", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            result.add(res.getString(res.getColumnIndex("ROOT")));
            res.moveToNext();
        }
        db.close();
        return result;
    }
    void deleteKey(String key)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("NetworkKeyDB","KEYNAME= ?",new String[]{key});
    }
    void deleteALlDirec()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ "Directories");
    }
    String getGateway(String key)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns={"KEYNAME","GATEWAY","ADMIN","PASSWORD","ROOT","UPLOAD"};
        String selection="KEYNAME=?";
        String[] selectionArgs = {String.valueOf(key)};
        Cursor cursor = db.query("NetworkKeyDB", columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();

        }
        return cursor.getString(1);
    }
    String getRoot(String key)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns={"KEYNAME","GATEWAY","ADMIN","PASSWORD","ROOT","UPLOAD"};
        String selection="KEYNAME=?";
        String[] selectionArgs = {String.valueOf(key)};
        Cursor cursor = db.query("NetworkKeyDB", columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();

        }
        return cursor.getString(4);
    }
    String getUploadDir(String key)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns={"KEYNAME","GATEWAY","ADMIN","PASSWORD","ROOT","UPLOAD"};
        String selection="KEYNAME=?";
        String[] selectionArgs = {String.valueOf(key)};
        Cursor cursor = db.query("NetworkKeyDB", columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();

        }
        return cursor.getString(5);
    }

    String getAdmin(String key)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns={"KEYNAME","GATEWAY","ADMIN","PASSWORD","ROOT"};
        String selection="KEYNAME=?";
        String[] selectionArgs = {String.valueOf(key)};
        Cursor cursor = db.query("NetworkKeyDB", columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();

        }
        return cursor.getString(2);
    }
    String getPassword(String key)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns={"KEYNAME","GATEWAY","ADMIN","PASSWORD","ROOT"};
        String selection="KEYNAME=?";
        String[] selectionArgs = {String.valueOf(key)};
        Cursor cursor = db.query("NetworkKeyDB", columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();

        }
        return cursor.getString(3);
    }
}
