package junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import items.Inventory;
import items.Item;


public class TestInventory {
	private Inventory inventory;
	
	@Before
	public void setUp() {
		inventory = new Inventory();
	}
	
	@Test
	public void testAddItem() {
		Item testItem = new Item();
		testItem.setCanBePickedUp(true);
		inventory.addItem("sandwich1", testItem);
		assertTrue(inventory.contains("sandwich1"));
	}
	
	@Test
	public void testGetItem() {
		Item newItem = new Item();
		newItem.setCanBePickedUp(true);
		inventory.addItem("testItem1", newItem);
		Item grabItem = inventory.getItem("testItem1");
		grabItem.setCanBePickedUp(true);
		assertTrue(grabItem.equals(newItem));
	}
	
	@Test
	public void canAddItem() {
		inventory.emptyInventory();
		
		Item item1 = new Item();
		item1.setWeight(25.0);
		inventory.addItem("item1", item1);
		
		Item item2 = new Item();
		item2.setWeight(13.0);
		
		assertTrue(inventory.canAddItem(item2));	
	}
	
	@Test
	public void cantAddItem() {
		inventory.emptyInventory();
		
		Item item1 = new Item();
		item1.setWeight(25.0);
		inventory.addItem("item1", item1);
		
		Item item2 = new Item();
		item2.setWeight(33.0);
		
		assertFalse(inventory.canAddItem(item2));	
	}
	
	@Test
	public void testCurrentWeight() {
		inventory.emptyInventory();
		
		Item item1 = new Item("item1", 25, false);
		Item item2 = new Item("item2", 12, false);

		inventory.addItem("item1", item1);
		inventory.addItem("item2", item2);
	
		assertEquals(37.0, inventory.getCurrentWeight(), 0.001);
	}
	
	@Test
	public void testInventorySize() {
		inventory.emptyInventory();
		
		Item item1 = new Item("item1", 25, false);
		Item item2 = new Item("item2", 12, false);

		inventory.addItem("item1", item1);
		inventory.addItem("item2", item2);
	
		assertEquals(2, inventory.getInventorySize());
	}
}
