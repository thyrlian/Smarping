package com.dreiri.smarping.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dreiri.smarping.R;
import com.dreiri.smarping.activities.ListActivity;
import com.dreiri.smarping.adapters.ItemAdapter;
import com.dreiri.smarping.exceptions.AlreadyExistsException;
import com.dreiri.smarping.exceptions.NullValueException;
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;

public class NewItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_new_item, container, false);
        EditText editTextNewItem = (EditText) layout.findViewById(R.id.editTextNewItem);
        editTextNewItem.setImeActionLabel("Add", KeyEvent.KEYCODE_ENTER);
        editTextNewItem.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(final View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                    EditText editTextNewItem = (EditText) v;
                    ListActivity listActivity = (ListActivity) getActivity();
                    List list = listActivity.list;
                    ItemAdapter adapter = listActivity.itemAdapter;
                    String itemName = editTextNewItem.getText().toString();
                    listActivity.runOnUiThread(new ListUpdateTask(list, adapter, itemName));
                    listActivity.scrollToTop();
                    editTextNewItem.setText("");
                    return true;
                } else {
                    return false;
                }
            }
        });
        return layout;
    }

    private class ListUpdateTask implements Runnable {

        private List list;
        private ItemAdapter adapter;
        private String itemName;

        public ListUpdateTask(List list, ItemAdapter adapter, String itemName) {
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
                Toast.makeText(getActivity(), R.string.toast_null_value, Toast.LENGTH_SHORT).show();
            } catch (AlreadyExistsException e) {
                Toast.makeText(getActivity(), R.string.toast_already_exists, Toast.LENGTH_SHORT).show();
            }
            adapter.refreshWithNewData(list);
            PersistenceManager persistenceManager = new PersistenceManager(getActivity());
            persistenceManager.saveList(list);
        }

    }

}
