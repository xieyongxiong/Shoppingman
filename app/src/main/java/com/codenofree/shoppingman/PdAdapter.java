package com.codenofree.shoppingman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codenofree.shoppingman.model.pdModel;

import java.util.List;

/**
 * Created by xieyongxiong on 2018/4/25.
 */

public class PdAdapter extends RecyclerView.Adapter<PdAdapter.PdHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<pdModel> data;
    public PdAdapter(Context mContext, List<pdModel> datas) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        data = datas;
    }

    @Override
    public PdHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PdHolder(inflater.inflate(R.layout.pd_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PdHolder holder, int position) {
        Glide.with(mContext).load(data.get(position).getImage()).into(holder.pdImage);
        holder.pdTitle.setText(data.get(position).getTitle());
        holder.pdPrice.setText("￥"+data.get(position).getPrice());
        holder.pdSource.setText("来自："+data.get(position).getSource());
        switch (data.get(position).getSource()){
            case "天猫":
                holder.pdSourceImg.setImageResource(R.drawable.tm);
                break;
            case "京东":
                holder.pdSourceImg.setImageResource(R.drawable.jd);
                break;
            case "国美在线":
                holder.pdSourceImg.setImageResource(R.drawable.guomei);
                break;
            case "蘑菇街":
                holder.pdSourceImg.setImageResource(R.drawable.mogujie);
                break;
            case "苏宁易购":
                holder.pdSourceImg.setImageResource(R.drawable.suning);
                break;
            case "当当网":
                holder.pdSourceImg.setImageResource(R.drawable.dangdang);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PdHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView pdImage;
        TextView pdTitle;
        TextView pdPrice;
        TextView pdSource;
        ImageView pdSourceImg;
        public PdHolder(View itemView) {
            super(itemView);
            pdImage = itemView.findViewById(R.id.pd_img);
            pdTitle = itemView.findViewById(R.id.pd_title);
            pdPrice = itemView.findViewById(R.id.pd_price);
            pdSource = itemView.findViewById(R.id.pd_source);
            pdSourceImg = itemView.findViewById(R.id.source_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra("url", data.get(getAdapterPosition()).getHref());
            mContext.startActivity(intent);
        }
    }
}
