package com.example.amit.reflexreactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class Online extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("OnlineActivity","1");
        String user_id = "9999999999";
        // Show list of Users (List View)

        //onclick save user id

        /*Intent intent = new Intent(this, Single_player.class);
        intent.putExtra("user_id",user_id);
        startActivity(intent);*/


    }

}
