package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import items.Item;
import puzzle.Chest;


public class TestChest {
	private Chest chest;

	@Before
	public void setUp() {
		chest = new Chest(); 
	}

	@Test
	public void testsetName() {
		chest.setName("chest");
		assertEquals("chest", chest.getName());
	}

	@Test
	public void testItemsInChest() {
		Item testItem = new Item();
		chest.addItem("book", testItem);
		assertTrue(chest.contains("book"));
	}

	@Test
	public void testGetItem() {
		Item testItem = new Item();
		chest.addItem("testItem", testItem);
		assertEquals(testItem, chest.getItem("testItem"));
	}

	@Test
	public void testRemoveItem() {
		Item testItem = new Item();
		chest.addItem("book", testItem);
		chest.removeItem("book");
		assertFalse(chest.contains("book"));
	}

	@Test
	public void solve() {
		Item testItem = new Item();
		chest.addItem("testItem", testItem);
		chest.setSolution("key");
		Item key = new Item();
		key.setName("key");
		chest.solve(key.getName());
		assertTrue(chest.getIsSovled());
		assertTrue(testItem.canBePickedUp());
	}

}