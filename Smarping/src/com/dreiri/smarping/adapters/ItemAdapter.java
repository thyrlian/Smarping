
package com.dreiri.smarping.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dreiri.smarping.R;
import com.dreiri.smarping.fragments.EditItemDialogFragment;
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;

public class ItemAdapter extends BaseAdapter {

    private List list;
    private FragmentManager fragmentManager;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private ArrayList<Boolean> checkBoxStates = new ArrayList<Boolean>();

    private class ViewHolder {
        TextView textViewItemName;
        CheckBox checkBox;
    }

    public ItemAdapter(Context context, List list) {
        this.inflater = LayoutInflater.from(context);
        this.fragmentManager = ((Activity) context).getFragmentManager();
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_list_row, null);
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

        viewHolder.textViewItemName.setOnClickListener(new OnClickListener() {
            static final int DOUBLE_CLICK_DELAY = 300;
            long lastClickTime = 0;
            long thisClickTime;

            @Override
            public void onClick(View v) {
                thisClickTime = System.currentTimeMillis();
                if (thisClickTime - lastClickTime < DOUBLE_CLICK_DELAY) {
                    lastClickTime = 0;
                    EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment.newInstance(position, item.name);
                    editItemDialogFragment.show(fragmentManager, "item");
                } else {
                    lastClickTime = thisClickTime;
                }
            }
        });

        viewHolder.checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
                checkBoxStates.set(position, checkBox.isChecked());
            }
        });

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

    private void resetCheckBoxStates() {
        checkBoxStates.clear();
        for (int i = 0; i < getCount(); i++) {
            checkBoxStates.add(false);
        }
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
