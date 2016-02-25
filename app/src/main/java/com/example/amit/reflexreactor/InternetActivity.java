package com.example.amit.reflexreactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class InternetActivity extends AppCompatActivity {

    String source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        source=intent.getStringExtra("source");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    public void tryAgain_action(View view)
    {
        ConnectionDetector connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet())
        {
            if(source.equals("OnlineListActivity")) {
                Intent intent = new Intent(InternetActivity.this, OnlineListActivity.class);
                startActivity(intent);
                finish();
            }
            else if(source.equals("Invites"))
            {
                Intent intent = new Intent(InternetActivity.this, Invites.class);
                startActivity(intent);
                finish();
            }
            else if(source.equals("Multi_player"))
            {
                Intent intent = new Intent(InternetActivity.this, Multi_player.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

}
