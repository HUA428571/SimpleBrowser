package com.example.webviewtest;

import android.app.Application;

public class application extends Application {
    private String title_from_favourite = "";
    private String url_from_favourite = "";
    private String title_from_history = "";
    private String url_from_history = "";
    String getTitle_from_favourite(){
        return this.title_from_favourite;
    }
    String getUrl_from_favourite(){
        return this.url_from_favourite;
    }
    void setTitle_from_favourite(String title){
        this.title_from_favourite = title;
    }
    void setUrl_from_favourite(String url){
        this.url_from_favourite = url;
    }

    String getTitle_from_history(){
        return this.title_from_history;
    }
    String getUrl_from_history(){
        return this.url_from_history;
    }
    void setTitle_from_history(String title){
        this.title_from_history = title;
    }
    void setUrl_from_history(String url){
        this.url_from_history = url;
    }
}
