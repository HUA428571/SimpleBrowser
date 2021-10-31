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

public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener  {

    SQLiteDatabase db;
    int sum;
    int favouriteId;

    int[] id;
    String[] title;
    String[] url;
    int[] favouriteWebsiteId;

    //String[] favouriteName;
    //int[] favouriteId;

    private RecyclerView rv_favouriteWebsiteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteId=getIntent().getIntExtra("favouriteId",0);
        System.out.println("favouriteId:"+favouriteId);
        init();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_favourite);

        Button addFavourite = (Button) findViewById(R.id.addFavourite);
        //Button addFavourite_confirm = (Button) findViewById(R.id.addFavourite_confirm);
        //EditText addFavourite_name = (EditText) findViewById(R.id.addFavourite_name);
        addFavourite.setOnClickListener(this);
        //addFavourite_confirm.setOnClickListener(this);

        rv_favouriteWebsiteList = findViewById(R.id.favouriteWebsiteList);
        rv_favouriteWebsiteList.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this));
        //rv_favouriteList.setAdapter(new favouriteListAdapter(FavouriteActivity.this,this.favouriteSum,this.favouriteName,this.favouriteId));
        rv_favouriteWebsiteList.setAdapter(new favouriteWebsiteListAdapter(FavouriteActivity.this,this.sum,this.title,this.url,this.id,this.db));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //webView.loadUrl(app.getUrl_from_favourite());
        init();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_favourite);

        rv_favouriteWebsiteList = findViewById(R.id.favouriteWebsiteList);
        rv_favouriteWebsiteList.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this));
        //rv_favouriteList.setAdapter(new favouriteListAdapter(FavouriteActivity.this,this.favouriteSum,this.favouriteName,this.favouriteId));

        //rv_favouriteList.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this));
        rv_favouriteWebsiteList.setAdapter(new favouriteWebsiteListAdapter(FavouriteActivity.this,this.sum,this.title,this.url,this.id,this.db));
        Button addFavourite = (Button) findViewById(R.id.addFavourite);
        Button checkFavourite = (Button) findViewById(R.id.checkFavourite);
        //Button addFavourite_confirm = (Button) findViewById(R.id.addFavourite_confirm);
        //EditText addFavourite_name = (EditText) findViewById(R.id.addFavourite_name);
        addFavourite.setOnClickListener(this);
        checkFavourite.setOnClickListener(this);
        //addFavourite_confirm.setOnClickListener(this);
    }

    public void init(){
        //System.out.println("init");
        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);//打开数据库
        Cursor favouriteWebsiteCur=db.rawQuery("select * from favouriteWebsite",null);//获取收藏网页的表
        //Cursor favouriteCur=db.rawQuery("select * from favourite",null);//获取收藏夹的表
        sum=0;
        int FavouriteWebsiteSum=favouriteWebsiteCur.getCount();
        //favouriteSum = favouriteCur.getCount();

        title = new String[FavouriteWebsiteSum];
        url = new String[FavouriteWebsiteSum];
        favouriteWebsiteId = new int[FavouriteWebsiteSum];
        id = new int[FavouriteWebsiteSum];

        //favouriteName = new String[favouriteSum];
        //favouriteId = new int[favouriteSum];

        for(int i=0;i<FavouriteWebsiteSum;i++) {
            favouriteWebsiteCur.moveToPosition(i);
            System.out.println("id:"+ favouriteWebsiteCur.getInt(0));
            System.out.println("id:"+ favouriteWebsiteCur.getInt(3));
            if(favouriteWebsiteCur.getInt(3)==favouriteId)
            {
                //System.out.println("correct");
                id[i] = favouriteWebsiteCur.getInt(0);
                title[i] = favouriteWebsiteCur.getString(1);
                url[i] = favouriteWebsiteCur.getString(2);
                favouriteWebsiteId[i] = favouriteWebsiteCur.getInt(3);
                sum++;
            }

        }
//        for(int i=0;i<favouriteSum;i++) {
//            favouriteCur.moveToPosition(i);
//            //System.out.println("id:"+favouriteWebsitecCur.getInt(2));
//            //favouriteName[i] = favouriteCur.getString(1);
//            //favouriteId[i] = favouriteCur.getInt(0);
//        }
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
