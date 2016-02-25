package com.example.amit.reflexreactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Offline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void create_game(View view)
    {
        Intent intent = new Intent(this,Create_game.class);
        startActivity(intent);
    }
    public void join_game(View view)
    {
        Intent intent = new Intent(this,Join_game.class);
        startActivity(intent);
    }


}
