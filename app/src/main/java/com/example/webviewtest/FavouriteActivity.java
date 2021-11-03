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

    List<Integer> id;
    List<String> title;
    List<String> url;
    int[] favouriteWebsiteId;

    //String[] favouriteName;
    //int[] favouriteId;

     RecyclerView rv_favouriteWebsiteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteId=getIntent().getIntExtra("favouriteId",0);
        //System.out.println("favouriteId:"+favouriteId);
        //init();
        //getSupportActionBar().hide();
        //setContentView(R.layout.activity_favourite);

        //Button addFavourite = (Button) findViewById(R.id.addFavourite);
        //addFavourite.setOnClickListener(this);

        //rv_favouriteWebsiteList = findViewById(R.id.favouriteWebsiteList);
        //rv_favouriteWebsiteList.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this));
        //rv_favouriteWebsiteList.setAdapter(new favouriteWebsiteListAdapter(FavouriteActivity.this,this.sum,this.title,this.url,this.id,this.db));
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resume");
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().hide();
        init();


        //System.out.println("idtoput:"+this.id[0]);

        //System.out.println("titletoput:"+this.title[0]);
        //System.out.println("urltoput:"+this.url[0]);
        //System.out.println("favouriteid:"+favouriteWebsiteCur.getInt(3));

        rv_favouriteWebsiteList = findViewById(R.id.favouriteWebsiteList);
        rv_favouriteWebsiteList.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this));
        rv_favouriteWebsiteList.setAdapter(new favouriteWebsiteListAdapter(FavouriteActivity.this,this.sum,this.title,this.url,this.id,this.db));
        Button addFavourite = (Button) findViewById(R.id.addFavourite);
        Button checkFavourite = (Button) findViewById(R.id.checkFavourite);
        addFavourite.setOnClickListener(this);
        checkFavourite.setOnClickListener(this);
    }

    public void init(){
        //System.out.println("init");
        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);//打开数据库
        Cursor favouriteWebsiteCur=db.rawQuery("select * from favouriteWebsite",null);//获取收藏网页的表
        //Cursor favouriteCur=db.rawQuery("select * from favourite",null);//获取收藏夹的表
        sum=0;
        int FavouriteWebsiteSum=favouriteWebsiteCur.getCount();
        //favouriteSum = favouriteCur.getCount();


        this.title = new ArrayList<>();
        this.url = new ArrayList<>();
        //favouriteWebsiteId = new int[FavouriteWebsiteSum];
        this.id = new ArrayList<>();

        //favouriteName = new String[favouriteSum];
        //favouriteId = new int[favouriteSum];

        for(int i=0;i<FavouriteWebsiteSum;i++) {
            favouriteWebsiteCur.moveToPosition(i);
            //System.out.println("id:"+ favouriteWebsiteCur.getInt(0));
            //System.out.println("id:"+ favouriteWebsiteCur.getInt(3));
            if(favouriteWebsiteCur.getInt(3)==favouriteId)
            {

                //System.out.println("idtoput:"+favouriteWebsiteCur.getInt(0));

                //System.out.println("titletoput:"+favouriteWebsiteCur.getString(1));
                //System.out.println("urltoput:"+favouriteWebsiteCur.getString(2));
                //System.out.println("favouriteid:"+favouriteWebsiteCur.getInt(3));


                this.id.add(favouriteWebsiteCur.getInt(0));
                this.title.add(favouriteWebsiteCur.getString(1));
                this.url.add(favouriteWebsiteCur.getString(2));
                //favouriteWebsiteId[i] = favouriteWebsiteCur.getInt(3);

                //System.out.println("idtoput:"+this.id[i]);
                //System.out.println("titletoput:"+this.title[i]);
                //System.out.println("urltoput:"+this.url[i]);
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
                //setContentView(R.layout.addfavourite_bar);
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
