package com.fanny.ghmf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.TempValueInfo;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class TempAdapter extends BaseAdapter {
    ArrayList<TempValueInfo> lists;
    Context mContext;

    public TempAdapter(Context context, ArrayList<TempValueInfo> list) {
        lists = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyListItem myListItem = null;
        if (convertView == null) {
            myListItem = new MyListItem();
            convertView = LinearLayout.inflate(mContext, R.layout.tiwen_item, null);
            myListItem.tv_temp = (TextView) convertView.findViewById(R.id.item_temp);
            myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time_temp);
            convertView.setTag(myListItem);
        } else {
            myListItem = (MyListItem) convertView.getTag();
        }

        myListItem.tv_temp.setText("体温为：" + lists.get(position).getValue());
        myListItem.tv_time.setText("时间：" + lists.get(position).getTime());
        return convertView;
    }

    public class MyListItem {
        TextView tv_temp;
        TextView tv_time;
    }

}
