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

class selectFavouriteWebsiteAdapter extends RecyclerView.Adapter <selectFavouriteWebsiteAdapter.LinearViewHolder>{

    private Context mContext;

    String[] favouriteName; //文件夹名称数组
    int[] favouriteId;  //文件夹id数组
    int sum;  //总数
    int fromWebsiteId; //所选择要移动的网页id，需要修改该id所属数据的favouriteId
    SQLiteDatabase db;

    public selectFavouriteWebsiteAdapter(Context context,int sum,int[] favouriteId,String[] favouriteName,int fromWebsiteId,SQLiteDatabase db){
        this.mContext=context;
        this.sum = sum;
        this.favouriteId = favouriteId;
        this.favouriteName = favouriteName;
        this.fromWebsiteId = fromWebsiteId;
        this.db = db;
    }//构造方法
    @Override
    //返回一个ViewHolder
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    public selectFavouriteWebsiteAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_favourite_item,parent,false));
    }

    @Override
    //绑定ViewHolder
    //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    public void onBindViewHolder(selectFavouriteWebsiteAdapter.LinearViewHolder holder,   int position) {
        holder.selectFavouriteName.setText(favouriteName[position]);
        int thisFavouriteId = favouriteId[position];
        holder.selectFavouriteItem.setOnClickListener(new View.OnClickListener() {    //选定对应的文件夹
            @Override
            public void onClick(View view) {
                db.execSQL("Update favouriteWebsite set favouriteId= ?  where id= ? ", new Object[]{thisFavouriteId, fromWebsiteId } );
                ((Activity)mContext).finish();
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
        private LinearLayout selectFavouriteItem;
        private TextView selectFavouriteName;
        public LinearViewHolder(View itemView) {
            super(itemView);
            selectFavouriteItem = itemView.findViewById(R.id.select_favourite_item);
            selectFavouriteName = itemView.findViewById(R.id.select_favourite_item_name);
        }
    }
}

