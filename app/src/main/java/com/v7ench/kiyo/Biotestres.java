package com.v7ench.kiyo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

public class Biotestres extends AppCompatActivity {
    ListView biore;
    private SQLiteHandler db;
    private ProgressDialog dialog;
    ImageView gghnj;
    TextView just_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biotestres);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        biore=(ListView) findViewById(R.id.biores);
        just_text=(TextView) findViewById(R.id.textview_poting);
        gghnj=(ImageView) findViewById(R.id.dsa);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String uid = user.get("uid");
        String ur = "http://gettalentsapp.com/vignesh2514/kiyo/androadmin/biores.php?uid="+uid;
        new JSONTask().execute(ur);
    }
    public class JSONTask extends AsyncTask<String,String, List<Biolistnew> > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected List<Biolistnew> doInBackground(String... params) {
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
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<Biolistnew> movieModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Biolistnew categorieslist = gson.fromJson(finalObject.toString(), Biolistnew.class);
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
        protected void onPostExecute(final List<Biolistnew> movieModelList) {
            super.onPostExecute(movieModelList);
            dialog.dismiss();
            if(movieModelList != null) {
               MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row_biores, movieModelList);
                biore.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
            if(biore.getCount()==0)
            {
                gghnj.setVisibility(View.VISIBLE);
                just_text.setVisibility(View.VISIBLE);
            }

        }

    }


    public class MovieAdapter extends ArrayAdapter {

        private List<Biolistnew> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        public MovieAdapter(Context context, int resource, List<Biolistnew> objects) {
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
                holder.clicknest=(ImageButton) convertView.findViewById(R.id.tstma);

                convertView.setTag(holder);

            }
            else {

                holder = (ViewHolder) convertView.getTag();

            }
            Biolistnew categorieslist= movieModelList.get(position);
            holder.conten.setText(categorieslist.getBiodqr());
            holder.tda.setText(categorieslist.getSdate());
            holder.tti.setText(categorieslist.getStime());
            holder.clicknest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Biolistnew categorieslist = movieModelList.get(position);
                    Intent intent = new Intent(Biotestres.this, BiotestResult.class);
                    intent.putExtra("biolist", new Gson().toJson(categorieslist));
                    startActivity(intent);
                }
            });
            return convertView;

        }

        class ViewHolder{
            private TextView conten,tda,tti;
ImageButton clicknest;

        }


    }


}