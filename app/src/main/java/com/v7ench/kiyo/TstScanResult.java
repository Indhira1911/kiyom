package com.v7ench.kiyo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.v7ench.kiyo.dbhandler.SQLiteHandler;

import java.util.HashMap;

public class TstScanResult extends AppCompatActivity {
TextView docname,conte,prcol,pocol,resulttst,scandqr;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tst_scan_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        docname=(TextView) findViewById(R.id.tstdocname);
        conte=(TextView) findViewById(R.id.content_text);
        prcol=(TextView) findViewById(R.id.pretest_color);
        pocol=(TextView) findViewById(R.id.post_test_color);
resulttst=(TextView) findViewById(R.id.test_result);
        scandqr=(TextView) findViewById(R.id.qr_scanned);
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
         Intent details=getIntent();
        String content = details.getStringExtra("content");
        String pack = details.getStringExtra("pack");
        String sterlizer = details.getStringExtra("sterlizer");
        String sload = details.getStringExtra("sload");
        String sdate = details.getStringExtra("sdate");
        String dqrres = details.getStringExtra("dqres");
        String type = details.getStringExtra("type");
        String precolor = details.getStringExtra("precolor");
        String postcol=details.getStringExtra("postcol");
        if (postcol.equals("#ffffff"))
        {
            resulttst.setText("SAFE");
            resulttst.setTextColor(Color.parseColor("#689F38"));
        }
        else
        {
            resulttst.setText("UNSAFE");
            resulttst.setTextColor(Color.parseColor("#C62828"));
        }
        scandqr.setText(dqrres);
       docname.setText("DR."+name);
        conte.setText(content);
        prcol.setText(precolor);
        pocol.setText(postcol);

    }

}
