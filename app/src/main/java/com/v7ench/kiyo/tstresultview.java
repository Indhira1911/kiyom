package com.v7ench.kiyo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.v7ench.kiyo.dbhandler.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class tstresultview extends AppCompatActivity {
TextView cont,precol,postcol,tstresu,qrsca,docname;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tstresultview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        docname=(TextView) findViewById(R.id.biodocnamev);
        cont=(TextView) findViewById(R.id.content_textv);
precol=(TextView)findViewById(R.id.pretest_date);
        postcol=(TextView) findViewById(R.id.post_test_colorv);
        tstresu=(TextView) findViewById(R.id.test_resultv);
        qrsca=(TextView) findViewById(R.id.qr_scanned);
        docname.setText("DR."+name);
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

    public void addtextinview(String content, String precolr, String postcolr, String testresult,String qrs)
    {
cont.setText(content);
        precol.setText(precolr);
        if (!postcolr.isEmpty()) {
            postcol.setText(postcolr);
            tstresu.setText(testresult);
        }
        else {
            postcol.setText("Test pending");
            tstresu.setText("Result Pending");
        }

        if (testresult.equals("SAFE"))
        {
            tstresu.setTextColor(Color.parseColor("#00a13a"));
        }
        else if(testresult.equals("UNSAFE"))
        {

            tstresu.setTextColor(Color.parseColor("#de2626"));
        }
        qrsca.setText(qrs);
    }

}
