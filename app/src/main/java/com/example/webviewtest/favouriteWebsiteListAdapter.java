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

public class favouriteWebsiteListAdapter extends RecyclerView.Adapter <favouriteWebsiteListAdapter.LinearViewHolder>{

    private Context mContext;
    String[] favouriteWebsiteTitle = {"菠菜宠物店1","菠菜宠物店2", "菠菜宠物店3", "菠菜宠物店4"};
    String[] favouriteWebsiteUrl = {"重庆市北碚区天生路1号","重庆市北碚区天生路2号","重庆市北碚区天生路3号","重庆市北碚区天生路4号"};
    int[] id;
    //int[] favouriteId;
    int sum;
    int fromWebsiteId;
    SQLiteDatabase db;

    public favouriteWebsiteListAdapter(Context context,int sum,String[] favouriteWebsiteTitle,String[] favouriteWebsiteUrl,int[] id,SQLiteDatabase db){
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
            int thisId = this.id[position];

            holder.favouriteWebsiteItemTitle.setText(favouriteWebsiteTitle[position]);
            holder.favouriteWebsiteItemUrl.setText(favouriteWebsiteUrl[position]);

            holder.deleteFavouriteWebsite.setOnClickListener(new View.OnClickListener() {    //删除网站
            @Override
            public void onClick(View view) {
                db.execSQL("DElETE  FROM favouriteWebsite where id= ?", new Object[]{thisId});
                //db.execSQL("DElETE  FROM favouriteWebsite where favouriteId= ?", new Object[]{thisFavouriteId});
                ((Activity) mContext).recreate();
            }
        });

        holder.editFavouriteWebsite.setOnClickListener(new View.OnClickListener() {    //修改网站信息
            @Override
            public void onClick(View view) {
                ((Activity) mContext).setContentView(R.layout.edit_favourite_website);
                Button editFavouriteWebsiteConfirm = (Button) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_confirm);
                EditText editFavouriteWebsiteName = (EditText) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_name);
                EditText editFavouriteWebsiteUrl = (EditText) ((Activity) mContext).findViewById(R.id.editFavouriteWebsite_url);
                editFavouriteWebsiteConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.execSQL("Update favouriteWebsite set title= ?,url=?  where id= ? ", new Object[]{editFavouriteWebsiteName.getText(),editFavouriteWebsiteUrl.getText(), thisId } );
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
            favouriteWebsiteItemBody = itemView.findViewById(R.id.favourite_item_body);
            deleteFavouriteWebsite = itemView.findViewById(R.id.delete_favouriteWebsite);
            editFavouriteWebsite = itemView.findViewById(R.id.edit_favouriteWebsite);
            moveFavouriteWebsite = itemView.findViewById(R.id.move_favouriteWebsite);
        }
    }
}

