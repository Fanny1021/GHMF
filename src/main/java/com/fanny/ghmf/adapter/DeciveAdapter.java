package com.fanny.ghmf.adapter;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.DeviceBean;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Fanny on 17/10/10.
 */

public class DeciveAdapter extends RecyclerView.Adapter<DeciveAdapter.ViewHolder> implements View.OnClickListener{

    private Context context;
    private ArrayList<DeviceBean> nameLis=new ArrayList<>();


    public DeciveAdapter(Context context, ArrayList<DeviceBean> list) {
        this.context = context;
        this.nameLis = list;

//        if (map != null) {
//            // 将map中的所有键去取出来，用迭代器进行读取
//            Set set = map.keySet();
//            if (set != null) {
//                Iterator iterator = set.iterator();
//                while (iterator.hasNext()) {
//                    // 取出单个的map键
//                    String key = (String) iterator.next();
//                    nameLis.add(key);
//                }
//            }
//
//        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        itemView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvName.setText(nameLis.get(position).getDevice_name());
        holder.imPic.setImageBitmap(nameLis.get(position).getDevice_pic());

        holder.itemView.setTag(position);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return nameLis.size();
    }

    @Override
    public void onClick(View v) {

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            imPic= (ImageView) itemView.findViewById(R.id.device_item_pic);
            tvName= (TextView) itemView.findViewById(R.id.device_item_name);
        }

        public ImageView imPic;
        public TextView tvName;

    }
}
