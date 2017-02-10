package com.v7ench.kiyo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.v7ench.kiyo.dbhandler.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TstResult extends AppCompatActivity {
ListView tst;
    private SQLiteHandler db;
    private ProgressDialog dialog;
ImageView gghnj;
    TextView just_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tst_result);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tst=(ListView) findViewById(R.id.tst_list);
        gghnj=(ImageView) findViewById(R.id.dsa);
        just_text=(TextView) findViewById(R.id.textview_poting);
        db = new SQLiteHandler(getApplicationContext());
       HashMap<String, String> user = db.getUserDetails();
        final String uid = user.get("uid");
           String ur = "http://gettalentsapp.com/vignesh2514/kiyo/androadmin/tstrescheck.php?uid="+uid;
        new JSONTask().execute(ur);

    }

    public class JSONTask extends AsyncTask<String,String, List<Categorieslist> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<Categorieslist> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("users");
                List<Categorieslist> movieModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Categorieslist categorieslist = gson.fromJson(finalObject.toString(), Categorieslist.class);
                    movieModelList.add(categorieslist);

                }
                return movieModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;

        }

        @Override
        protected void onPostExecute(final List<Categorieslist> movieModelList) {
            super.onPostExecute(movieModelList);
            dialog.dismiss();
            if(movieModelList != null) {
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row_tst, movieModelList);
                tst.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                tst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Categorieslist categorieslist = movieModelList.get(position);
                        Intent intent = new Intent(TstResult.this, tstresultview.class);
                        intent.putExtra("subcat", new Gson().toJson(categorieslist));
                        startActivity(intent);

                    }
                });
            }
/*
            else if (movieModelList == null){
                try {
                    movieModelList.addAll((List<Categorieslist>) cacheThis.readObject(TstResult.this, "helloall.txt"));
                    MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row_tst, movieModelList);
                    tst.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"I am here",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }*/
            if (tst.getCount()==0)
            {
                gghnj.setVisibility(View.VISIBLE);
                just_text.setVisibility(View.VISIBLE);
            }
        }

    }


    public class MovieAdapter extends ArrayAdapter {

        private List<Categorieslist> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        public MovieAdapter(Context context, int resource, List<Categorieslist> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context =context;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getViewTypeCount() {

            return 1;
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder  ;

            if(convertView == null){

                convertView = inflater.inflate(resource,null);
                holder = new ViewHolder();
                holder.conten=(TextView) convertView.findViewById(R.id.bconte);
                holder.tda=(TextView) convertView.findViewById(R.id.btdate);
                holder.tti=(TextView) convertView.findViewById(R.id.ttime);

                convertView.setTag(holder);

            }
            else {

                holder = (ViewHolder) convertView.getTag();

            }
            Categorieslist categorieslist= movieModelList.get(position);
            holder.conten.setText(categorieslist.getContent());
            holder.tda.setText(categorieslist.getSdate());
            holder.tti.setText("@"+categorieslist.getStme()+" hrs");
            return convertView;

        }

        class ViewHolder{
               private TextView conten,tda,tti;


        }


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(TstResult.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
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

}
