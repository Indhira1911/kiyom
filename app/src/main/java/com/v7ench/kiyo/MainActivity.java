package com.v7ench.kiyo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.v7ench.kiyo.dbhandler.SQLiteHandler;
import com.v7ench.kiyo.dbhandler.SessionManager;
import com.v7ench.kiyo.scanneracti.TstScan;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SQLiteHandler db;
    private SessionManager session;
    int PERMISSION_ALL = 1;
    TextView sa,ua,ra;
   // ListView mst;

    ImageButton Itst,Ibi;
    ImageView noim;
    RatingBar rateme;
        private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  mst=(ListView) findViewById(R.id.mmn);
        sa=(TextView) findViewById(R.id.safe_num);
        ua=(TextView) findViewById(R.id.unsafe_num);
ra=(TextView) findViewById(R.id.rate_txt);
        Itst=(ImageButton) findViewById(R.id.tstma);
       // Ibi=(ImageButton) findViewById(R.id.bima);
        db = new SQLiteHandler(getApplicationContext());
rateme=(RatingBar) findViewById(R.id.rating_main);
        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        final String email = user.get("email");
        final String uid = user.get("uid");
        String[] PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
//        String url="http://gettalentsapp.com/vignesh2514/kiyo/androadmin/fcmc.php?uid="+uid;
//    backme(url);
        Itst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, TstScan.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
//        Ibi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(MainActivity.this, BioTestScan.class);
//                MainActivity.this.startActivity(myIntent);
//            }
//        });
//        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
//        actionA.setTitle("BIO Test");
//        actionA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(MainActivity.this, BioTestScan.class);
//                MainActivity.this.startActivity(myIntent);
//            }
//        });
//        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
//        actionB.setTitle("TST Test");
//
//        actionB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(MainActivity.this, TstScan.class);
//                MainActivity.this.startActivity(myIntent);
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
       // setFloatingButtonControls();

        TextView Docname =(TextView) header.findViewById(R.id.docname);
       // TextView Docmail=(TextView) header.findViewById(R.id.docemail);
        Docname.setText("Welcome "+name+",");
       // Docmail.setText(email);
       /* String ur = "http://gettalentsapp.com/vignesh2514/kiyo/androadmin/tstrescheck.php?uid="+uid;
        new JSONTask().execute(ur);*/
    }
//    public void backme(String url)
//    {
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                             JSONObject user = jObj.getJSONObject("user");
//                    String safe = user.getString("safe");
//                    String unsafe = user.getString("unsafe");
//                      sa.setText(safe);
//                    ua.setText(unsafe);
//           int isafe=Integer.parseInt(sa.getText().toString());
//                    int iunsafe=Integer.parseInt(ua.getText().toString());
//                    int toti=isafe+iunsafe;
//                    if (toti==0)
//                    {ra.setText("0.0");             }
//                   else if (isafe==toti)
//                    {
//                        ra.setText("5.0");
//                    }
//                    else {
//                        double prefinali=((double)isafe)/toti;
//                        double finali=prefinali*5;
//                     String fin=  String.format("%.2f",finali);
//                        ra.setText(fin);
//                        rateme.setRating(Float.parseFloat(fin));
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        AppController.getInstance().addToRequestQueue(stringRequest);
//
//    }

//    private void setFloatingButtonControls(){
//       final View bckgroundDimmer = findViewById(R.id.background_dimmer);
//        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
//        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
//            @Override
//            public void onMenuExpanded() {
//                bckgroundDimmer.setVisibility(View.VISIBLE);
//                bckgroundDimmer.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menuMultipleActions.collapse();
//                    }
//                });
//            }
//
//            @Override
//            public void onMenuCollapsed() {
//                bckgroundDimmer.setVisibility(View.GONE);
//            }
//        });
//
//    }

/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu_name_safe) {
        // Inflate the menu_name_safe; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu_name_safe.main, menu_name_safe);
        return true;
    }
