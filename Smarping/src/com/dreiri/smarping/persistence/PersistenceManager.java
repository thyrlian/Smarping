package com.dreiri.smarping.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.dreiri.smarping.models.List;

public class PersistenceManager {
    
    private SharedPreferences prefs;
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
    
    private void saveString(String key, String value) {
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    private void saveInt(String key, int value) {
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    
    private void clearAllSavedItems() {
        Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    private int getNumberOfItems() {
        return prefs.getInt(keyTotalNumber, 0);
    }

    private void setNumberOfItems(int numberOfItems) {
        saveInt(keyTotalNumber, numberOfItems);
    }
    
}
