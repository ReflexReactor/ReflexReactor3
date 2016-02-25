package com.example.amit.reflexreactor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class scoreboard3 extends AppCompatActivity {

    String phn;
    String game_id;
    int score=200;
    ConnectionDetector connectionDetector;
    Boolean connection=true;
    JSONParser jsonParser=new JSONParser();
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard3);
        connectionDetector=new ConnectionDetector(this);
        Intent intent=getIntent();
        game_id=intent.getStringExtra("game_id");
        new UpdateRating().execute();
    }
    class UpdateRating extends AsyncTask<String, String, String> {

        int successResult=0;
        String msg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!connectionDetector.isConnectingToInternet())
            {
                Log.d("UpdateResult", "No connection");
                LaunchInternetActivity();
            }
            phn= GlobalUtils.getPhnNumber(context);
        }

        @Override
        protected String doInBackground(String... args) {

            if(connection) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phn", phn));
                params.add(new BasicNameValuePair("rating",""+score));


                JSONObject json = jsonParser.makeHttpRequest(GlobalUtils.url_update_rating,
                        "POST", params);

                try {
                    int success = json.getInt("success");
                    //   System.out.println("In multi_player.class " + json.toString());
                    Log.d("In scoreboard2.class ", json.toString() + ":end");
                    if (success == 1) {
                        successResult = 1;
                        msg = "Rating Updated successfully";
                        Log.d("UpdateSuccess1:","1");
                    } else {
                        successResult = 0;
                        msg = json.getString("message") + ";It's message";
                    }
                } catch (Exception e) {

                }


            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(connection)
            {
                if(successResult==1)
                {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void LaunchInternetActivity()
    {
        connection=false;
        Intent intent=new Intent(scoreboard3.this,InternetActivity.class);
        intent.putExtra("source", "scoreboard3");
        startActivity(intent);
        finish();
    }
}
