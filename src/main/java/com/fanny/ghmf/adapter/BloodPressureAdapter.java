package com.fanny.ghmf.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.BloodPressureInfo;
import com.fanny.ghmf.bean.BloodPressureValueInfo;
import com.fanny.ghmf.bean.TempValueInfo;
import com.fanny.ghmf.fragment.FragmentRecord;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodPressureAdapter extends BaseAdapter {
    ArrayList<BloodPressureValueInfo> lists;
    Activity activity;
    FragmentRecord fragmentRecord;

    public BloodPressureAdapter(Activity activity, FragmentRecord fragmentRecord) {
        this.activity = activity;
        this.fragmentRecord = fragmentRecord;

    }

    public void setData(ArrayList<BloodPressureValueInfo> data) {
        this.lists = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0) {

            return lists.size();
        }
        return 0;
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
            convertView = LinearLayout.inflate(activity, R.layout.xueya_item, null);
            myListItem.tv_sys = (TextView) convertView.findViewById(R.id.item_sys);
            myListItem.tv_dia = (TextView) convertView.findViewById(R.id.item_dia);
            myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time);
            myListItem.tv_plus = (TextView) convertView.findViewById(R.id.item_plus);
            myListItem.tv_map = (TextView) convertView.findViewById(R.id.item_map);
            convertView.setTag(myListItem);
        } else {
            myListItem = (MyListItem) convertView.getTag();
        }

        myListItem.tv_sys.setText("收缩压为：" + lists.get(position).getSys());
        myListItem.tv_dia.setText("舒张压为：" + lists.get(position).getDia());
        myListItem.tv_plus.setText("脉率为：" + lists.get(position).getPlus());
        myListItem.tv_map.setText("平均压为：" + lists.get(position).getMap());
        myListItem.tv_time.setText("时间：" + lists.get(position).getTime());
        return convertView;
    }

    public final class MyListItem {
        public TextView tv_sys;
        TextView tv_dia;
        TextView tv_time;
        TextView tv_plus;
        TextView tv_map;
    }

}