*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Settingsall.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_clinic) {
            Intent intent = new Intent(MainActivity.this, Clinic_new.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_tsttest) {
            Intent intent = new Intent(MainActivity.this, Strat_test_test.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_bitest) {
//            Intent intent = new Intent(MainActivity.this, Strart_Bi_test.class);
//            startActivity(intent);
//        }
        else if (id == R.id.nav_scanhis) {
            Intent intent = new Intent(MainActivity.this, Scan_history_all.class);
            startActivity(intent);
        }
//        Intent intent = new Intent(MainActivity.this, Tstresultsum.class);
//        startActivity(intent);
// else if (id == R.id.nav_bitest) {
//            Intent intent = new Intent(MainActivity.this, BiotestNew.class);
//            startActivity(intent);
//        }
else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_settings){
//            Intent intent = new Intent(MainActivity.this, Settingsall.class);
//            startActivity(intent);
//        }
       else if (id == R.id.nav_question)
       {
           Intent intent = new Intent(MainActivity.this, About_Us.class);
           startActivity(intent);
        }
        else if (id==R.id.nav_logout)
        {
            logoutUser();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, SignActivity.class);
        startActivity(intent);
        finish();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    Boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    public class JSONTask extends AsyncTask<String,String, List<Categorieslist> > {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected List<Categorieslist> doInBackground(String... params) {
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//
//            try {
//                URL url = new URL(params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                InputStream stream = connection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuffer buffer = new StringBuffer();
//                String line ="";
//                while ((line = reader.readLine()) != null){
//                    buffer.append(line);
//                }
//
//                String finalJson = buffer.toString();
//
//                JSONObject parentObject = new JSONObject(finalJson);
//                JSONArray parentArray = parentObject.getJSONArray("users");
//                List<Categorieslist> movieModelList = new ArrayList<>();
//                Gson gson = new Gson();
//                for(int i=0; i<parentArray.length(); i++) {
//                    JSONObject finalObject = parentArray.getJSONObject(i);
//                    if (finalObject.getString("type").contains("pretest")) {
//                        Categorieslist categorieslist = gson.fromJson(finalObject.toString(), Categorieslist.class);
//                        movieModelList.add(categorieslist);
//                    }
//                }
//                return movieModelList;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                if(connection != null) {
//                    connection.disconnect();
//                }
//                try {
//                    if(reader != null) {
//                        reader.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return  null;
//
//        }
//
//        @Override
//        protected void onPostExecute(final List<Categorieslist> movieModelList) {
//            super.onPostExecute(movieModelList);
//noim=(ImageView) findViewById(R.id.noima);
//
//            TextView tesaac=(TextView) findViewById(aac);
//            if(movieModelList != null) {
//                tesaac.setVisibility(View.VISIBLE);
//                noim.setVisibility(View.GONE);
//                mst.setVisibility(View.VISIBLE);
//                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row_tst, movieModelList);
//                mst.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//                mst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Categorieslist categorieslist = movieModelList.get(position);
//                        Intent intent = new Intent(MainActivity.this, tstresultview.class);
//                        intent.putExtra("subcat", new Gson().toJson(categorieslist));
//                        startActivity(intent);
//
//                    }
//                });
//            }
//            if (mst.getCount()==0)
//            {
//                noim.setVisibility(View.VISIBLE);
//                tesaac.setVisibility(View.GONE);
//            }
//
//        }
//
//    }
//
//
//    public class MovieAdapter extends ArrayAdapter {
//
//        private List<Categorieslist> movieModelList;
//        private int resource;
//        Context context;
//        private LayoutInflater inflater;
//        public MovieAdapter(Context context, int resource, List<Categorieslist> objects) {
//            super(context, resource, objects);
//            movieModelList = objects;
//            this.context =context;
//            this.resource = resource;
//            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        }
//        @Override
//        public int getViewTypeCount() {
//
//            return 1;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//
//            final ViewHolder holder  ;
//
//            if(convertView == null){
//
//                convertView = inflater.inflate(resource,null);
//                holder = new ViewHolder();
//                holder.conten=(TextView) convertView.findViewById(R.id.bconte);
//                holder.tda=(TextView) convertView.findViewById(R.id.btdate);
//                holder.tti=(TextView) convertView.findViewById(R.id.ttime);
//                convertView.setTag(holder);
//
//            }
//            else {
//
//                holder = (ViewHolder) convertView.getTag();
//
//            }
//            Categorieslist categorieslist= movieModelList.get(position);
//            holder.conten.setText(categorieslist.getContent());
//            holder.tda.setText(categorieslist.getSdate());
//            holder.tti.setText("@"+categorieslist.getStme()+" hrs");
//            return convertView;
//
//        }
//
//        class ViewHolder{
//            private TextView conten,tda,tti;
//
//
//        }
//
//
//    }

}
