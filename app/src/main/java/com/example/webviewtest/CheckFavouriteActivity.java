package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class CheckFavouriteActivity extends AppCompatActivity {
    SQLiteDatabase db;
    int favouriteSum;  //收藏夹总数
    String[] favouriteName;  //收藏夹名称
    int[] favouriteId;  //收藏夹id
    private RecyclerView rv_favouriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_check_favourite);

        rv_favouriteList = findViewById(R.id.favouriteList);
        rv_favouriteList.setLayoutManager(new LinearLayoutManager(CheckFavouriteActivity.this));
        rv_favouriteList.setAdapter(new favouriteListAdapter(CheckFavouriteActivity.this,this.favouriteSum,this.favouriteName,this.favouriteId,this.db));
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_check_favourite);
        rv_favouriteList = findViewById(R.id.favouriteList);
        rv_favouriteList.setLayoutManager(new LinearLayoutManager(CheckFavouriteActivity.this));
        rv_favouriteList.setAdapter(new favouriteListAdapter(CheckFavouriteActivity.this,this.favouriteSum,this.favouriteName,this.favouriteId,this.db));

    }

    public void init(){
        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);//打开数据库
        Cursor favouriteCur=db.rawQuery("select * from favourite",null);//获取收藏夹的表
        favouriteSum = favouriteCur.getCount();
        favouriteName = new String[favouriteSum];
        favouriteId = new int[favouriteSum];
        for(int i=0;i<favouriteSum;i++) {
            favouriteCur.moveToPosition(i);
            favouriteName[i] = favouriteCur.getString(1);
            favouriteId[i] = favouriteCur.getInt(0);
        }
    }

}