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

import com.dreiri.smarping.R;
import com.dreiri.smarping.activities.ListActivity;
import com.dreiri.smarping.adapters.ItemAdapter;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.utils.ListUpdateTask;

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
                    if (getActivity() != null) {
                        ListActivity listActivity = (ListActivity) getActivity();
                        List list = listActivity.list;
                        ItemAdapter adapter = listActivity.itemAdapter;
                        EditText editTextNewItem = (EditText) v;
                        String itemName = editTextNewItem.getText().toString();
                        listActivity.runOnUiThread(new ListUpdateTask(getActivity(), list, adapter, itemName));
                        listActivity.scrollToTop();
                        editTextNewItem.setText("");
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        return layout;
    }

}