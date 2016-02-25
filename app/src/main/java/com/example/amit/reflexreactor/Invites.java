package com.example.amit.reflexreactor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Invites extends AppCompatActivity {
    ProgressDialog pDialog;
    JSONParser jsonParser=new JSONParser();
    String phn;
    Context context=this;
    JSONArray requests;
    String[] game_ids=new String[0];
    String[] sender_names=new String[0];
    ListView listview;
    ConnectionDetector connectionDetector;
    boolean connection=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview=(ListView)findViewById(R.id.list_requests);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String game_id=game_ids[(int)id];
                Intent intent = new Intent(context, RequestActivity.class);
                intent.putExtra("game_id",game_id);
                intent.putExtra("sender_name",sender_names[(int)id]);
                startActivity(intent);

            }
        });
        connectionDetector=new ConnectionDetector(this);

        new GetRequests().execute();

    }
    class GetRequests extends AsyncTask<String, String, String> {
        String msg;
        int successResult=0;
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            if(!connectionDetector.isConnectingToInternet())
            {
                Log.d("GetOnlineUSers","No connection");
                // Toast.makeText(context,"No Internet Connection!!",Toast.LENGTH_LONG);
                connection=false;
                Intent intent=new Intent(Invites.this,InternetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("source","Invites");
                startActivity(intent);
                finish();

            }
            else {
                pDialog = new ProgressDialog(Invites.this);
                pDialog.setMessage("Fetching Requests..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                phn = GlobalUtils.getPhnNumber(context);
                super.onPreExecute();
            }
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            if(connection) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phn", phn));
                // getting JSON string from URL
                JSONObject json = jsonParser.makeHttpRequest(GlobalUtils.url_get_all_requests, "POST", params);

                // Check your log cat for JSON reponse
                Log.d("All Requests: ", json.toString());
                System.out.println(json.toString());
                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt("success");

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        requests = json.getJSONArray("requests");
                        // looping through All Products

                        System.out.println("working fine until now");

                        sender_names = new String[requests.length()];
                        game_ids = new String[requests.length()];

                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);
                            // Storing each json item in variable
                            String g_id = c.getString("game_id");
                            String s_name = c.getString("sender_name");
                            sender_names[i] = s_name;
                            game_ids[i] = g_id;

                        }
                        successResult = 1;
                        if(requests.length()==0)
                        {
                            successResult = 2;
                            msg = "No requests pending";
                        }

                    } else if (success == 2) {


                        successResult = 2;
                        msg = "No requests pending";
                    } else {
                        successResult = 0;
                        msg = json.getString("message");
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if(connection) {
                pDialog.dismiss();
                if (successResult != 1) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed JSON data into ListView
                             * */
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sender_names);
                            listview.setAdapter(adapter);
                        }
                    });
                }
            }
        }

    }
}
