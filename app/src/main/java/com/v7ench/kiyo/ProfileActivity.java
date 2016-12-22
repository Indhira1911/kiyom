package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class ProfileActivity extends AppCompatActivity {
    ListView tsti;
    TextView proname,sal,unsal,rati;
    TextView cname,cadd,cnum,cemail;
Button addclin;
LinearLayout bnkj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tsti = (ListView) findViewById(R.id.tst_list);
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        proname = (TextView) findViewById(R.id.mname);
bnkj=(LinearLayout) findViewById(R.id.gmg);
        sal=(TextView) findViewById(R.id.safep);
        rati=(TextView) findViewById(R.id.ratings);
        unsal=(TextView) findViewById(R.id.textView18);
        addclin=(Button) findViewById(R.id.addcli);
        cname=(TextView) findViewById(R.id.clicname);
        cadd=(TextView) findViewById(R.id.clicadd);
        cnum=(TextView) findViewById(R.id.clinephn);
        cemail=(TextView) findViewById(R.id.clicmail);
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String uid=user.get("uid");
        proname.setText("Dr." + name);
        String url="http://gettalentsapp.com/vignesh2514/kiyo/androadmin/fcmc.php?uid="+uid;
        backme(url);
        addclin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,AddClinic.class);
                startActivity(intent);
            }
        });
        clickme(uid);
    }

    public void backme(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject user = jObj.getJSONObject("user");
                    String safe = user.getString("safe");
                    String unsafe = user.getString("unsafe");
                    sal.setText(safe);
                    unsal.setText(unsafe);
                    int isafe = Integer.parseInt(safe);
                    int iunsafe = Integer.parseInt(unsafe);
                    int toti = isafe + iunsafe;
                    double prefinali=((double)isafe)/toti;
                    double finali=prefinali*5;
                    if (toti==0)
                    {rati.setText("0.0");             }
                    else if (isafe==toti)
                    {
                        rati.setText("5.0");
                    }
                    else {
                        String fin = String.format("%.2f", finali);
                        rati.setText(fin);
                    }
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
    public void clickme(final String uid)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.CLICPRO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                    JSONObject user = jObj.getJSONObject("user");
                   String cnamee=user.getString("cname");
                    String address=user.getString("address");
                    String city =user.getString("city");
                    String pincode=user.getString("pincode");
                    String phnum=user.getString("phnum");
                    String emailid=user.getString("emailid");
                        bnkj.setVisibility(View.VISIBLE);
                        cbnc(cnamee, address, city, pincode, phnum, emailid);
                        addclin.setVisibility(View.INVISIBLE);

                                                                  }
                    else {

                        addclin.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<String, String>();
                params.put("uid",uid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void cbnc(String cnamee, String address, String city, String pincode, String phnum, String emailid)
    {
cname.setText(cnamee);
        cadd.setText(address+" "+" "+ city+" Pincode:"+pincode );
        cnum.setText("Mobile No:"+phnum);
        cemail.setText("Email id:"+emailid);
    }

}