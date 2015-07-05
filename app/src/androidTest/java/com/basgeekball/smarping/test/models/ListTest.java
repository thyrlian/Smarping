package com.basgeekball.smarping.test.models;

import android.test.AndroidTestCase;

import com.basgeekball.smarping.exceptions.AlreadyExistsException;
import com.basgeekball.smarping.models.Item;
import com.basgeekball.smarping.models.List;

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
        } catch (AlreadyExistsException e) {
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
        } catch (AlreadyExistsException e) {
            assertEquals("Can not modify with given item, it is already in the list.", e.getMessage());
        }
    }

    public void testModifyByNameDuplication() {
        try {
            list.modify("Eggs", "Eggs");
        } catch (AlreadyExistsException e) {
            assertEquals("Can not modify with given item, it is already in the list.", e.getMessage());
        }
    }

    public void testRemoveByItem() {
        assertFalse(list.remove(new Item("Egg")));
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.has("Egg"));
        assertTrue(list.remove(new Item("Eggs")));
        assertEquals(sizeOriginal - 1, list.size());
        assertFalse(list.has("Eggs"));
    }

    public void testRemoveByName() {
        assertFalse(list.remove("Breast"));
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.has("Breast"));
        assertTrue(list.remove(" bread "));
        assertEquals(sizeOriginal - 1, list.size());
        assertFalse(list.has("Bread"));
    }

    public void testRemoveByIndex() {
        assertNull(list.remove(-1));
        assertEquals(sizeOriginal, list.size());
        assertNull(list.remove(3));
        assertEquals(sizeOriginal, list.size());
        assertEquals("Eggs", list.remove(2));
        assertEquals(sizeOriginal - 1, list.size());
        assertFalse(list.has("Eggs"));
        assertEquals("Bread", list.remove(0));
        assertEquals(sizeOriginal - 2, list.size());
        assertFalse(list.has("Bread"));
    }

    public void testRemoveByItemsVarargs() {
        list.remove(new Item("Egg"));
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.has("Egg"));
        list.remove(new Item("Eggs"));
        assertEquals(sizeOriginal - 1, list.size());
        assertFalse(list.has("Eggs"));
        list.add("Goku", "Bardock", "Gohan", "Frieza", "Vegeta", "Nappa");
        int sizeNow = list.size();
        list.remove(new Item("Bardock"), new Item("Freezer"), new Item("Gohan"), new Item("Nappa"));
        assertEquals(sizeNow - 3, list.size());
        assertFalse(list.has("Bardock"));
        assertFalse(list.has("Gohan"));
        assertFalse(list.has("Nappa"));
        assertTrue(list.has("Frieza"));
        assertFalse(list.has("Freezer"));
    }

    public void testRemoveByNamesVarargs() {
        list.remove("Egg");
        assertEquals(sizeOriginal, list.size());
        assertFalse(list.has("Egg"));
        list.remove("Eggs");
        assertEquals(sizeOriginal - 1, list.size());
        assertFalse(list.has("Eggs"));
        list.add("Goku", "Bardock", "Gohan", "Frieza", "Vegeta", "Nappa");
        int sizeNow = list.size();
        list.remove("Nappa", "Freezer", "Bardock", "Gohan");
        assertEquals(sizeNow - 3, list.size());
        assertFalse(list.has("Bardock"));
        assertFalse(list.has("Gohan"));
        assertFalse(list.has("Nappa"));
        assertTrue(list.has("Frieza"));
        assertFalse(list.has("Freezer"));
    }

    public void testRemoveByIndexesVarargs() {
        list.add("Goku", "Bardock", "Gohan", "Frieza", "Vegeta", "Nappa");
        int sizeNow = list.size();
        list.remove(sizeNow + 1, -1, 8, 3, 3, 8, 1);
        assertEquals(sizeNow - 3, list.size());
        assertFalse(list.has("Vegeta"));
        assertFalse(list.has("Gohan"));
        assertFalse(list.has("Eggs"));
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
