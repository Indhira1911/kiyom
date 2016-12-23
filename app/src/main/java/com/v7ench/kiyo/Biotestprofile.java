package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.v7ench.kiyo.dbhandler.SQLiteHandler;

import java.util.HashMap;

public class Biotestprofile extends AppCompatActivity {
TextView pdate,ptime,bdqr,namaki;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biotestprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pdate=(TextView) findViewById(R.id.prebitesttime);
        ptime=(TextView) findViewById(R.id.pretest_date);
        bdqr=(TextView) findViewById(R.id.qr_scanned_bio);
        namaki=(TextView) findViewById(R.id.biodocnamev);
        Intent intent = getIntent();
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        namaki.setText("Dr."+name);
        String biodqr = intent.getStringExtra("biodqr");
        String sdate = intent.getStringExtra("sdate");
        String stime=intent.getStringExtra("stime");
        bdqr.setText(biodqr);
        pdate.setText(sdate);
        ptime.setText(stime);

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Biotestprofile.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
