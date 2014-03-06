
package com.dreiri.smarping.models;

import java.util.ArrayList;

import com.dreiri.smarping.exceptions.AlreadyExists;

public class List {

    private ArrayList<Item> items;

    public List() {
        items = new ArrayList<Item>();
    }

    public void add(Item item) {
        if (has(item)) {
            throw new AlreadyExists("Can not add given item, it is already in the list.");
        }
        items.add(0, item);
    }

    public void add(String name) {
        add(new Item(name));
    }

    public void add(Item... items) {
        for (Item item : items) {
            add(item);
        }
    }

    public void add(String... names) {
        for (String name : names) {
            add(name);
        }
    }

    public boolean modify(int index, String newItemName) {
        if (index >= 0 && index < items.size()) {
            if (!has(newItemName)) {
                items.set(index, new Item(newItemName));
                return true;
            } else {
                throw new AlreadyExists("Can not modify with given item, it is already in the list.");
            }
        } else {
            return false;
        }
    }

    public boolean modify(String name, final String newItemName) {
        return findItemAndExecuteAction(new Item(name), new Callback() {
            @Override
            public boolean execute(Item item, int index) {
                if (!has(newItemName)) {
                    items.set(index, new Item(newItemName));
                    return true;
                } else {
                    throw new AlreadyExists("Can not modify with given item, it is already in the list.");
                }
            }
        });
    }

    public boolean remove(Item item) {
        return findItemAndExecuteAction(item, new Callback() {
            @Override
            public boolean execute(Item item, int index) {
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
            public boolean execute(Item item, int index) {
                return true;
            }
        });
    }

    public boolean has(String name) {
        return has(new Item(name));
    }

    public int find(Item item) {
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).equals(item)) {
                return index;
            }
        }
        return -1;
    }

    public int find(String name) {
        return find(new Item(name));
    }

    private boolean findItemAndExecuteAction(Item item, Callback callback) {
        for (int index = 0; index < items.size(); index++) {
            Item itemInList = items.get(index);
            if (itemInList.equals(item)) {
                return callback.execute(itemInList, index);
            }
        }
        return false;
    }

    private interface Callback {
        public boolean execute(Item item, int index);
    }

}
