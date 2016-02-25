package com.example.amit.reflexreactor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Questions extends AppCompatActivity {


    int indx;
    CountDownTimer cTimer;
    ArrayList<String> My_question;
    int[] Category_id;
    int[] question_indexes;
    int question_index_count;
    int cat_id_index=0;
    int p=1;
    Button optnA,optnB,optnC,optnD;
    TextView que_no,ques,timer;
    Random rand ;
    int weget_count=0;
    ArrayList<String> User_id;
    String[] user_names;
    int My_level;
    //int[] score ;
    //int score_count=0;

    TextView tview;
    MyDatabaseAdapter dbasehelper;
    MyDatabaseAdapter.MyHelper helper;
    String selected_list[];
    char[] correct;
    char[] weget;


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
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // score = new int[10];
        rand =new Random();
        weget = new char[4];
        Category_id = new int[20];
        question_indexes = new int[6];

        Intent intent = getIntent();
        intent.getExtras();
        indx = intent.getIntExtra("idx", 0);
        selected_list = intent.getStringArrayExtra("selected_categories");
        My_level = intent.getIntExtra("mylevel",0);
        User_id = intent.getStringArrayListExtra("user_id");
        user_names=intent.getStringArrayExtra("user_names");

        que_no = (TextView) findViewById(R.id.quesNo);
        ques = (TextView) findViewById(R.id.ques);
        optnA = (Button) findViewById(R.id.optionA);
        optnB = (Button) findViewById(R.id.optionB);
        optnC = (Button) findViewById(R.id.optionC);
        optnD = (Button) findViewById(R.id.optionD);
        timer = (TextView) findViewById(R.id.timer);

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

        int val =rand.nextInt(indx);
        Category_id[cat_id_index]=val;
        cat_id_index++;
        int my_q_index = rand.nextInt(4)+1;
        question_indexes[question_index_count]=my_q_index;
        question_index_count++;
        My_question = dbasehelper.getSubjectData(selected_list[val],my_q_index);
        que_no.setText("Question NO " + p + "");
        ques.setText("Question \n" + My_question.get(1));

        int[] mpoptns = new int[4];
        for(int i=0;i<4;i++)
        {            mpoptns[i]=-1;
        }
        Random random = new Random();
        for(int i=1;i<=4;i++)
        {
            int idx = random.nextInt(4);
            if(mpoptns[idx]==-1)
            {
                mpoptns[idx]=i;
            }
            else
                i--;
        }

//        correct = get_correct(mpoptns);

        optnA.setText("A) " + My_question.get(mpoptns[0]+1));
        optnB.setText("B) " + My_question.get(mpoptns[1]+1));
        optnC.setText("C) " + My_question.get(mpoptns[2]+1));
        optnD.setText("D) " + My_question.get(mpoptns[3]+1));

        p++;

    }

    private char[] get_correct(int[] mpoptns) {
        char[] ans = new char[4];
        ans[mpoptns[0]]='a';
        ans[mpoptns[1]]='b';
        ans[mpoptns[2]]='c';
        ans[mpoptns[3]]='d';
        return ans;
    }


    public void dont_store_next_question(View view) {

        dont_store_next_question_without_view();
    }

    public void reset_priority(View view) {
    }

    public void store_next_question(View view) {

        //process the correct
        //score[score_count]=get_LCS_count(correct,weget,4,4);

        if(p==7)
        {
            p++;
            cTimer.cancel();
            Intent intent = new Intent(this,Score_board.class);
            intent.putExtra("idx",indx);
            intent.putExtra("user_id",User_id);
            intent.putExtra("user_names",user_names);
            intent.putExtra("level",My_level);
            intent.putExtra("catagories",selected_list);
            intent.putExtra("catagories_id",Category_id);
            intent.putExtra("question_indexes",question_indexes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(p<7){
            que_no.setText("Question NO " + p + "");
            int val =rand.nextInt(indx);
            Category_id[cat_id_index]=val;
            cat_id_index++;
            int my_q_index = rand.nextInt(4)+1;
            question_indexes[question_index_count]=my_q_index;
            question_index_count++;
            My_question = dbasehelper.getSubjectData(selected_list[val],my_q_index);
            ques.setText("Question \n" + My_question.get(1));
            int[] mpoptns = new int[4];
            for(int i=0;i<4;i++)
            {
                mpoptns[i]=-1;
            }
            Random random = new Random();
            for(int i=1;i<=4;i++)
            {
                int idx = random.nextInt(4);
                if(mpoptns[idx]==-1)
                {
                    mpoptns[idx]=i;
                }
                else
                    i--;
            }

         //   correct = get_correct(mpoptns);

            optnA.setText("A) " + My_question.get(mpoptns[0]+1));
            optnB.setText("B) " + My_question.get(mpoptns[1]+1));
            optnC.setText("C) " + My_question.get(mpoptns[2]+1));
            optnD.setText("D) " + My_question.get(mpoptns[3]+1));

            p++;
        }
        cTimer.cancel();
        cTimer.start();
    }

    private int get_LCS_count(char[] correct, char[] weget,int m,int n) {
        if(m==0 || n==0)
        return 0;
        if(correct[m-1] == weget[n-1])
            return 1+ get_LCS_count(correct,weget,m-1,n-1);
        else
            return max(get_LCS_count(correct,weget,m,n-1),get_LCS_count(correct,weget,m-1,n));
    }

    private int max(int a, int b) {
        if(a>b)
            return a;
        else
            return b;
    }

    private void dont_store_next_question_without_view() {

      //  score[score_count]=0;
      //  score_count++;
        if(p==7) {
            p++;
            cTimer.cancel();
            Intent intent = new Intent(this,Score_board.class);
            intent.putExtra("idx",indx);
            intent.putExtra("user_id",User_id);
            intent.putExtra("user_names",user_names);
            intent.putExtra("level",My_level);
            intent.putExtra("catagories",selected_list);
            intent.putExtra("catagories_id",Category_id);
            intent.putExtra("question_indexes",question_indexes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(p<7){
            que_no.setText("Question NO " + p + "");
            int val =rand.nextInt(indx);
            Category_id[cat_id_index]=val;
            cat_id_index++;
            int my_q_index = rand.nextInt(4)+1;
            question_indexes[question_index_count]=my_q_index;
            question_index_count++;
            My_question = dbasehelper.getSubjectData(selected_list[val],my_q_index);
            ques.setText("Question \n" + My_question.get(1));
            int[] mpoptns = new int[4];
            for(int i=0;i<4;i++)
            {
                mpoptns[i]=-1;
            }
            Random random = new Random();
            for(int i=1;i<=4;i++)
            {
                int idx = random.nextInt(4);
                if(mpoptns[idx]==-1)
                {
                    mpoptns[idx]=i;
                }
                else
                    i--;
            }

            optnA.setText("A) " + My_question.get(mpoptns[0]+1));
            optnB.setText("B) " + My_question.get(mpoptns[1]+1));
            optnC.setText("C) " + My_question.get(mpoptns[2]+1));
            optnD.setText("D) " + My_question.get(mpoptns[3]+1));

            p++;
        }
        cTimer.cancel();
        cTimer.start();
    }


    public void selected_optionA(View view) {
        weget[weget_count]='a';
        weget_count++;
    }
    public void selected_optionB(View view) {
        weget[weget_count]='b';
        weget_count++;
    }
    public void selected_optionC(View view) {
        weget[weget_count]='c';
        weget_count++;
    }
    public void selected_optionD(View view) {
        weget[weget_count]='d';
        weget_count=0;
    }
}

