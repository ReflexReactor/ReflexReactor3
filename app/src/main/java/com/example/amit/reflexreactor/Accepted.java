package com.example.amit.reflexreactor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Accepted extends AppCompatActivity {

    int p=1;
    ArrayList<String> My_question;
    MyDatabaseAdapter dbasehelper;
    MyDatabaseAdapter.MyHelper helper;
    CountDownTimer cTimer;
    TextView que_no,ques,timer;
    Button optnA,optnB,optnC,optnD;
    int[] catagories_id;
    int[] questions_id;
    String[] selected_list;
    int level;
    String game_id;
    int score=0;


    @Override
    public void onBackPressed() {
        cTimer.cancel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_accepted);

        Intent intent = getIntent();
        intent.getExtras();
        level = intent.getIntExtra("level",0);
        selected_list = intent.getStringArrayExtra("table_name");
        catagories_id = intent.getIntArrayExtra("table_number");
        questions_id = intent.getIntArrayExtra("Question_number");
        game_id = intent.getStringExtra("game_id");

        que_no = (TextView) findViewById(R.id.quesNo2);
        ques = (TextView) findViewById(R.id.ques2);
        optnA = (Button) findViewById(R.id.optionA2);
        optnB = (Button) findViewById(R.id.optionB2);
        optnC = (Button) findViewById(R.id.optionC2);
        optnD = (Button) findViewById(R.id.optionD2);
        timer = (TextView) findViewById(R.id.timer2);

        cTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                dont_store_next_question_without_view();
            }
        }.start();

        dbasehelper = new MyDatabaseAdapter(this);
        helper = new MyDatabaseAdapter.MyHelper(this);
        try {
            helper.createDataBase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
        try {
            helper.openDataBase();
        } catch (SQLException e) {
        }

        My_question = dbasehelper.getSubjectData(selected_list[catagories_id[p-1]],questions_id[p-1]);
        que_no.setText("Question NO " + p + "");
        ques.setText("Question \n" + My_question.get(1));
        optnA.setText("A) " + My_question.get(2));
        optnB.setText("B) " + My_question.get(3));
        optnC.setText("C) " + My_question.get(4));
        optnD.setText("D) " + My_question.get(5));

        p++;
    }

    public void dont_store_next_question_without_view() {

        //  score[score_count]=0;
        //  score_count++;
        if(p==7) {
            p++;
            cTimer.cancel();
            Intent intent = new Intent(this,scoreboard2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("game_id",game_id);
            startActivity(intent);
        }
        else if(p<7){
            que_no.setText("Question NO " + p + "");
            My_question = dbasehelper.getSubjectData(selected_list[catagories_id[p-1]],questions_id[p-1]);
            ques.setText("Question \n" + My_question.get(1));
            optnA.setText("A) " + My_question.get(2));
            optnB.setText("B) " + My_question.get(3));
            optnC.setText("C) " + My_question.get(4));
            optnD.setText("D) " + My_question.get(5));

            p++;
        }
        cTimer.cancel();
        cTimer.start();
    }

    public void dont_store_next_question(View view) {
        dont_store_next_question_without_view();
    }
}
