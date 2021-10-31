package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddFavouriteActivity extends AppCompatActivity implements View.OnClickListener{

    SQLiteDatabase db;
    int sum;
    application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favourite);

        Button addFavourite_confirm = (Button) findViewById(R.id.addFavourite_confirm);
        addFavourite_confirm.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.addFavourite_confirm:
                app = (application)getApplication();
                EditText addFavourite_name = (EditText) findViewById(R.id.addFavourite_name);
                System.out.println("name:"+String.valueOf(addFavourite_name.getText()));
                db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);//打开数据库
                Cursor FavouriteCur=db.rawQuery("select * from favourite",null);//获取收藏网页的表
                sum=FavouriteCur.getCount();
                app.setTotal_favourite_num(app.getTotal_favourite_num()+1);
                ContentValues Favourite_cv = new ContentValues(2);
                Favourite_cv.put("id",app.getTotal_favourite_num());
                Favourite_cv.put("name", String.valueOf(addFavourite_name.getText()));
                db.insert("favourite",null,Favourite_cv);
                //setContentView(R.layout.activity_favourite);
                finish();
        }
    }
}