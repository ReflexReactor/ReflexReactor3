package com.example.amit.reflexreactor;

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

public class Home2 extends AppCompatActivity {

    String phn;
    Context context=this;
    JSONParser jsonParser=new JSONParser();
    String stored_reg_id;
    GCMClientManager pushClientManager;
    String reg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pushClientManager=new GCMClientManager(this, GlobalUtils.PROJECT_NUMBER);

        phn=GlobalUtils.getPhnNumber(this);
        new Check_Valid_Phn().execute();
    }
    class Check_Valid_Phn extends AsyncTask<String, String, String> {
        String msg;
        int successResult=0;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            phn=GlobalUtils.getPhnNumber(context);
            //reg_id=GlobalUtils.getRegistrationId(context);
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_phn", phn));

            //If called from onstart on is true else false


            JSONObject json = jsonParser.makeHttpRequest(GlobalUtils.url_check_valid_phn,
                    "POST", params);

            try {
                int success = json.getInt("success");

                if (success == 1) {
                    successResult = 1;
                    msg = "valid";
                    stored_reg_id=json.getString("reg_id");

                } else {
                    successResult = 0;
                }
            } catch (Exception e) {

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
//            Log.d("sucessresult", msg+":endMsg");
            if(successResult==1)
            {
                Log.d("Check_valid_phn","1");
                check_valid_reg_id();
            }
            else
            {
                save_reg_id();
                LaunchRegisterActivity();
            }

        }
    }
    void LaunchRegisterActivity()
    {
        Intent intent = new Intent(this, Register.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    void save_reg_id()
    {
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id:", registrationId);
                reg_id = new String(registrationId);
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.saved_reg_id), reg_id);
                    editor.commit();


            }

            @Override
            public void onFailure(String ex) {
                Log.d("failure", "registration failed");
                super.onFailure(ex);
                //     failure=1;

            }
        });
    }
    void check_valid_reg_id()
    {
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id:", registrationId);
                reg_id = new String(registrationId);
                if (isNewRegistration) {
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.saved_reg_id), reg_id);
                    editor.commit();
                }
                if (!stored_reg_id.equals(reg_id))
                    new Update_Reg_id().execute();
                else
                    LaunchMainActivity();

            }

            @Override
            public void onFailure(String ex) {
                Log.d("failure", "registration failed");
                super.onFailure(ex);
                //     failure=1;

            }
        });

    }
    class Update_Reg_id extends AsyncTask<String, String, String> {
        String msg;
        int successResult=0;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //reg_id=GlobalUtils.getRegistrationId(context);
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_phn", phn));
            params.add(new BasicNameValuePair("reg_id",reg_id));


            //If called from onstart on is true else false


            JSONObject json = jsonParser.makeHttpRequest(GlobalUtils.url_update_reg_id,
                    "POST", params);

            try {
                int success = json.getInt("success");

                if (success == 1) {
                    successResult = 1;
                    msg = "updated";


                } else {
                    successResult = 0;
                    msg=json.getString("message");
                }
            } catch (Exception e) {

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
//            Log.d("sucessresult", msg+":endMsg");
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
            if(successResult==1) {
                LaunchMainActivity();
            }

        }
    }
    void LaunchMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
