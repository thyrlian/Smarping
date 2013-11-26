package com.dreiri.smarping.models;

import java.util.ArrayList;

import com.dreiri.smarping.utils.Action;

public class List {
    
    private ArrayList<Item> items;
    
    public List() {
        items = new ArrayList<Item>();
    }
    
    public boolean add(Item item) {
        return items.add(item);
    }
    
    public boolean remove(Item item) {
        return iterateThruItemsAndPerformActionWhenFind(item, new Action() {
            @Override
            public boolean take(Item item) {
                return items.remove(item);
            }
        });
    }
    
    public boolean remove(String name) {
        return remove(new Item(name));
    }
    
    public int size() {
        return items.size();
    }
    
    public Item get(int index) {
        return items.get(index);
    }
    
    public boolean has(Item item) {
        return iterateThruItemsAndPerformActionWhenFind(item, new Action() {
            @Override
            public boolean take(Item item) {
                return true;
            }
        });
    }
    
    public boolean has(String name) {
        return has(new Item(name));
    }
    
    private boolean iterateThruItemsAndPerformActionWhenFind(Item item, Action action) {
        for (Item itemInList: items) {
            if (itemInList.equals(item)) {
                return action.take(itemInList);
            }
        }
        return false;
    }
    
}