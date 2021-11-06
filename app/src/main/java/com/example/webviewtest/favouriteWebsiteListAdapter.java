package com.example.webviewtest;
import android.app.Activity;
import android.content.Context;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.example.webviewtest.application;

import java.util.List;

public class favouriteWebsiteListAdapter extends RecyclerView.Adapter <favouriteWebsiteListAdapter.LinearViewHolder>{

    private Context mContext;
    List<String> favouriteWebsiteTitle ;
    List<String> favouriteWebsiteUrl;
    List<Integer> id;
    int sum;
    SQLiteDatabase db;
    application app;

    public favouriteWebsiteListAdapter(Context context, int sum, List<String> favouriteWebsiteTitle, List<String> favouriteWebsiteUrl, List<Integer> id, SQLiteDatabase db, application app){
        this.mContext=context;
        this.sum = sum;
        this.favouriteWebsiteTitle = favouriteWebsiteTitle;
        this.favouriteWebsiteUrl = favouriteWebsiteUrl;
        this.id = id;
        this.db = db;
        this.app = app;

    }//构造方法
    @Override
    //返回一个ViewHolder
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    public favouriteWebsiteListAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.favourite_website_item,parent,false));
    }

    @Override
    //绑定ViewHolder
    //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    public void onBindViewHolder(favouriteWebsiteListAdapter.LinearViewHolder holder, final int position) {
        int thisId; //当前收藏网页对应在表里的id

        if(this.id.size()>0&&this.id!=null)
        {
             thisId = this.id.get(position); //若收藏网页表不为空，则赋值
        }
        else{
            thisId = 0; //若不为空则赋值0
        }
        if(this.favouriteWebsiteTitle.size()>0&&this.favouriteWebsiteTitle!=null)
        {
            holder.favouriteWebsiteItemTitle.setText(this.favouriteWebsiteTitle.get(position)); //将未修改前的标题自动传入
        }
        if(this.favouriteWebsiteUrl.size()>0&&this.favouriteWebsiteUrl!=null)
        {
            holder.favouriteWebsiteItemUrl.setText(this.favouriteWebsiteUrl.get(position));  //将未修改前的网址自动传入
        }
            holder.deleteFavouriteWebsite.setOnClickListener(new View.OnClickListener() {    //删除网站
            @Override
            public void onClick(View view) {
                if(thisId!=0)
                {
                    db.execSQL("DElETE  FROM favouriteWebsite where id= ?", new Object[]{thisId});
                    ((Activity) mContext).recreate();
                }

            }
        });

        holder.editFavouriteWebsite.setOnClickListener(new View.OnClickListener() {    //修改网站信息
            @Override
            public void onClick(View view) {
                ((Activity) mContext).setContentView(R.layout.edit_favourite_website);
                Button editFavouriteWebsiteConfirm = (Button) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_confirm);
                Button editFavouriteWebsiteCancel = (Button) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_cancel);
                EditText editFavouriteWebsiteName = (EditText) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_name);
                EditText editFavouriteWebsiteUrl = (EditText) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_url);

                editFavouriteWebsiteName.setText(holder.favouriteWebsiteItemTitle.getText());
                editFavouriteWebsiteUrl.setText(holder.favouriteWebsiteItemUrl.getText());

                editFavouriteWebsiteConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.execSQL("Update favouriteWebsite set title= ?,url=?  where id= ? ", new Object[]{editFavouriteWebsiteName.getText(),editFavouriteWebsiteUrl.getText(), thisId } );
                        ((Activity) mContext).recreate();
                    }
                });
                editFavouriteWebsiteCancel.setOnClickListener(new View.OnClickListener() { //取消修改
                    @Override
                    public void onClick(View view) {
                        ((Activity) mContext).recreate();
                    }
                });
            }
        });
        holder.moveFavouriteWebsite.setOnClickListener(new View.OnClickListener() {    //移动
            @Override
            public void onClick(View view) {
                Intent selectFavouriteActivity = new Intent(mContext,SelectFavouriteActivity.class);
                selectFavouriteActivity.putExtra("favouriteWebsiteId",thisId);
                ((Activity) mContext).startActivity(selectFavouriteActivity);
            }
        });

        holder.favouriteWebsiteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
                app.setTitle_from_favourite((String)holder.favouriteWebsiteItemTitle.getText());
                app.setUrl_from_favourite((String)holder.favouriteWebsiteItemUrl.getText());
                Intent newIntent = new Intent(mContext, MainActivity.class);
                ((Activity) mContext).startActivity(newIntent);
            }
        });

    }

    @Override
    //获取列表长度
    public int getItemCount() {

        return this.sum;
    }



    class LinearViewHolder extends RecyclerView.ViewHolder {
        //找到组件
        private TextView favouriteWebsiteItemTitle,favouriteWebsiteItemUrl;
        private Button deleteFavouriteWebsite,editFavouriteWebsite,moveFavouriteWebsite;
        private LinearLayout favouriteWebsiteItem;
        public LinearViewHolder(View itemView) {
            super(itemView);
            favouriteWebsiteItemTitle = itemView.findViewById(R.id.favouriteWebsite_item_title);
            favouriteWebsiteItemUrl = itemView.findViewById(R.id.favouriteWebsite_item_url);
            deleteFavouriteWebsite = itemView.findViewById(R.id.delete_favouriteWebsite);
            editFavouriteWebsite = itemView.findViewById(R.id.edit_favouriteWebsite);
            moveFavouriteWebsite = itemView.findViewById(R.id.move_favouriteWebsite);
            favouriteWebsiteItem = itemView.findViewById(R.id.favouriteWebsite_item_body);
        }
    }
}

