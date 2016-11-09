package com.v7ench.kiyo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScanAttach extends AppCompatActivity implements
        View.OnClickListener {
    private int mYear, mMonth, mDay,thour,tminu,tseco;
    EditText Edat,Econtent,Epack,Esterli,Eload;
    TextView Tdqr;
    ImageButton dateselect,savescn;
    private ProgressDialog dialog;
    String Scontent,Spack,Ssterli,Sload,Sdat,Stype,Stme;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_attach);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
              getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(getApplicationContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String uid = user.get("uid");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        thour=c.get(Calendar.HOUR);
        tminu=c.get(Calendar.MINUTE);
        tseco=c.get(Calendar.SECOND);
        Edat=(EditText) findViewById(R.id.Edate);
        Edat.setText(mDay+"/"+mMonth+"/"+mYear);
        Econtent=(EditText) findViewById(R.id.content);
        Epack=(EditText) findViewById(R.id.pack);
        Esterli=(EditText) findViewById(R.id.sterilizer);
        Eload=(EditText) findViewById(R.id.load);
         dateselect=(ImageButton) findViewById(R.id.dateselectn);
        dateselect.setOnClickListener(this);
        savescn=(ImageButton) findViewById(R.id.savescan);
        Tdqr=(TextView) findViewById(R.id.qrscani);
        Intent details=getIntent();
            final String Sdqr=details.getStringExtra("sr");
        Tdqr.setText(Sdqr);
        Ssterli=Sdqr.substring(2,4);
        if (Ssterli.equals("ST"))
        {
            Esterli.setText("STEAM");
        }
        else if(Ssterli.equals("GA"))
        {
            Esterli.setText("GAS");
        }
        else if (Ssterli.equals("RA"))
        {
            Esterli.setText("RADIATION");

        }

           savescn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Scontent=Econtent.getText().toString();
                Spack=Epack.getText().toString();
                Ssterli=Esterli.getText().toString();
                Sload=Eload.getText().toString();
                Sdat=Edat.getText().toString();
                Stype="pretest";
                Stme=Integer.toString(thour)+":"+Integer.toString(tminu)+":"+Integer.toString(tseco);
if (Scontent.isEmpty()&&Ssterli.isEmpty()&&Sdat.isEmpty())
{

    Toast.makeText(getApplicationContext(),"Please fill all fields",Toast.LENGTH_SHORT).show();
}
else
{
    prescan(uid,Scontent,Spack,Ssterli,Sload,Sdat,Stype,Sdqr,Stme);
}
            }
        });
    }

public void prescan(final String uid, final String scontent, final String spack, final String ssterli, final String sload, final String sdat, final String stype, final String sdqr, final String stme)
{

    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.PRETEST, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject =new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error)
                {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Pre test scan has been successfull",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScanAttach.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    dialog.dismiss();
                    String errorMsg = jsonObject.getString("error_msg");
                    Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),"Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params= new HashMap<String, String>();
            params.put("uid",uid);
            params.put("content",scontent);
            params.put("pack",spack);
            params.put("sterli",ssterli);
            params.put("sload",sload);
            params.put("sdate",sdat);
            params.put("dqr",sdqr);
            params.put("type",stype);
            params.put("stme",stme);
            return params;
        }
    };
    AppController.getInstance().addToRequestQueue(stringRequest);
}
    @Override
    public void onClick(View v) {

             DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Edat.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
