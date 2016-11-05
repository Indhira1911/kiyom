package com.v7ench.kiyo;

import android.app.ProgressDialog;
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
import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.dbhandler.SessionManager;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {
    private SQLiteHandler db;
    private SessionManager session;
    EditText Eotpn;
    String Smonum,Sotpnum;
    FloatingActionButton otpreq;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        Smonum=user.get("pnum");
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
       Eotpn=(EditText) findViewById(R.id.otpnumber);
          otpreq =(FloatingActionButton) findViewById(R.id.otpsend);
                otpreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Sotpnum=Eotpn.getText().toString();
                if (Sotpnum.isEmpty())
                {

                    Toast.makeText(getApplicationContext(),"Enter Your otp",Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.show();
                    checkotp(Smonum,Sotpnum);

                }
            }
        });


    }
   public void checkotp(final String smonum, final String sotpnum)
   {

       StringRequest stringRequest =new StringRequest(Request.Method.POST, UrlReq.OTPCHECK,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {

                           if (response.equals("success")) {
                               dialog.dismiss();
                               Intent intent = new Intent(OtpActivity.this,MainActivity.class);
                               startActivity(intent);
                           }
                           else
                           {
                               Toast.makeText(getApplicationContext(), "Enter a valid OTP", Toast.LENGTH_SHORT).show();

                           }

                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),"Server Busy",Toast.LENGTH_SHORT).show();
           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> param =new HashMap<String, String>();
               param.put("monum",smonum);
               param.put("otp",sotpnum);
               return param;
           }
       };
       AppController.getInstance().addToRequestQueue(stringRequest);
   }

}
