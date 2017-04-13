package com.v7ench.kiyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.v7ench.kiyo.R.id.email;

public class SignupActivity extends android.support.v4.app.Fragment {
EditText Ename,Emono,Eemail,Epass;
    String Sname,Semail,Smono,Spass,Stype;
    FloatingActionButton signup;
    private SessionManager session;
    private SQLiteHandler db;
    Spinner delarea;
    private ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_signup, container, false);
        Ename=(EditText) v.findViewById(R.id.name);
        Eemail=(EditText) v.findViewById(email);
        Emono =(EditText) v.findViewById(R.id.mobilenumber);
        Epass =(EditText) v.findViewById(R.id.password);
        delarea=(Spinner) v.findViewById(R.id.deliarea);

        signup =(FloatingActionButton) v.findViewById(R.id.signup);
        final String[] list = getResources().getStringArray(R.array.darea);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list);

        delarea.setAdapter(adapter);
        session = new SessionManager(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        // SQLite database handler
        db = new SQLiteHandler(getActivity());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Stype =delarea.getSelectedItem().toString();
                Sname=Ename.getText().toString();
                Semail=Eemail.getText().toString();
                Smono=Emono.getText().toString();
                Spass=Epass.getText().toString();
                if (Sname.isEmpty()||Semail.isEmpty()||Smono.isEmpty()||Spass.isEmpty())
                {
                    Toast.makeText(getContext(),"Please fill all fields!",Toast.LENGTH_SHORT).show();
                }
                else if ( !Semail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
                {
                    Toast.makeText(getContext(),"Enter an valid email id", Toast.LENGTH_SHORT).show();
                }

                else if (Smono.length()<10)
                {
                    Toast.makeText(getContext(),"Mobile Number is invalid",Toast.LENGTH_SHORT).show();

                }
                else if ( Spass.length()<6)
                {
                    Toast.makeText(getContext(),"Enter atleast 6 character password", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.show();
                   signup(Sname,Semail,Smono,Spass,Stype);

                }
            }
        });
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    public  void signup(final String sname, final String semail, final String smono, final String spass,final String stype)
    {
        String tag_string_req = "req_register";
        StringRequest stringRequest =new StringRequest(Request.Method.POST, UrlReq.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                session.setLogin(true);
                                String uid = jObj.getString("uid");
                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String pnum =user.getString("pnum");
                                String whoty=user.getString("type");
                                // Inserting row in users table



                                dialog.dismiss();
                                Toast.makeText(getContext(), "Otp has been sent to registered mobile number.!", Toast.LENGTH_LONG).show();
                                db.addUser(name, email, uid,pnum);
                                // Launch login activity
                                Intent intent = new Intent(getActivity(), OtpActivity.class);
                                startActivity(intent);


                            }
                            else {

                                // Error occurred in registration. Get the error
                                // message
                                dialog.dismiss();
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getContext(),"Check your internet connection", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("name",sname);
                params.put("email",semail);
                params.put("pnum",smono);
                params.put("password",spass);
                params.put("type",stype);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
    }
}
