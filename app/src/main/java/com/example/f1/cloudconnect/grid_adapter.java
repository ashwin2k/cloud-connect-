package com.example.f1.cloudconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPFile;

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
        if(file_list==null)
            return 0;
        return file_list.length;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences preferences=context.getSharedPreferences("Themes",0);
        Boolean choice=preferences.getBoolean("CurrentTheme",false);
        FTPFile file=(FTPFile) file_list[position];
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.folder_layout, null);
        }
            TextView name=(TextView)convertView.findViewById(R.id.foldername);
        AppCompatImageView img=convertView.findViewById(R.id.folder);

        if(choice)
                name.setTextColor(Color.BLACK);
            else
                name.setTextColor(Color.WHITE);
            name.setText(file.getName());
            if(file.isFile())
            {

                img.setImageResource(R.drawable.ic_file);
                Log.d("TYPE",Utility.ftype(file.getName()));
                String type=Utility.ftype(file.getName());
                switch(type){
                    case "zip" :
                        img.setImageResource(R.drawable.zip);
                        break;
                    case "mp3":
                        img.setImageResource(R.drawable.mp3);
                        break;
                    case "txt":
                        img.setImageResource(R.drawable.txt);
                        break;
                    case "doc":
                    case "docx":
                        img.setImageResource(R.drawable.doc);
                        break;
                    case "png":
                        img.setImageResource(R.drawable.png);
                        break;
                    case "pdf":
                        img.setImageResource(R.drawable.pdf);
                        break;
                    case "ppt":
                    case "pptx":
                        img.setImageResource(R.drawable.ppt);
                        break;
                    case "avi":
                        img.setImageResource(R.drawable.avi);
                        break;
                    case "jpeg":
                    case "jpg":
                        img.setImageResource(R.drawable.jpg);
                        break;
                    case "mp4":
                        img.setImageResource(R.drawable.mp4);
                        break;
                    case "mov":
                        img.setImageResource(R.drawable.mov);
                        break;
                    case "wmv":
                        img.setImageResource(R.drawable.wmv);
                        break;
                    case "mkv":
                        img.setImageResource(R.drawable.mkv);
                        break;
                    case "gif":
                        img.setImageResource(R.drawable.gif);
                        break;
                    case "apk":
                    case "pkg":
                        img.setImageResource(R.drawable.apk);
                        break;
                    case "svg":
                        img.setImageResource(R.drawable.svg);
                        break;
                    case "wav":
                        img.setImageResource(R.drawable.wav);
                        break;
                    case "iso":
                        img.setImageResource(R.drawable.iso);
                        break;
                    case "exe":
                    case "msi":
                        img.setImageResource(R.drawable.exe);
                        break;
                    case "info":
                    case "nfo":
                        img.setImageResource(R.drawable.nfo);
                        break;
                    case "html":
                    case "htm":
                    case "xhtml":
                        img.setImageResource(R.drawable.html);
                        break;
                    case "ico":
                        img.setImageResource(R.drawable.ico);
                        break;
                    case "vcf":
                        img.setImageResource(R.drawable.vcf);
                        break;
                    case "dll":
                        img.setImageResource(R.drawable.dll);
                        break;
                    case "css":
                        img.setImageResource(R.drawable.css);
                        break;
                    case "xml":
                        img.setImageResource(R.drawable.xml);
                        break;
                    case "bin":
                        img.setImageResource(R.drawable.bin);
                        break;
                    case "ini":
                        img.setImageResource(R.drawable.ini);
                        break;
                    case "jar":
                        img.setImageResource(R.drawable.jar);
                        break;
                    case "eml":
                        img.setImageResource(R.drawable.eml);
                        break;
                    case "xls":
                    case "xlsx":
                        img.setImageResource(R.drawable.xls);
                        break;
                    case "csv":
                        img.setImageResource(R.drawable.csv);
                        break;
                    case "js":
                        img.setImageResource(R.drawable.js);
                        break;
                    case "asp":
                        img.setImageResource(R.drawable.txt);
                        break;
                    case "rtf":
                        img.setImageResource(R.drawable.rtf);
                        break;
                    case "psd":
                        img.setImageResource(R.drawable.psd);
                        break;
                    case "3ds":
                        img.setImageResource(R.drawable.tds);
                        break;
                    case "obj":
                        img.setImageResource(R.drawable.obj);
                        break;
                    case "otf":
                        img.setImageResource(R.drawable.otf);
                        break;
                    case "ttf":
                        img.setImageResource(R.drawable.ttf);
                        break;
                    case "dwg":
                        img.setImageResource(R.drawable.dwg);
                        break;
                    default:
                        img.setImageResource(R.drawable.ic_file);

                }


            }
            else
                img.setImageResource(R.drawable.ic_folder);


            if(file.getName().length()>8)
            {
                name.setText(file.getName().substring(0,8)+"...");
            }



        return convertView;
    }

}
