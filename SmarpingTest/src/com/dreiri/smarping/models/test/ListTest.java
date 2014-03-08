
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

    public void testModifyByIndexSucceed() {
        int sizeOriginal = list.size();
        int index = 0;
        String newItemName = "Vegetables";
        String oldItemName = list.get(index).name;
        list.modify(index, newItemName);
        assertTrue(list.has(newItemName));
        assertFalse(list.has(oldItemName));
        assertEquals(sizeOriginal, list.size());
    }

    public void testModifyByNameSucceed() {
        int sizeOriginal = list.size();
        String oldItemName = "Bread";
        String newItemName = "Vegetables";
        list.modify(oldItemName, newItemName);
        assertTrue(list.has(newItemName));
        assertFalse(list.has(oldItemName));
        assertEquals(sizeOriginal, list.size());
    }

    public void testModifyByIndexFail() {
        int sizeOriginal = list.size();
        String newItemName = "Vegetables";
        list.modify(sizeOriginal, newItemName);
        assertFalse(list.has(newItemName));
        assertEquals(sizeOriginal, list.size());
    }

    public void testModifyByNameFail() {
        int sizeOriginal = list.size();
        String oldItemName = "Bread";
        String newItemName = "Vegetables";
        list.modify(oldItemName + "?", newItemName);
        assertTrue(list.has(oldItemName));
        assertFalse(list.has(newItemName));
        assertEquals(sizeOriginal, list.size());
    }

    public void testModifyByIndexDuplication() {
        try {
            list.modify(0, "Milk");
        } catch (AlreadyExists e) {
            assertEquals("Can not modify with given item, it is already in the list.", e.getMessage());
        }
    }

    public void testModifyByNameDuplication() {
        try {
            list.modify("Eggs", "Eggs");
        } catch (AlreadyExists e) {
            assertEquals("Can not modify with given item, it is already in the list.", e.getMessage());
        }
    }

    public void testRemove() {
        assertFalse(list.remove(new Item("Egg")));
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.has("Egg"));
        assertFalse(list.remove("Breast"));
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.has("Breast"));
        assertTrue(list.remove(new Item("Eggs")));
        assertEquals(sizeOriginal - 1, list.size());
        assertFalse(list.has("Eggs"));
        assertTrue(list.remove(" bread "));
        assertEquals(sizeOriginal - 2, list.size());
        assertFalse(list.has("Bread"));
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

    public void testFind() {
        assertEquals(2, list.find("Eggs"));
        assertEquals(0, list.find(new Item("Bread")));
        assertEquals(-1, list.find(new Item("Bacon")));
    }

}
