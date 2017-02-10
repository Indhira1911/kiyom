package com.v7ench.kiyo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class About_Us extends AppCompatActivity {
ImageButton abus,termsandco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        abus=(ImageButton) findViewById(R.id.tstma);
        termsandco=(ImageButton) findViewById(R.id.termacon);
        abus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About_Us.this,"Hi you clicked me!",Toast.LENGTH_SHORT).show();

            }
        });
        termsandco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About_Us.this,"Hi you clicked me!",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
