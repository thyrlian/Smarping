package com.basgeekball.smarping.utils;

import android.widget.Toast;

import com.basgeekball.smarping.R;
import com.basgeekball.smarping.activities.ListActivity;
import com.basgeekball.smarping.adapters.ItemAdapter;
import com.basgeekball.smarping.exceptions.AlreadyExistsException;
import com.basgeekball.smarping.exceptions.NullValueException;
import com.basgeekball.smarping.models.Item;
import com.basgeekball.smarping.models.List;
import com.basgeekball.smarping.persistence.PersistenceManager;

public class ListUpdateTask implements Runnable {

    private ListActivity activity;
    private List list;
    private ItemAdapter adapter;
    private String itemName;

    public ListUpdateTask(ListActivity activity, String itemName) {
        this.activity = activity;
        this.list = activity.list;
        this.adapter = activity.itemAdapter;
        this.itemName = itemName;
    }

    @Override
    public void run() {
        try {
            Item item = new Item(itemName);
            list.add(item);
            adapter.refreshWithNewData(list);
            activity.scrollToTop();
            activity.editTextNewItem.setText("");
            PersistenceManager persistenceManager = new PersistenceManager(activity);
            persistenceManager.saveList(list);
        } catch (NullValueException e) {
            Toast.makeText(activity, R.string.toast_null_value, Toast.LENGTH_SHORT).show();
        } catch (AlreadyExistsException e) {
            Toast.makeText(activity, R.string.toast_already_exists, Toast.LENGTH_SHORT).show();
        }
    }

}