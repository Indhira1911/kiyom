package com.v7ench.kiyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.dbhandler.SessionManager;
import com.v7ench.kiyo.global.AppController;
import com.v7ench.kiyo.global.UrlReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends android.support.v4.app.Fragment {
private EditText Emobno,Epass;

    String Smobno,Spass;
    private ProgressDialog dialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_signin, container, false);
         Emobno =(EditText) v.findViewById(R.id.mobileno);
        Epass =(EditText) v.findViewById(R.id.password);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        // SQLite database handler
        db = new SQLiteHandler(getContext());

        // Session manager
        session = new SessionManager(getContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        }

        FloatingActionButton send = (FloatingActionButton) v.findViewById(R.id.siginsend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Smobno=Emobno.getText().toString();
                Spass =Epass.getText().toString();
                if (Smobno.isEmpty()||Spass.isEmpty())
                {
                    Toast.makeText(getContext(),"Please fill all fields!",Toast.LENGTH_SHORT).show();
                }
                else if (Smobno.length()<10)
                {
                    Toast.makeText(getContext(),"Mobile Number is invalid",Toast.LENGTH_SHORT).show();

                }

                else {
                    dialog.show();
                    checkLogin(Smobno,Spass);

                }
            }
        });
        Button fp =(Button) v.findViewById(R.id.button);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Forgotpass1.class);
                startActivity(intent);
            }
        });
        return v;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    private void checkLogin(final String smobno, final String spass) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlReq.SIGNIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        dialog.dismiss();
                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String pnum = user .getString("pnum");
                        String passme = user .getString("passme");
                        String totstrip = user .getString("totstrip");
                        // Inserting row in users table
                        db.addUser(name, email, uid, pnum);
if (passme.matches("1")) {
    // Launch main activity
    Intent intent = new Intent(getActivity(), MainActivity.class);
    intent.putExtra("totstrip",totstrip);
    startActivity(intent);
}
else {
    Intent intent = new Intent(getActivity(), OtpActivity.class);
    startActivity(intent);
}

                    } else {
                        dialog.dismiss();
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
dialog.dismiss();
                Toast.makeText(getContext(),"CHECK YOUR INTERNET CONNECTION", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pnum", smobno);
                params.put("password", spass);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



}
