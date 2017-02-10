package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.v7ench.kiyo.scanneracti.TstScan;

public class Strat_test_test extends AppCompatActivity {
Button angryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strat_test_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         angryButton = (Button) findViewById(R.id.angry_btn);
        angryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Strat_test_test.this, TstScan.class);
                startActivity(myIntent);
            }
        });
    }

}
