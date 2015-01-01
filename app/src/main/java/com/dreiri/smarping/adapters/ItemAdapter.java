package com.dreiri.smarping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dreiri.smarping.R;
import com.dreiri.smarping.activities.ListActivity;
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private List list;
    private Context context;
    private ListActivity activity;
    private LayoutInflater inflater;
    private View.OnTouchListener onTouchListener;
    private ViewHolder viewHolder;
    private ArrayList<Boolean> checkBoxStates = new ArrayList<Boolean>();

    private class ViewHolder {
        TextView textViewItemName;
        CheckBox checkBox;
    }

    public ItemAdapter(Context context, List list, View.OnTouchListener onTouchListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.activity = (ListActivity) context;
        this.list = list;
        this.onTouchListener = onTouchListener;
        resetCheckBoxStates();
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_list_row, null, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewItemName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        for (int i = checkBoxStates.size(); i < list.size(); i++) {
            checkBoxStates.add(0, false);
        }
        final Item item = list.get(position);
        viewHolder.textViewItemName.setText(item.name);
        viewHolder.checkBox.setChecked(checkBoxStates.get(position));
        viewHolder.checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
                checkBoxStates.set(position, checkBox.isChecked());
                activity.updateMenu();
            }
        });
        viewHolder.textViewItemName.setOnTouchListener(onTouchListener);

        return convertView;
    }

    public int[] getIndexesOfCheckedItems() {
        ArrayList<Integer> checkedItemsIndexes = new ArrayList<Integer>();
        for (int i = 0; i < checkBoxStates.size(); i++) {
            if (checkBoxStates.get(i)) {
                checkedItemsIndexes.add(i);
            }
        }
        int[] checkedIndexes = new int[checkedItemsIndexes.size()];
        for (int i = 0; i < checkedItemsIndexes.size(); i++) {
            checkedIndexes[i] = checkedItemsIndexes.get(i);
        }
        return checkedIndexes;
    }

    public void setCheckedItems(ArrayList<Integer> checkedItemsIndexes) {
        checkBoxStates.clear();
        for (int i = 0; i < getCount(); i++) {
            checkBoxStates.add(checkedItemsIndexes.contains(i));
        }
    }

    private void resetCheckBoxStates() {
        checkBoxStates.clear();
        for (int i = 0; i < getCount(); i++) {
            checkBoxStates.add(false);
        }
    }

    public void remove(Object object) {
        list.remove((Item) object);
        notifyDataSetChanged();
        PersistenceManager persistenceManager = new PersistenceManager(context);
        persistenceManager.saveList(list);
    }

    public void refreshWithNewData(List list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void refreshWithNewDataAndResetCheckBoxes(List list) {
        this.list = list;
        resetCheckBoxStates();
        notifyDataSetChanged();
    }

}