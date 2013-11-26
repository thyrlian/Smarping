package com.dreiri.smarping.models.test;

import android.test.AndroidTestCase;

import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;

public class ListTest extends AndroidTestCase {
    
    List list;
    int sizeOriginal;
    
    protected void setUp() throws Exception {
        super.setUp();
        list = new List();
        list.add(new Item("Eggs"));
        list.add(new Item("Milk"));
        list.add(new Item("Bread"));
        sizeOriginal = list.size();
    }

    public void testAdd() {
        Item item = new Item("Yogurt");
        assertFalse(list.has(item));
        list.add(item);
        assertEquals(sizeOriginal + 1, list.size());
        assertTrue(list.has(item));
    }

    public void testRemove() {
        assertFalse(list.remove(new Item("Egg")));
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.remove("Breast"));
        assertEquals(sizeOriginal, list.size());
        assertTrue(list.remove(new Item("Eggs")));
        assertEquals(sizeOriginal - 1, list.size());
        assertTrue(list.remove(" bread "));
        assertEquals(sizeOriginal - 2, list.size());
    }

    public void testSize() {
        assertEquals(3, list.size());
    }

    public void testGet() {
        Item item = new Item("Beer");
        list.add(item);
        assertEquals(item, list.get(sizeOriginal));
    }

    public void testHas() {
        assertTrue(list.has(new Item("Milk")));
        assertTrue(list.has(new Item(" milk ")));
        assertFalse(list.has(new Item("Milch")));
        assertTrue(list.has(" bread "));
        assertTrue(list.has("Bread"));
        assertFalse(list.has("Brot"));
    }

}