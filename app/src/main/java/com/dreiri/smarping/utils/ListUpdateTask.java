package com.dreiri.smarping.utils;

import android.app.Activity;
import android.widget.Toast;

import com.dreiri.smarping.R;
import com.dreiri.smarping.adapters.ItemAdapter;
import com.dreiri.smarping.exceptions.AlreadyExistsException;
import com.dreiri.smarping.exceptions.NullValueException;
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;

public class ListUpdateTask implements Runnable {

    private Activity activity;
    private List list;
    private ItemAdapter adapter;
    private String itemName;

    public ListUpdateTask(Activity activity, List list, ItemAdapter adapter, String itemName) {
        this.activity = activity;
        this.list = list;
        this.adapter = adapter;
        this.itemName = itemName;
    }

    @Override
    public void run() {
        try {
            Item item = new Item(itemName);
            list.add(item);
        } catch (NullValueException e) {
            Toast.makeText(activity, R.string.toast_null_value, Toast.LENGTH_SHORT).show();
        } catch (AlreadyExistsException e) {
            Toast.makeText(activity, R.string.toast_already_exists, Toast.LENGTH_SHORT).show();
        }
        adapter.refreshWithNewData(list);
        PersistenceManager persistenceManager = new PersistenceManager(activity);
        persistenceManager.saveList(list);
    }

}