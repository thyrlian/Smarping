package com.dreiri.smarping.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.dreiri.smarping.models.List;
import com.dreiri.smarping.utils.Callback;

public class PersistenceManager {

    private SharedPreferences prefs;
    private Editor editor;
    private String appIdentifier = "Smarping";
    private String keyTotalNumber = appIdentifier + "_total_number";
    private String keyItem = appIdentifier + "_item_";

    public PersistenceManager(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveList(List list) {
        clearAllSavedItems();
        setNumberOfItems(list.size());
        for (int i = 0; i < list.size(); i++) {
            String key = keyItem + i;
            saveString(key, list.get(i).name);
        }
    }

    public List readList() {
        List list = new List();
        int totalNum = getNumberOfItems();
        for (int i = totalNum - 1; i >= 0; i--) {
            String key = keyItem + i;
            String value = prefs.getString(key, null);
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    public void updateItem(final String key, final String value) {
        modifyValues(new Callback() {
            @Override
            public void execute() {
                editor.putString(key, value);
            }
        });
    }

    public void removeItems(final String[] keys) {
        modifyValues(new Callback() {
            @Override
            public void execute() {
                for (String key : keys) {
                    editor.remove(key);
                }
            }
        });
    }

    private void modifyValues(Callback callback) {
        this.editor = prefs.edit();
        callback.execute();
        editor.apply();
    }

    public void saveString(final String key, final String value) {
        modifyValues(new Callback() {
            @Override
            public void execute() {
                editor.putString(key, value);
            }
        });
    }

    public void saveInt(final String key, final int value) {
        modifyValues(new Callback() {
            @Override
            public void execute() {
                editor.putInt(key, value);
            }
        });
    }

    public String getString(String key) {
        return prefs.getString(key, null);
    }

    private void clearAllSavedItems() {
        modifyValues(new Callback() {
            @Override
            public void execute() {
                int totalItems = getNumberOfItems();
                for (int i = 0; i < totalItems; i++) {
                    String key = keyItem + i;
                    editor.remove(key);
                }
                editor.remove(keyTotalNumber);
            }
        });
    }

    private int getNumberOfItems() {
        return prefs.getInt(keyTotalNumber, 0);
    }

    private void setNumberOfItems(int numberOfItems) {
        saveInt(keyTotalNumber, numberOfItems);
    }

}
