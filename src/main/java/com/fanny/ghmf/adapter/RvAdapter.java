package com.fanny.ghmf.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fanny.ghmf.R;

import java.util.List;

/**
 * Created by Fanny on 17/6/16.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder>{

    private final int NORMAL_TYPE=0;
    private final int FOOT_TYPE=1111;
    private List<String> mData;
    private int mMaxCount=15;
    private int mDataCount=0;
    private IListener mIListener;

    public RvAdapter(List<String> data , IListener iListener){
        mData=data;
        mIListener=iListener;
    }
    public interface IListener{
        void normalItemClick(String s);
        void clearItemClick();
    }
    @Override
    public RvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal_views= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        View foot_view=LayoutInflater.from(parent.getContext()).inflate(R.layout.search_clear,parent,false);
        if(viewType==FOOT_TYPE){
            return new ViewHolder(foot_view,FOOT_TYPE);
        }
        return new ViewHolder(normal_views,NORMAL_TYPE);
    }

    @Override
    public void onBindViewHolder(RvAdapter.ViewHolder holder, int position) {
        if(getItemViewType(position)!=FOOT_TYPE){
            holder.tvViewHolder.setText(mData.get(position));
        }else {
            Log.d("foot","填充底部容器");
        }
    }

    @Override
    public int getItemCount() {
        if(mData.size()<mMaxCount){
            mDataCount=mData.size();
            return mData.size();
        }
        mDataCount=mMaxCount;
        return mMaxCount;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==mDataCount-1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvViewHolder;
        public RelativeLayout tvFootView;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==NORMAL_TYPE){
                tvViewHolder= (TextView) itemView.findViewById(R.id.tv_history_item);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIListener.normalItemClick(mData.get(ViewHolder.this.getAdapterPosition()));
                    }
                });
            }else if(viewType==FOOT_TYPE){
                tvFootView= (RelativeLayout) itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIListener.clearItemClick();
                    }
                });
            }
        }
    }
}
