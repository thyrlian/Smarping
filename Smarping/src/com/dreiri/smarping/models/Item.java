package com.dreiri.smarping.models;

public class Item {
    
    public String name;
    
    public Item(String name) {
        this.name = name.trim();
    }
    
    public boolean equals(Item item) {
        return name.equalsIgnoreCase(item.name);
    }
    
}