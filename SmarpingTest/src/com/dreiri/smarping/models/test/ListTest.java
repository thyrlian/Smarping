package com.dreiri.smarping.models.test;

import android.test.AndroidTestCase;

import com.dreiri.smarping.exceptions.AlreadyExists;
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
    
    public void testAddDuplication() {
        Item item = new Item("Milk");
        assertTrue(list.has(item));
        try {
            list.add(item);
        } catch (AlreadyExists e) {
            assertEquals("Can not add given item, it is already in the list.", e.getMessage());
        }
    }
    
    public void testAddVarargs() {
        list.add("Computer", "Girl", "Basketball", "Gadget", "Muscle");
        assertEquals(sizeOriginal + 5, list.size());
        assertTrue(list.has("Muscle"));
        List anotherList = new List();
        anotherList.add(new Item("Money"), new Item("Sex"), new Item("Love"));
        assertEquals(3, anotherList.size());
        assertTrue(anotherList.has("Sex"));
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
    
    public void testClear() {
        assertFalse(list.isEmpty());
        list.clear();
        assertTrue(list.isEmpty());
        list.add(new Item("Gadget"));
        assertFalse(list.isEmpty());
    }

    public void testSize() {
        assertEquals(3, list.size());
    }
    
    public void testIsEmpty() {
        assertFalse(list.isEmpty());
        assertTrue(new List().isEmpty());
    }

    public void testGet() {
        Item firstItem = new Item("Eggs");
        Item item = new Item("Beer");
        list.add(item);
        assertTrue(firstItem.equals(list.get(sizeOriginal)));
        assertEquals(item, list.get(0));
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