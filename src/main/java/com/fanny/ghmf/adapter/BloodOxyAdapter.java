package com.fanny.ghmf.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.BloodOxyValueInfo;
import com.fanny.ghmf.bean.BloodSugarValueInfo;
import com.fanny.ghmf.fragment.FragmentRecord;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodOxyAdapter extends BaseAdapter {
    ArrayList<BloodOxyValueInfo> lists;
    Activity activity;
    FragmentRecord fragmentRecord;

    public BloodOxyAdapter(Activity activity, FragmentRecord fragmentRecord) {
        this.activity = activity;
        this.fragmentRecord = fragmentRecord;

    }

    public void setData(ArrayList<BloodOxyValueInfo> data) {
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
            convertView = LinearLayout.inflate(activity, R.layout.xueyang_item, null);
            myListItem.tv_sp02 = (TextView) convertView.findViewById(R.id.item_spo2);
            myListItem.tv_pr = (TextView) convertView.findViewById(R.id.item_pr);
            myListItem.tv_pi = (TextView) convertView.findViewById(R.id.item_pi);
            myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time_xueyang);
            convertView.setTag(myListItem);
        } else {
            myListItem = (MyListItem) convertView.getTag();
        }

        myListItem.tv_sp02.setText("血氧：" + lists.get(position).getSp02());
        myListItem.tv_pr.setText("脉率：" + lists.get(position).getPr());
        myListItem.tv_pi.setText("血流灌注值：" + lists.get(position).getPi());
        myListItem.tv_time.setText("时间：" + lists.get(position).getTime());
        return convertView;
    }

    public final class MyListItem {
        TextView tv_time;
        TextView tv_sp02;
        TextView tv_pr;
        TextView tv_pi;
    }

}
