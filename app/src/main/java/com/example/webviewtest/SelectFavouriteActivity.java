package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class SelectFavouriteActivity extends AppCompatActivity {
    SQLiteDatabase db;
    private RecyclerView rv_selectFavouriteList;
    int favouriteSum;
    String[] favouriteName;
    int[] favouriteId;
    int fromWebsiteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromWebsiteId = getIntent().getIntExtra("favouriteWebsiteId",0);
        init();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_favourite);

        rv_selectFavouriteList = findViewById(R.id.select_favouriteList);
        rv_selectFavouriteList.setLayoutManager(new LinearLayoutManager(SelectFavouriteActivity.this));
        rv_selectFavouriteList.setAdapter(new selectFavouriteWebsiteAdapter(SelectFavouriteActivity.this,this.favouriteSum,this.favouriteId,this.favouriteName,this.fromWebsiteId,this.db));
    }

    public void init(){
        db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);//打开数据库
        Cursor favouriteCur=db.rawQuery("select * from favourite",null);//获取收藏夹的表
        favouriteSum = favouriteCur.getCount();
        favouriteName = new String[favouriteSum];
        favouriteId = new int[favouriteSum];
        for(int i=0;i<favouriteSum;i++) {
            favouriteCur.moveToPosition(i);
            //System.out.println("id:"+favouriteWebsitecCur.getInt(2));
            favouriteName[i] = favouriteCur.getString(1);
            favouriteId[i] = favouriteCur.getInt(0);
        }
    }
}