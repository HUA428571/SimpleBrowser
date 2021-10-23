package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.webviewtest.application;

public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().hide();



        LinearLayout favourite_item = (LinearLayout) findViewById(R.id.favourite_item);
        favourite_item.setOnClickListener((View.OnClickListener) this);



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.favourite_item)
        {
            TextView favourite_item_title = (TextView) findViewById(R.id.favourite_item_title);
            TextView favourite_item_url = (TextView) findViewById(R.id.favourite_item_url);

            application app = (application)getApplication();
            app.setTitle_from_favourite((String) favourite_item_title.getText());
            app.setUrl_from_favourite((String) favourite_item_url.getText());

            //Intent intent = new Intent(FavouriteActivity.this,MainActivity.class);
            //startActivity(intent);
            finish();
        }
    }

    }
