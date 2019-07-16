package com.example.f1.cloudconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    String[] answers;
    Context c;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utility.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);
        answers=getResources().getStringArray(R.array.answers);
        TextView header = findViewById(R.id.header_text);
        header.setText("Help");
        c=this;
        final String[] faqs={"Q: Cannot connect to FTP server.","Q: App crashes abnormally.","Q: Cannot upload to FTP server.","Q: Auto Backup feature not working."};

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,faqs);
        ListView faqlist=findViewById(R.id.faqlist);

        faqlist.setAdapter(adapter);
        faqlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent send=new Intent(c,answer.class);
                    Bundle b=new Bundle();
                    b.putString("QUESTION",faqs[position]);
                    b.putString("ANSWERS",answers[position]);
                    send.putExtra("DETAILS",b);
                    startActivity(send);
            }
        });
        NavigationView nav = findViewById(R.id.nav_view);
        Utility.menuOperations(nav,this,findViewById(android.R.id.content));

        final RelativeLayout content = findViewById(R.id.dd);

        DrawerLayout drawerLayout = findViewById(R.id.draw_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }
}
