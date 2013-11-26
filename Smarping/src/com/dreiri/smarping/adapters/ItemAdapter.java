package com.dreiri.smarping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreiri.smarping.R;
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;

public class ItemAdapter extends BaseAdapter {
    
    private List list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    
    private class ViewHolder {
        TextView textViewItemName;
    }
    
    public ItemAdapter(Context context, List list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }
    
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.fragment_list_row, null);
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewItemName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Item item = list.get(position);
        viewHolder.textViewItemName.setText(item.name);
        return convertView;
    }

}