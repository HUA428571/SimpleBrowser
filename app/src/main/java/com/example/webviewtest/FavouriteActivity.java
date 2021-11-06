package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.webviewtest.application;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener  {

    SQLiteDatabase db;
    int sum;
    int favouriteId;

    List<Integer> id; //收藏的网页id List
    List<String> title; //收藏的网页标题List
    List<String> url;  //收藏的网页网址List
    RecyclerView rv_favouriteWebsiteList;
    application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteId=getIntent().getIntExtra("favouriteId",0); //获取来自的收藏夹id（默认为0）
        app = (application)getApplication();
    }

    @Override
    //在resume函数中使用是因为收藏夹首页会多次返回到，生命周期中activity创建之后总要resume。在不影响初始化的情况下我们只采用resume函数初始化。
    protected void onResume() {
        super.onResume();
        System.out.println("resume");
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().hide();
        init();
        rv_favouriteWebsiteList = findViewById(R.id.favouriteWebsiteList);
        rv_favouriteWebsiteList.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this));
        rv_favouriteWebsiteList.setAdapter(new favouriteWebsiteListAdapter(FavouriteActivity.this,this.sum,this.title,this.url,this.id,this.db,this.app));
        Button addFavourite = (Button) findViewById(R.id.addFavourite);
        Button checkFavourite = (Button) findViewById(R.id.checkFavourite);
        addFavourite.setOnClickListener(this);
        checkFavourite.setOnClickListener(this);
    }

    public void init(){

        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);//打开数据库
        Cursor favouriteWebsiteCur=db.rawQuery("select * from favouriteWebsite",null);//获取收藏网页的表
        sum=0;
        int FavouriteWebsiteSum=favouriteWebsiteCur.getCount();
        this.title = new ArrayList<>();
        this.url = new ArrayList<>();
        this.id = new ArrayList<>();
        for(int i=0;i<FavouriteWebsiteSum;i++) {
            favouriteWebsiteCur.moveToPosition(i);
            if(favouriteWebsiteCur.getInt(3)==favouriteId)
            {
                this.id.add(favouriteWebsiteCur.getInt(0));
                this.title.add(favouriteWebsiteCur.getString(1));
                this.url.add(favouriteWebsiteCur.getString(2));
                sum++;
            }

        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.addFavourite:
                Intent addFavouriteIntent = new Intent(FavouriteActivity.this,AddFavouriteActivity.class);
                startActivity(addFavouriteIntent);
                break;
            case R.id.checkFavourite:
                Intent checkFavouriteIntent = new Intent(FavouriteActivity.this,CheckFavouriteActivity.class);
                startActivity(checkFavouriteIntent);
                break;
        }
    }
    }
