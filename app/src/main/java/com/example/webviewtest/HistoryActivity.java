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
    SQLiteDatabase db; //数据库
    String [] title; //标题数组
    String [] url;  //网址数组，和标题数组一一对应
    int sum;
    application app;
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
        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);
            Cursor cur=db.rawQuery("select * from test",null);
            sum=cur.getCount();
            title = new String[sum];
            url = new String[sum];
            for(int i=0;i<sum;i++) {
                cur.moveToPosition(i);
                title[sum-i-1] = cur.getString(0);
                url[sum-i-1] = cur.getString(1);
            }
    }
}