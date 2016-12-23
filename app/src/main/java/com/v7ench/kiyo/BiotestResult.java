package com.v7ench.kiyo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.v7ench.kiyo.dbhandler.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.victoralbertos.breadcumbs_view.BreadcrumbsView;

public class BiotestResult extends AppCompatActivity {
TextView iop,knv;
    BreadcrumbsView bcv;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biotest_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
iop=(TextView) findViewById(R.id.oopen);
        bcv=(BreadcrumbsView) findViewById(R.id.breadcrumbs);
        knv=(TextView) findViewById(R.id.biodocnamev);
        bcv.setCurrentStep(0);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        knv.setText("Dr."+name);
        String strObj = getIntent().getStringExtra("biolist");
        try {
            JSONObject myJsonObj = null;
            myJsonObj = new JSONObject(strObj);
            String stats = myJsonObj.getString("status");
           addnj(stats);
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void addnj(String stats)
    {
        if (stats.matches("SD")) {
bcv.setCurrentStep(1);
            iop.setText("Strip Dispatched");

        }
        else if (stats.matches("SR"))
        {
            bcv.setCurrentStep(2);
            iop.setText("Strip Received");
        }
        else if (stats.matches("UP"))
        {
            bcv.setCurrentStep(3);
            iop.setText("UNDER PROCESS");
        }
        else {

            iop.setText("Waiting For Dispatch");
        }

    }

}
