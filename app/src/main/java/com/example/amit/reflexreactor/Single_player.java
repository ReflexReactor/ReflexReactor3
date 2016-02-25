package com.example.amit.reflexreactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class Single_player extends AppCompatActivity {


    int LEVEL=0;
    ArrayList<String> User_id;
    String[] user_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent  intent1;
        intent1 = getIntent();
        User_id = intent1.getStringArrayListExtra("user_id");
        user_names=intent1.getStringArrayExtra("user_names");
    }

    public void start_category()
    {
        Intent intent = new Intent(this,Category.class);
        intent.putExtra("MyLevel",LEVEL);
        intent.putExtra("user_id",User_id);
        intent.putExtra("user_names",user_names);
        startActivity(intent);
    }

    public void level1_function(View view) {
        LEVEL = 1;
        start_category();
    }
    public void level2_function(View view) {
        LEVEL = 2;
        start_category();
    }
    public void level3_function(View view) {
        LEVEL = 3;
        start_category();
    }
    public void level4_function(View view) {
        LEVEL = 4;
        start_category();
    }

}
