package com.dreiri.smarping.models;

import com.dreiri.smarping.exceptions.NullValue;

public class Item {
    
    public String name;
    
    public Item(String name) {
        name = name.trim();
        if (name.isEmpty()) {
            throw new NullValue("Can not create item with empty name.");
        } else {
            this.name = name;
        }
    }
    
    public boolean equals(Item item) {
        return name.equalsIgnoreCase(item.name);
    }
    
}