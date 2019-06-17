package com.example.f1.cloudconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class list_adapter extends BaseAdapter {
    ArrayList<String> key;
    ArrayList<String> gate;
    Context con;
    LayoutInflater inflater;
    ArrayList<String> admin;
    ArrayList<String> root;
    public list_adapter(ArrayList<String> key,ArrayList<String> gate,ArrayList<String> admin,ArrayList<String> root,Context con)
    {
        this.key=key;
        this.gate=gate;
        this.root=root;
        this.admin=admin;
        this.con=con;
        inflater = (LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return key.size();
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
        String item_key=key.get(position);
        String item_gate=gate.get(position);
        String item_admin=admin.get(position);
        String item_root=root.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.server_unit_list, null);
            TextView posid=convertView.findViewById(R.id.posid);
            TextView keyvalue=convertView.findViewById(R.id.keyvalue);
            TextView gatevalue=convertView.findViewById(R.id.gatevalue);
            posid.setText(position+1+".");
            keyvalue.setText(item_key);
            gatevalue.setText(item_gate);
        }
        return convertView;
    }
}
