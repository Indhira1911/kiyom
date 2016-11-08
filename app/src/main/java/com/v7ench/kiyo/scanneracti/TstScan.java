package com.v7ench.kiyo.scanneracti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.v7ench.kiyo.MainActivity;
import com.v7ench.kiyo.R;
import com.v7ench.kiyo.ScanAttach;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class TstScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tst_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mScannerView = (ZXingScannerView) findViewById(R.id.scanview);  // Programmatically initialize the scanner view
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();


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
        // show the scanner result into dialog box.

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setMessage(rawResult.getText());
        String scanresult=rawResult.getText();
        if (scanresult.contains("KI")) {
            scanima(scanresult);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid QR code", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(TstScan.this, MainActivity.class);
            startActivity(myIntent);
        }

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }

    public void  scanima(final String scanresult)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlReq.POSTTEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error)
                    {
                                             JSONObject user = jObj.getJSONObject("user");
                        String content = user.getString("content");
                        String pack = user.getString("pack");
                        String sterlizer = user .getString("sterlizer");
                        String sload = user .getString("sload");
                        String sdate = user.getString("sdate");
                        String dqr = user .getString("dqr");
                        String type = user .getString("type");
                        Toast.makeText(getApplicationContext(),content+pack+sterlizer+sload+sdate+dqr+type, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Scan Successfull", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(TstScan.this, ScanAttach.class);
                        myIntent.putExtra("sr",scanresult);
                        startActivity(myIntent);
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
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
