package com.example.f1.cloudconnect;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ConnectHelp extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.changeTheme(this);
        setContentView(R.layout.connecthelp);

        TextView header=findViewById(R.id.header_text);
        header.setText("Help");
    }
}
