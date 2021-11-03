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
    //int[] favouriteId;
    int sum;
    int fromWebsiteId;
    SQLiteDatabase db;

    public favouriteWebsiteListAdapter(Context context, int sum, List<String> favouriteWebsiteTitle, List<String> favouriteWebsiteUrl, List<Integer> id, SQLiteDatabase db){
        this.mContext=context;
        this.sum = sum;
        this.favouriteWebsiteTitle = favouriteWebsiteTitle;
        this.favouriteWebsiteUrl = favouriteWebsiteUrl;
        this.id = id;
        this.db = db;

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
        int thisId;

        if(this.id.size()>0&&this.id!=null)
        {
             thisId = this.id.get(position);
        }
        else{
            thisId = 0;
        }



        //System.out.println("idtoput:"+id[position]);

        //System.out.println("titletoput:"+favouriteWebsiteTitle[position]);
        //System.out.println("urltoput:"+favouriteWebsiteUrl[position]);
        //System.out.println("favouriteid:"+favouriteWebsiteCur.getInt(3));
        if(this.favouriteWebsiteTitle.size()>0&&this.favouriteWebsiteTitle!=null)
        {
            holder.favouriteWebsiteItemTitle.setText(this.favouriteWebsiteTitle.get(position));
        }
        if(this.favouriteWebsiteUrl.size()>0&&this.favouriteWebsiteUrl!=null)
        {
            holder.favouriteWebsiteItemUrl.setText(this.favouriteWebsiteUrl.get(position));
        }




            holder.deleteFavouriteWebsite.setOnClickListener(new View.OnClickListener() {    //删除网站
            @Override
            public void onClick(View view) {
                if(thisId!=0)
                {
                    db.execSQL("DElETE  FROM favouriteWebsite where id= ?", new Object[]{thisId});
                    //System.out.println(thisId);
                    //db.execSQL("DElETE  FROM favouriteWebsite where favouriteId= ?", new Object[]{thisFavouriteId});
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

                editFavouriteWebsiteCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //db.execSQL("Update favouriteWebsite set title= ?,url=?  where id= ? ", new Object[]{editFavouriteWebsiteName.getText(),editFavouriteWebsiteUrl.getText(), thisId } );
                        //((Activity) mContext).setContentView(R.layout.activity_favourite);
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

    }

    @Override
    //获取列表长度
    public int getItemCount() {

        return this.sum;
    }



    class LinearViewHolder extends RecyclerView.ViewHolder {
        //找到组件
        private TextView favouriteWebsiteItemTitle,favouriteWebsiteItemUrl;
        private LinearLayout favouriteWebsiteItem;
        private LinearLayout favouriteWebsiteItemBody;
        private Button deleteFavouriteWebsite,editFavouriteWebsite,moveFavouriteWebsite;

        public LinearViewHolder(View itemView) {
            super(itemView);
            favouriteWebsiteItem = itemView.findViewById(R.id.favouriteWebsite_item);
            favouriteWebsiteItemTitle = itemView.findViewById(R.id.favouriteWebsite_item_title);
            favouriteWebsiteItemUrl = itemView.findViewById(R.id.favouriteWebsite_item_url);
            favouriteWebsiteItemBody = itemView.findViewById(R.id.favouriteWebsite_item_body);
            deleteFavouriteWebsite = itemView.findViewById(R.id.delete_favouriteWebsite);
            editFavouriteWebsite = itemView.findViewById(R.id.edit_favouriteWebsite);
            moveFavouriteWebsite = itemView.findViewById(R.id.move_favouriteWebsite);
        }
    }
}

