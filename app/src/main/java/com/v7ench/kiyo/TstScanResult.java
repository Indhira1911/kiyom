package com.v7ench.kiyo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        docname=(TextView) findViewById(R.id.biodocnamev);
        conte=(TextView) findViewById(R.id.content_textv);
        prcol=(TextView) findViewById(R.id.pretest_date);
        pocol=(TextView) findViewById(R.id.post_test_colorv);
        resulttst=(TextView) findViewById(R.id.test_result);
        scandqr=(TextView) findViewById(R.id.qr_scanned);
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        final String uid=user.get("uid");
         Intent details=getIntent();
        String content = details.getStringExtra("content");
        String dqr=details.getStringExtra("dqr");
        String pack = details.getStringExtra("pack");
        String sterlizer = details.getStringExtra("sterlizer");
        String sload = details.getStringExtra("sload");
        String sdate = details.getStringExtra("sdate");
        String dqrres = details.getStringExtra("dqres");
        String type = details.getStringExtra("type");
        String precolor = details.getStringExtra("precolor");
        String postcol=details.getStringExtra("postcol");
        scandqr.setText(dqrres);
        docname.setText("Dr."+name);
        pocol.setText(postcol);
        conte.setText(content);
        strfc(uid,dqr,postcol);

    }
    public void strfc(final String uid, final String dqr, final String postcol)
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.PREPOTST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String pre_colr = user.getString("pre_colr");
                        String post_colr = user.getString("post_colr");
                        if (postcol.equals(post_colr))
                        {
                            resulttst.setText("SAFE");
                            resulttst.setTextColor(Color.parseColor("#00a13a"));
                        }
                        else
                        {
                            resulttst.setText("UNSAFE");
                            resulttst.setTextColor(Color.parseColor("#de2626"));
                        }
                        prcol.setText(pre_colr);
                        String resf=resulttst.getText().toString();
                        ffres(uid,dqr,resf);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Server Busy",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }

        );
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void ffres(final String uid, final String dqr, final String resf){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.PRERESF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<String, String>();
                params.put("uid",uid);
                params.put("dqr",dqr);
                params.put("resf",resf);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(TstScanResult.this,MainActivity.class);
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
