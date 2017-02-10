package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.HashMap;
import java.util.Map;

public class AddClinic extends AppCompatActivity {
TextView cli,addre,ccit,cphnu,cemai,cpinc;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String uid=user.get("uid");
        cli=(TextView) findViewById(R.id.clinicname);
        addre=(TextView) findViewById(R.id.address);
        ccit=(TextView) findViewById(R.id.ccity);
        cphnu=(TextView) findViewById(R.id.phnumber);
        cemai=(TextView) findViewById(R.id.emailid);
        cpinc=(TextView) findViewById(R.id.pincode);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clinicn=cli.getText().toString();
                String address=addre.getText().toString();
                String city=ccit.getText().toString();
                String phnum=cphnu.getText().toString();
                String cemail=cemai.getText().toString();
                String pinc=cpinc.getText().toString();
                if (clinicn.isEmpty()||address.isEmpty()||city.isEmpty()||phnum.isEmpty()||cemail.isEmpty()||pinc.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"PLease fill all feilds",Toast.LENGTH_SHORT).show();
                }
                else {
                    addcclinic(uid,clinicn,address,city,phnum,cemail,pinc);
                }

            }
        });
    }
    public void addcclinic(final String uid, final String clinicn, final String address, final String city, final String phnum, final String cemail, final String pinc)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.CLINICC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent= new Intent(AddClinic.this,ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<String,String>();
                params.put("clinic",clinicn);
                params.put("address",address);
                params.put("uid",uid);
                params.put("city",city);
                params.put("phnum",phnum);
                params.put("cemail",cemail);
                params.put("pinc",pinc);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
