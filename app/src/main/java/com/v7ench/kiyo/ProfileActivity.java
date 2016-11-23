package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
    private SQLiteHandler db;
    TextView proname,sal,unsal,rati;
Button addclin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tsti = (ListView) findViewById(R.id.tst_list);
        db = new SQLiteHandler(getApplicationContext());
        proname = (TextView) findViewById(R.id.mname);
        sal=(TextView) findViewById(R.id.safep);
        rati=(TextView) findViewById(R.id.ratings);
        unsal=(TextView) findViewById(R.id.textView18);
        addclin=(Button) findViewById(R.id.addcli);
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String uid=user.get("uid");
        proname.setText("DR." + name);
        String url="http://vigneshintech.tk/kiyo/fcmc.php?uid="+uid;
        backme(url);
        addclin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,AddClinic.class);
                startActivity(intent);
            }
        });
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
                    sal.setText("Safe:"+safe);
                    unsal.setText("Unsafe:"+unsafe);
                    int isafe = Integer.parseInt(safe);
                    int iunsafe = Integer.parseInt(unsafe);
                    int toti = isafe + iunsafe;
                    int prefinali = (isafe) / toti;
                    double finali = prefinali * 5;
                    rati.setText("Ratings:"+Double.toString(finali));
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