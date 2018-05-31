package com.fanny.ghmf.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.BloodOxyValueInfo;
import com.fanny.ghmf.bean.EcgValueInfo;
import com.fanny.ghmf.fragment.FragmentRecord;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class EcgAdapter extends BaseAdapter {
    ArrayList<EcgValueInfo> lists;
    Activity activity;
    FragmentRecord fragmentRecord;

    public EcgAdapter(Activity activity, FragmentRecord fragmentRecord) {
        this.activity = activity;
        this.fragmentRecord = fragmentRecord;

    }

    public void setData(ArrayList<EcgValueInfo> data) {
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
            convertView = LinearLayout.inflate(activity, R.layout.ecg_item, null);
            myListItem.tv_ecg = (TextView) convertView.findViewById(R.id.item_ecg);
            myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time_ecg);
            convertView.setTag(myListItem);
        } else {
            myListItem = (MyListItem) convertView.getTag();
        }

        myListItem.tv_ecg.setText("心率：" + lists.get(position).getValue());
        myListItem.tv_time.setText("时间：" + lists.get(position).getTime());
        return convertView;
    }

    public final class MyListItem {
        TextView tv_time;
        TextView tv_ecg;
    }

}
