package com.example.webviewtest;
import android.app.Activity;
import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.example.webviewtest.application;

public class historyListAdapter extends RecyclerView.Adapter <historyListAdapter.LinearViewHolder>{

    private Context mContext;
    String[] title = {"菠菜宠物店1","菠菜宠物店2", "菠菜宠物店3", "菠菜宠物店4"};
    String[] url = {"重庆市北碚区天生路1号","重庆市北碚区天生路2号","重庆市北碚区天生路3号","重庆市北碚区天生路4号"};
    int sum;
    application app;

    public historyListAdapter(Context context,String[] title,String[] url,int sum,application app){
        this.mContext=context;
        this.title = title;
        this.url = url;
        this.sum = sum;
        this.app = app;
    }//构造方法
    @Override
    //返回一个ViewHolder
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    public historyListAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.history_item,parent,false));
    }

    @Override
    //绑定ViewHolder
    //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    public void onBindViewHolder(historyListAdapter.LinearViewHolder holder, final int position) {

        holder.historyItemTitle.setText(title[position]);
        holder.historyItemUrl.setText(url[position]);
        holder.historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
                app.setTitle_from_history((String)holder.historyItemTitle.getText());
                app.setUrl_from_history((String)holder.historyItemUrl.getText());
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
        private TextView historyItemTitle,historyItemUrl;
        private LinearLayout historyItem;

        public LinearViewHolder(View itemView) {
            super(itemView);
            historyItem = itemView.findViewById(R.id.history_item);
            historyItemTitle = itemView.findViewById(R.id.history_item_title);
            historyItemUrl = itemView.findViewById(R.id.history_item_url);
        }
    }
}

