package com.v7ench.kiyo.scanneracti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;
import com.v7ench.kiyo.MainActivity;
import com.v7ench.kiyo.R;
import com.v7ench.kiyo.ScanAttach;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class TstScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tst_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

                Toast.makeText(getApplicationContext(), "Scan Successfull", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(TstScan.this, ScanAttach.class);
                myIntent.putExtra("sr",scanresult);
                startActivity(myIntent);

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

}
