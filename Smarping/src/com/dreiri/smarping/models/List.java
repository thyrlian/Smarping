package com.dreiri.smarping.models;

import java.util.ArrayList;

public class List {
    
    private ArrayList<Item> items;
    
    public List() {
        items = new ArrayList<Item>();
    }
    
    public Boolean add(Item item) {
        return items.add(item);
    }
    
    public Boolean remove(Item item) {
        for (Item itemInList : items) {
            if (itemInList.equals(item)) {
                return items.remove(itemInList);
            }
        }
        return false;
    }
    
    public Boolean remove(String name) {
        for (Item itemInList : items) {
            if (itemInList.name.equalsIgnoreCase(name.trim())) {
                return items.remove(itemInList);
            }
        }
        return false;
    }
    
    public int size() {
        return items.size();
    }
    
    public Item get(int index) {
        return items.get(index);
    }
    
    public boolean has(Item item) {
        for (Item itemInList: items) {
            if (itemInList.equals(item)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean has(String name) {
        for (Item itemInList: items) {
            if (itemInList.name.equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }
    
}