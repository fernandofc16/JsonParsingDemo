package br.com.fexus.jsonparsingdemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewMovies;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Carregando...");

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        listViewMovies = (ListView) findViewById(R.id.listViewMovies);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");
            }
        });

    }

    private ArrayList<Movies> callConnection(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder stringBuffer = new StringBuilder();
        Movies movieInfo;
        ArrayList<Movies> moviesList = new ArrayList<>();

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) { connection.disconnect(); }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String finalJSONString = stringBuffer.toString();
        try {
            JSONObject jsonObject = new JSONObject(finalJSONString);
            JSONArray jsonArray = jsonObject.getJSONArray("movies");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalJsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArrayCast = finalJsonObject.getJSONArray("cast");
                ArrayList<String> cast = new ArrayList<>();
                for(int j = 0; j < jsonArrayCast.length(); j++) {
                    JSONObject castJSONObject = jsonArrayCast.getJSONObject(j);
                    cast.add(castJSONObject.getString("name"));
                }
                Log.d("FEXUUUUUUUUUUUUUUUUUS: ", "VALUEEEEEEEEEEEEEE: " + (float) finalJsonObject.getDouble("rating"));
                movieInfo = new Movies( finalJsonObject.getString("movie"),
                                        finalJsonObject.getInt("year"),
                                        (float) finalJsonObject.getDouble("rating"),
                                        finalJsonObject.getString("duration"),
                                        finalJsonObject.getString("director"),
                                        finalJsonObject.getString("tagline"),
                                        cast,
                                        finalJsonObject.getString("image"),
                                        finalJsonObject.getString("story"));
                moviesList.add(movieInfo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviesList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONTask extends AsyncTask<String, String, ArrayList<Movies>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<Movies> doInBackground(String... strings) {
            return callConnection(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            MoviesAdapter adapter = new MoviesAdapter(getApplicationContext(), R.layout.row, result, getLayoutInflater());
            listViewMovies.setAdapter(adapter);
        }
    }

}
