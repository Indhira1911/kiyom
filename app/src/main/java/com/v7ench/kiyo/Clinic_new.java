package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class Clinic_new extends AppCompatActivity {
    FloatingActionButton fab;
    TextView clinicmen, cliniaddr, clinicity, clinicpinc, clinicmo, clinicemai, formtext, clic_name;
    ImageView imaen;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Clinic_new.this, AddClinic.class);
                startActivity(intent);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clic_name = (TextView) findViewById(R.id.clinic_alpha_name);
        clinicmen = (TextView) findViewById(R.id.clininame_alpha);
        cliniaddr = (TextView) findViewById(R.id.clic_add);
        clinicity = (TextView) findViewById(R.id.city_alpa);
        clinicpinc = (TextView) findViewById(R.id.pincode_alpa);
        clinicmo = (TextView) findViewById(R.id.monumber_alpa);
        clinicemai = (TextView) findViewById(R.id.email_alpa);
        imaen = (ImageView) findViewById(R.id.imageView5);
        formtext = (TextView) findViewById(R.id.textView15);
        scrollView = (ScrollView) findViewById(R.id.clinic_alpha_scroll);
        fab.setVisibility(View.INVISIBLE);

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String uid = user.get("uid");
        clickme(uid);

    }

    public void clickme(final String uid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.CLICPRO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String cnamee = user.getString("cname");
                        String address = user.getString("address");
                        String city = user.getString("city");
                        String pincode = user.getString("pincode");
                        String phnum = user.getString("phnum");
                        String emailid = user.getString("emailid");

                        cbnc(cnamee, address, city, pincode, phnum, emailid);

                    } else {
                        scrollView.setVisibility(View.INVISIBLE);
                        imaen.setVisibility(View.VISIBLE);
                        formtext.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void cbnc(String cnamee, String address, String city, String pincode, String phnum, String emailid) {
        clic_name.setText(cnamee);
        clinicmen.setText(cnamee);
        cliniaddr.setText(address);
        clinicity.setText(city);
        clinicpinc.setText(pincode);
        clinicmo.setText(phnum);
        clinicemai.setText(emailid);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name_safe, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.edit_home_safe:
                Intent intent = new Intent(Clinic_new.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
