package com.fanny.ghmf.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.BloodPressureValueInfo;
import com.fanny.ghmf.bean.BloodSugarValueInfo;
import com.fanny.ghmf.fragment.FragmentRecord;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodSugarAdapter extends BaseAdapter {
    ArrayList<BloodSugarValueInfo> lists;
    Activity activity;
    FragmentRecord fragmentRecord;

    public BloodSugarAdapter(Activity activity, FragmentRecord fragmentRecord) {
        this.activity = activity;
        this.fragmentRecord = fragmentRecord;

    }

    public void setData(ArrayList<BloodSugarValueInfo> data) {
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
            convertView = LinearLayout.inflate(activity, R.layout.xuetang_item, null);
            myListItem.tv_glu = (TextView) convertView.findViewById(R.id.item_glu);
            myListItem.tv_ua = (TextView) convertView.findViewById(R.id.item_ua);
            myListItem.tv_chol = (TextView) convertView.findViewById(R.id.item_chol);
            myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time_xuetang);
            convertView.setTag(myListItem);
        } else {
            myListItem = (MyListItem) convertView.getTag();
        }

        myListItem.tv_glu.setText("血糖：" + lists.get(position).getGlu());
        myListItem.tv_ua.setText("尿酸：" + lists.get(position).getUa());
        myListItem.tv_chol.setText("总胆固醇：" + lists.get(position).getChol());
        myListItem.tv_time.setText("时间：" + lists.get(position).getTime());
        return convertView;
    }

    public final class MyListItem {
        TextView tv_time;
        TextView tv_glu;
        TextView tv_ua;
        TextView tv_chol;
    }

}
