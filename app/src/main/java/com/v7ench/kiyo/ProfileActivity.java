package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class ProfileActivity extends AppCompatActivity {
    ListView tsti;

    TextView cname,cadd,cnum,cemail,proname;
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
        proname = (TextView) findViewById(R.id.clinic_alpha_name);
bnkj=(LinearLayout) findViewById(R.id.gmg);

        cname=(TextView) findViewById(R.id.clininame_alpha);
        cnum=(TextView) findViewById(R.id.mobile_safe_alpha);
        cemail=(TextView) findViewById(R.id.email_id_profile);
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String uid=user.get("uid");
        String monum=user.get("pnum");
        String emmail=user.get("email");
        cname.setText("Dr." +  name);
        proname.setText("Dr." + name);
        cnum.setText(monum);
        cemail.setText(emmail);

        String url="http://gettalentsapp.com/vignesh2514/kiyo/androadmin/fcmc.php?uid="+uid;
        backme(url);


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
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}