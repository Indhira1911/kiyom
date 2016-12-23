package com.v7ench.kiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BioTestScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private SQLiteHandler db;
    TextView uu,eda,eti;
    private int mYear, mMonth, mDay,thour,tminu,tseco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_test_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mScannerView = (ZXingScannerView) findViewById(R.id.scanview);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        uu=(TextView) findViewById(R.id.biot);
        eda=(TextView) findViewById(R.id.dayma);
        eti=(TextView) findViewById(R.id.tmema);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
         String uid = user.get("uid");
uu.setText(uid);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        thour=c.get(Calendar.HOUR);
        tminu=c.get(Calendar.MINUTE);
        tseco=c.get(Calendar.SECOND);
        mMonth=mMonth+1;
       eda.setText(mDay+"/"+mMonth+"/"+mYear);
        eti.setText(thour+":"+tminu+":"+tseco);
    }




    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        String scanresult=rawResult.getText();
        String uud= uu.getText().toString();
        String eday=eda.getText().toString();
String etim=eti.getText().toString();
        if (scanresult.contains("KIBI")) {
            scanima(scanresult,uud,eday,etim);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid QR code", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(BioTestScan.this, MainActivity.class);
            startActivity(myIntent);
        }

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }

    public void  scanima(final String scanresult,final  String uud, final  String eday,final  String etim)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.BIOSCAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        JSONObject user = jObj.getJSONObject("user");
                        String biodqr = user.getString("biodqr");
                        String sdate = user.getString("sdate");
                        String stime = user .getString("stime");
                        Intent intent =new Intent(BioTestScan.this,Biotestprofile.class);
                        intent.putExtra("biodqr",biodqr);
                        intent.putExtra("sdate",sdate);
                        intent.putExtra("stime",stime);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Scan Again", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("dqr",scanresult);
                params.put("uid",uud);
                params.put("eday",eday);
                params.put("etime",etim);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
