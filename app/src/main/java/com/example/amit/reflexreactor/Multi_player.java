package com.example.amit.reflexreactor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Multi_player extends AppCompatActivity {

    String phn;
    String reg_id;
    Context context;
    JSONParser jsonParser=new JSONParser();
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;

    }
    public void online_start(View view)
    {
        //First check for internet connectivity
        /*..Put the code here....*/

        //If connected then Launch next activity
        Intent intent = new Intent(this,OnlineListActivity.class);
        startActivity(intent);
    }
    public void offline_start(View view)
    {
        Intent intent = new Intent(this,Offline.class);
        startActivity(intent);
    }

}
