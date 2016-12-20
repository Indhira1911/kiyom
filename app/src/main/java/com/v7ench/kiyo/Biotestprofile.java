package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Biotestprofile extends AppCompatActivity {
TextView pdate,ptime,bdqr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biotestprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pdate=(TextView) findViewById(R.id.prebitesttime);
        ptime=(TextView) findViewById(R.id.pretest_date);
        bdqr=(TextView) findViewById(R.id.qr_scanned_bio);
        Intent intent = getIntent();
        String biodqr = intent.getStringExtra("biodqr");
        String sdate = intent.getStringExtra("sdate");
        String stime=intent.getStringExtra("stime");
        bdqr.setText(biodqr);
        pdate.setText(sdate);
        ptime.setText(stime);

    }

}
