package com.example.f1.cloudconnect;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;

public class grid_adapter extends BaseAdapter {
    FTPFile[] file_list;
    Context context;
    LayoutInflater inflater;

    public grid_adapter(FTPFile[] list, Context c)
    {

        this.context=c;
        this.file_list=list;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void updatelist(FTPFile[] update)
    {
        this.file_list=update;

    }
    @Override
    public int getCount() {
        return file_list.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FTPFile file=(FTPFile) file_list[position];
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.folder_layout, null);
            TextView name=(TextView)convertView.findViewById(R.id.foldername);
            
            name.setText(file.getName());
            if(file.isFile())
            {
                AppCompatImageView img=convertView.findViewById(R.id.folder);
                img.setImageResource(R.drawable.ic_file);
            }
            if(file.getName().length()>8)
            {
                name.setText(file.getName().substring(0,8)+"...");
            }

        }
        return convertView;
    }

}
