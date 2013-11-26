package com.dreiri.smarping.utils;

import com.dreiri.smarping.models.Item;

public interface Action {
    public boolean take(Item item);
}