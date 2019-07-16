package com.example.f1.cloudconnect;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class answer extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utility.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_lay);
        TextView header = findViewById(R.id.header_text);
        header.setText("Help");
        Bundle b=getIntent().getBundleExtra("DETAILS");
        String q=b.getString("QUESTION",null);
        String ans=b.getString("ANSWERS",null);
        TextView quess=findViewById(R.id.question);
        TextView anss=findViewById(R.id.answer);
        quess.setText(q);
        anss.setText(ans);
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
