package com.dreiri.smarping.models;

import java.util.ArrayList;

public class List {
    
    private ArrayList<Item> items;
    
    public List() {
        items = new ArrayList<Item>();
    }
    
    public boolean add(Item item) {
        if (has(item)) {
            throw new AlreadyExists("Can not add given item, it is already in the list.");
        }
        return items.add(item);
    }
    
    public boolean remove(Item item) {
        return findItemAndExecuteAction(item, new Callback() {
            @Override
            public boolean execute(Item item) {
                return items.remove(item);
            }
        });
    }
    
    public boolean remove(String name) {
        return remove(new Item(name));
    }
    
    public void clear() {
        items.clear();
    }
    
    public int size() {
        return items.size();
    }
    
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public Item get(int index) {
        return items.get(index);
    }
    
    public boolean has(Item item) {
        return findItemAndExecuteAction(item, new Callback() {
            @Override
            public boolean execute(Item item) {
                return true;
            }
        });
    }
    
    public boolean has(String name) {
        return has(new Item(name));
    }
    
    private boolean findItemAndExecuteAction(Item item, Callback callback) {
        for (Item itemInList: items) {
            if (itemInList.equals(item)) {
                return callback.execute(itemInList);
            }
        }
        return false;
    }
    
    private interface Callback {
        public boolean execute(Item item);
    }
    
    public class AlreadyExists extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        public AlreadyExists(String message) {
            super(message);
        }
        
    }
    
}