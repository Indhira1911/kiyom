package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.global.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Scan_history_all extends AppCompatActivity {
    private SQLiteHandler db;
    TextView tst_total_result,tst_safe,tst_unsafe,bi_total_result;
    ImageButton Itst,Ibi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String uid = user.get("uid");
       tst_total_result=(TextView) findViewById(R.id.testnum_tst);
        tst_safe=(TextView) findViewById(R.id.testnum_tst_safe);
        tst_unsafe=(TextView) findViewById(R.id.testnum_tst_unsafe);
bi_total_result=(TextView) findViewById(R.id.testnum_bi);
        Itst=(ImageButton) findViewById(R.id.tstma);
        Ibi=(ImageButton) findViewById(R.id.bima);
        String url="http://gettalentsapp.com/vignesh2514/kiyo/androadmin/fcmc.php?uid="+uid;
    backme(url);
        Itst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Scan_history_all.this, TstResult.class);
                startActivity(myIntent);
            }
        });
        Ibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Scan_history_all.this, Biotestres.class);
                startActivity(myIntent);
            }
        });
    }
    public void backme(String url)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                             JSONObject user = jObj.getJSONObject("user");
                    String safe = user.getString("safe");
                    String unsafe = user.getString("unsafe");
                    String totalsafe = user.getString("totalsafe");
                    String bitest = user.getString("bitest");

                    tst_total_result.setText(totalsafe+" Tests");
                    tst_safe.setText(safe+" Safe");
                    tst_unsafe.setText(unsafe+" UnSafe");
                    bi_total_result.setText(bitest+" Tests");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
