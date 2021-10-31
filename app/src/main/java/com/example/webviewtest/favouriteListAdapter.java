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

public class favouriteListAdapter extends RecyclerView.Adapter <favouriteListAdapter.LinearViewHolder>{

    private Context mContext;
    String[] favouriteName = {"菠菜宠物店1","菠菜宠物店2", "菠菜宠物店3", "菠菜宠物店4"};
    //String[] url = {"重庆市北碚区天生路1号","重庆市北碚区天生路2号","重庆市北碚区天生路3号","重庆市北碚区天生路4号"};
    int[] favouriteId;
    int sum;
    SQLiteDatabase db;

    public favouriteListAdapter(Context context,int sum,String[] favouriteName,int[] favouriteId,SQLiteDatabase db){
        this.mContext=context;
        this.sum = sum;
        this.favouriteName = favouriteName;
        this.favouriteId = favouriteId;
        this.db = db;
    }//构造方法
    @Override
    //返回一个ViewHolder
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    public favouriteListAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.favourite_item,parent,false));
    }

    @Override
    //绑定ViewHolder
    //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    public void onBindViewHolder(favouriteListAdapter.LinearViewHolder holder,   int position) {
        int thisFavouriteId = this.favouriteId[position];
        holder.favouriteItemTitle.setText(favouriteName[position]);
        //holder.favouriteItemUrl.setText(url[position]);
        holder.deleteFavourite.setOnClickListener(new View.OnClickListener() {    //删除文件夹
            @Override
            public void onClick(View view) {

                //((Activity) mContext).finish();
                //app.setTitle_from_history((String)holder.historyItemTitle.getText());
                //app.setUrl_from_history((String)holder.historyItemUrl.getText());
                db.execSQL("DElETE  FROM favourite where id= ?", new Object[]{thisFavouriteId});
                db.execSQL("DElETE  FROM favouriteWebsite where favouriteId= ?", new Object[]{thisFavouriteId});
                ((Activity) mContext).recreate();
            }
        });
        holder.editFavourite.setOnClickListener(new View.OnClickListener() {    //修改文件夹名称
            @Override
            public void onClick(View view) {

                ((Activity) mContext).setContentView(R.layout.edit_favourite);
                Button editFavouriteConfirm = (Button) ((Activity) mContext).findViewById(R.id.editFavourite_confirm);
                EditText editFavouriteName = (EditText) ((Activity) mContext).findViewById(R.id.editFavourite_name);
                editFavouriteConfirm.setOnClickListener(new View.OnClickListener() {    //修改文件夹名称
                    @Override
                    public void onClick(View view) {
                        db.execSQL("Update favourite set name= ?  where id= ? ", new Object[]{editFavouriteName.getText(), thisFavouriteId } );
                        ((Activity) mContext).recreate();
                    }
                });
            }
        });
        holder.favouriteItemBody.setOnClickListener(new View.OnClickListener() {    //查看对应的文件夹
            @Override
            public void onClick(View view) {
                Intent favouriteWebsiteListActivity = new Intent(mContext,FavouriteActivity.class);
                favouriteWebsiteListActivity.putExtra("favouriteId",thisFavouriteId);
                ((Activity) mContext).startActivity(favouriteWebsiteListActivity);
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
        private TextView favouriteItemTitle,favouriteItemUrl;
        private LinearLayout favouriteItem;
        private Button deleteFavourite,editFavourite,editFavouriteConfirm;
        private EditText editFavouriteName;
        private LinearLayout favouriteItemBody;


        public LinearViewHolder(View itemView) {
            super(itemView);
            favouriteItem = itemView.findViewById(R.id.favourite_item);
            favouriteItemTitle = itemView.findViewById(R.id.favourite_item_title);
            favouriteItemUrl = itemView.findViewById(R.id.favourite_item_url);
            deleteFavourite = itemView.findViewById(R.id.delete_favourite);
            editFavourite = itemView.findViewById(R.id.edit_favourite);
            editFavouriteName = itemView.findViewById(R.id.editFavourite_name);
            editFavouriteConfirm = itemView.findViewById(R.id.editFavourite_confirm);
            favouriteItemBody = itemView.findViewById(R.id.favourite_item_body);
        }
    }
}

