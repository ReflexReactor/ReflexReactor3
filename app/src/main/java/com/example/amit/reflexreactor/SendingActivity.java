package com.example.amit.reflexreactor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendingActivity extends AppCompatActivity {

    int level=1;
    Context context=this;
    ConnectionDetector connectionDetector;
    boolean connection=true;
    ProgressDialog pDialog;
    ArrayList<String> user_id;
    String selected_list[];
    int[] question_indexes;
    MyDatabaseAdapter dbasehelper;
    int catagories_id[];
    int idx;
    int score=0;
    JSONParser jsonParser=new JSONParser();
    public static int isAccepted=0;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);
        question_indexes = new int[6];
        Intent intent = getIntent();
        intent.getExtras();
        idx = intent.getIntExtra("idx", 0);
        user_id = intent.getStringArrayListExtra("user_id");
        level = intent.getIntExtra("level", 0);
        catagories_id = intent.getIntArrayExtra("catagories_id");
        selected_list = intent.getStringArrayExtra("catagories");
        question_indexes = intent.getIntArrayExtra("question_indexes");


    }
    class Send_Game_Request extends AsyncTask<String, String, String> {
        String msg;
        String phn;
        int successResult=0;
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            phn= GlobalUtils.getPhnNumber(context);
            super.onPreExecute();
            if(!connectionDetector.isConnectingToInternet())
            {
                Log.d("GetOnlineUSers","No connection");
                LaunchInternetActivity();

            }
            else
            {
                pDialog = new ProgressDialog(SendingActivity.this);
                pDialog.setMessage("Trying to send..");
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
                params.add(new BasicNameValuePair("sender_phn", phn));
                for (int i = 0; i < user_id.size(); i++) {
                    String tmp = "phn";
                    tmp = tmp + (i + 1);
                    params.add(new BasicNameValuePair(tmp, user_id.get(i)));
                }
                params.add(new BasicNameValuePair("level", "" + level));
                String category = "";
                for (int i = 0; i < idx; i++) {
                    String tmp = "cat";
                    tmp = tmp + (i + 1);
                    if (i > 0)
                        category = category + "," + selected_list[i];
                    else
                        category = category + selected_list[i];
                    params.add(new BasicNameValuePair(tmp, selected_list[i]));
                }
                params.add(new BasicNameValuePair("numberOfCategories", "" + idx));
                params.add(new BasicNameValuePair("category", category));
                params.add(new BasicNameValuePair("score", "" + score));

                String tmp1, tmp2;
                tmp1 = "";
                tmp2 = "";
                for (int i = 0; i < 6; i++) {
                    if (i == 0) {
                        tmp1 = tmp1 + catagories_id[i];
                        tmp2 = tmp2 + question_indexes[i];
                    } else {
                        tmp1 = tmp1 + "," + catagories_id[i];
                        tmp2 = tmp2 + "," + question_indexes[i];
                    }
                }

                params.add(new BasicNameValuePair("tables", tmp1));
                params.add(new BasicNameValuePair("rows", tmp2));
                // getting JSON string from URL
                JSONObject json = jsonParser.makeHttpRequest(GlobalUtils.url_send_game_request, "POST", params);

                // Check your log cat for JSON reponse
                Log.d("All Articles: ", json.toString());
                //System.out.println(json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt("success");

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        successResult = 1;
                        msg = "Request sent successfully";
                    } else {
                        msg = json.getString("message");
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

           // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
           int success=0;
            if(connection)
            {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       if(isAccepted==0&&count<15) {
                           count++;
                           handler.postDelayed(this, 1000);
                       }
                    }
                });
                pDialog.dismiss();
                if(isAccepted>0)
                {
                    LaunchNextActivity();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                }

            }

        }

    }
    public void LaunchInternetActivity()
    {
        connection=false;
        Intent intent=new Intent(SendingActivity.this,Questions.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("source", "OnlineListActivity");
        startActivity(intent);
        finish();
    }
    public void LaunchNextActivity()
    {
        Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_LONG);
    }
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
