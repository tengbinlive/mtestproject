package com.binteng.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.binteng.R;
import com.binteng.bean.ActInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<ActInfo> dataList;


    public ActivityListAdapter(Activity context, List<ActInfo> _list) {
        this.dataList = _list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public ActInfo getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_activity_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.activityName.setText(dataList.get(position).getActivityName());

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.activity_name)
        TextView activityName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}