package com.dreiri.smarping.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreiri.smarping.exceptions.NullValue;

public class Item implements Parcelable {
    
    public String name;
    
    public Item(String name) {
        name = name.trim();
        if (name.isEmpty()) {
            throw new NullValue("Can not create item with empty name.");
        } else {
            this.name = name;
        }
    }
    
    public Item(Parcel orig) {
        new Item(orig.readString());
    }
    
    public boolean equals(Item item) {
        return name.equalsIgnoreCase(item.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
    
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    
}