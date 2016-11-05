package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgotpass1 extends AppCompatActivity {
EditText rnum;
    String Srnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgorpass1);
        rnum=(EditText) findViewById(R.id.editText);

        FloatingActionButton flot=(FloatingActionButton) findViewById(R.id.fpsend);
        flot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Srnm =rnum.getText().toString();
                if(Srnm.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
                }
                else if (Srnm.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Mobile number is not valid",Toast.LENGTH_SHORT).show();
                }
                else {

                    resetnumber(Srnm);
                }
            }
        });
    }
    public void resetnumber(final String srnm)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.RESETPASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        Toast.makeText(getApplicationContext(),"Otp has been sent to your registered mobile number",Toast.LENGTH_SHORT).show();
                      Intent myIntent = new Intent(Forgotpass1.this, ResetActivity.class);
                 startActivity(myIntent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Number has not been registered!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check your internet connection!",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params= new HashMap<String,String>();
                params.put("pnum",srnm);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
