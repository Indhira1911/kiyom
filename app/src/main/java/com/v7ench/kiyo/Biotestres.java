package com.v7ench.kiyo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.global.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Biotestres extends AppCompatActivity {
    ListView biore=null;
    private SQLiteHandler db;
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> Item_List;
    public static final String CONTENTT = "biodqr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biotestres);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        biore=(ListView) findViewById(R.id.biores);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String uid = user.get("uid");
        Item_List = new ArrayList<HashMap<String, String>>();
        ReadDataFromDB(uid);
    }


    private void ReadDataFromDB(String uid) {

        String ur = "http://vigneshintech.tk/kiyo/biores.php?uid=";

        String url=ur + uid;
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ja = response.getJSONArray("users");

                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject jobj = ja.getJSONObject(i);
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put(CONTENTT, jobj.getString(CONTENTT));


                                Item_List.add(item);

                            } // for loop ends

                            String[] from = {CONTENTT};
                            int[] to = {R.id.bior};

                            adapter = new SimpleAdapter(getApplicationContext(), Item_List,R.layout.row_biores, from, to);

                            biore.setAdapter(adapter);

//                                listview.setOnItemClickListener(new ListitemClickListener());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);

    }
}
