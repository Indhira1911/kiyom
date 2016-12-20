package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.v7ench.kiyo.scanneracti.TstScan;

public class Tstresultsum extends AppCompatActivity {
Button btstrsult;
    ImageView adf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tstresultsum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
btstrsult=(Button) findViewById(R.id.tsttestb);
        adf=(ImageView) findViewById(R.id.image_bio_view);
btstrsult.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent =new Intent(Tstresultsum.this,TstResult.class);
        startActivity(intent);
        finish();
    }
});
        adf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Tstresultsum.this,TstScan.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
