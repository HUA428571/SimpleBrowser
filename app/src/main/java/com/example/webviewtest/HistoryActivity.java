package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.webviewtest.application;

public class HistoryActivity extends AppCompatActivity  {
    SQLiteDatabase db;
    String [] title;
    String [] url;
    int sum;
    application app;
    //LayoutInflater inflater = getLayoutInflater();
    private RecyclerView rv_historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (application)getApplication();
        init();
        setContentView(R.layout.activity_history);
        rv_historyList = findViewById(R.id.historyList);
        rv_historyList.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_historyList.setAdapter(new historyListAdapter(HistoryActivity.this,this.title,this.url,this.sum,this.app));

    }

    public void init(){
        //创建数据库及数据表
        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);

            Cursor cur=db.rawQuery("select * from test",null);
            sum=cur.getCount();
            title = new String[sum];
            url = new String[sum];
            //String sUser=String.format("共有记录数量：%d:\n",sum);
            for(int i=0;i<sum;i++) {
                cur.moveToPosition(i);
                title[i] = cur.getString(0);
                url[i] = cur.getString(1);
                //sUser += String.format("%s,%s\n", cur.getString(0), cur.getString(1));
            }
    }

//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public void onClick(View view)
//    {
//        if(view.getId() == R.id.history_item)
//        {
//            finish();
//        }
//    }
}