package com.basgeekball.smarping.test.models;

import android.test.AndroidTestCase;

import com.basgeekball.smarping.exceptions.NullValueException;
import com.basgeekball.smarping.models.Item;

public class ItemTest extends AndroidTestCase {

    Item itemA;
    Item itemB;
    Item itemC;
    Item itemD;

    protected void setUp() throws Exception {
        super.setUp();
        itemA = new Item("Milk");
        itemB = new Item("milk");
        itemC = new Item(" Milk ");
        itemD = new Item("Bread");
    }

    public void testEqualsItem() {
        assertTrue(itemA.equals(itemB));
        assertTrue(itemA.equals(itemC));
        assertFalse(itemA.equals(itemD));
    }

    public void testCreateItemWithEmptyValue() {
        try {
            new Item(" ");
        } catch (NullValueException e) {
            assertEquals("Can not create item with empty name.", e.getMessage());
        }
    }

}