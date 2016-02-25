package com.example.amit.reflexreactor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class Home extends AppCompatActivity {
    String PROJECT_NUMBER="546205713647";
    GCMClientManager pushClientManager;
    Activity context;
    boolean LaunchRegister=false;
    ProgressDialog pDialog;
    int failure=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new InitialiseGame().execute();
    }
    class InitialiseGame extends AsyncTask<String, String, String> {

        String msg;
        int successResult=0;
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Home.this);
            pDialog.setMessage("Initialising..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            super.onPreExecute();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            pushClientManager=new GCMClientManager(context, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {

                    Log.d("Registration id:", registrationId);
                    String reg_id;
                    reg_id = new String(registrationId);

                    if(isNewRegistration) {
                        //Updating the registration Id in a sharedPref object so that other activities in the app can use this
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_reg_id), reg_id);
                        editor.commit();
                        LaunchRegister=true;
                    }

                }

                @Override
                public void onFailure(String ex) {
                    Log.d("failure", "registration failed");
                    super.onFailure(ex);
               //     failure=1;


                }
            });
            return null;
        }

        protected void onPostExecute(String file_url) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
           // if(failure==1)
             //   finish();
            //else
                LaunchNextActivity();
        }
    }
    String getRegistrationId()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.saved_reg_id),"");
    }
    String getPhnNumber()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.saved_phn),"");
    }
    void LaunchNextActivity()
    {
        if(LaunchRegister)
        {
            Intent intent = new Intent(this, Register.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
