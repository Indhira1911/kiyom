package com.v7ench.kiyo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ResetActivity extends AppCompatActivity {
EditText Eotp,Epass,Econpass;
    String Sotp,Spass,Sconpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        Eotp = (EditText) findViewById(R.id.otp);
        Epass =(EditText) findViewById(R.id.pass);
        Econpass =(EditText) findViewById(R.id.confirmpass);

        FloatingActionButton reset =(FloatingActionButton) findViewById(R.id.passsend);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sotp =Eotp.getText().toString();
                Spass=Epass.getText().toString();
                Sconpass=Econpass.getText().toString();
                if (Sotp.isEmpty()||Spass.isEmpty()||Sconpass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter all fields properly",Toast.LENGTH_SHORT).show();
                }
                else if (!(Spass.equals(Sconpass)))
                {
                    Toast.makeText(getApplicationContext(),"Password donot match!",Toast.LENGTH_SHORT).show();

                }
                else if (Spass.length()<6)
                {
                    Toast.makeText(getApplicationContext(),"Enter atleast 6 character password",Toast.LENGTH_SHORT).show();

                }
                else {
                    resetactivity(Sotp,Spass);
                }

            }
        });
    }
    public void resetactivity(final String sotp, final String spass)
    {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, UrlReq.RESETPASSC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            boolean error =jsonObject.getBoolean("error");
                            if (!error)
                            {
                                Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(ResetActivity.this, SignActivity.class);
                                startActivity(myIntent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Otp donot match",Toast.LENGTH_SHORT).show();
                                Epass.setText("");
                                Eotp.setText("");
                                Econpass.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check Your internet connection",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<String, String>();
                params.put("otp",sotp);
                params.put("pass",spass);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
