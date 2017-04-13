package com.v7ench.kiyo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v7ench.kiyo.dbhandler.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class tstresultview extends AppCompatActivity {
TextView precol,postcol,tstresu,qrsca,docname;
    private SQLiteHandler db;
    RelativeLayout ab,bc;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tstresultview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
imageView=(ImageView) findViewById(R.id.imageView7);
precol=(TextView)findViewById(R.id.precol_safe);
        postcol=(TextView) findViewById(R.id.post_col);
        tstresu=(TextView) findViewById(R.id.safe_result);
        qrsca=(TextView) findViewById(R.id.qr_scanned);
ab= (RelativeLayout) findViewById(R.id.pretest_safe);
        bc=(RelativeLayout) findViewById(R.id.postcolor_safe);
        String strObj = getIntent().getStringExtra("subcat");
        try {
            JSONObject myJsonObj = null;
            myJsonObj = new JSONObject(strObj);
            String content = myJsonObj.getString("content");
            String precolr = myJsonObj.getString("pre_colr");
            String postcolr=myJsonObj.getString("postcolor");
            String testresult=myJsonObj.getString("tresult");
            String qrs=myJsonObj.getString("dqr");
            addtextinview(content,precolr,postcolr,testresult,qrs);
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addtextinview(String content, String precolr, String Postcolr, String testresult,String qrs)
    {

        precol.setText(precolr);
        ab.setBackgroundColor(Color.parseColor(precolr));
        imageView.setBackgroundResource(R.drawable.pending_safe);
            postcol.setText(Postcolr);
            tstresu.setText(testresult);
        if (Postcolr.contains("#"))
        {
            bc.setBackgroundColor(Color.parseColor(Postcolr));

        }

        if (testresult.equalsIgnoreCase("SAFE"))
        {
            imageView.setBackgroundResource(R.drawable.safe_new);
            tstresu.setTextColor(Color.parseColor("#00a13a"));
        }
        else if(testresult.equalsIgnoreCase("UNSAFE"))
        {
            imageView.setBackgroundResource(R.drawable.unsafe_new);
            tstresu.setTextColor(Color.parseColor("#de2626"));
        }
        qrsca.setText(qrs);
    }

}
