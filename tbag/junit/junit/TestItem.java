package junit;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import items.Item;


public class TestItem {
	private Item item;
	
	@Before
	public void setUp() {
		item = new Item("testItem");
	}
	
	@Test
	public void testGetName() {
		assertEquals("testItem", item.getName());
	}
	
	@Test
	public void testSetName() {
		item.setName("test");
		assertEquals("test", item.getName());
	}
	
	@Test
	public void testSetWeight() {
		item.setWeight(5.3);
		assertEquals(5.3, item.getWeight(), 0.001);
	}
	
	@Test
	public void testIsInteractable() {
		item.setInteractable(true);
		assertTrue(item.isInteractable());
	}
	
	@Test
	public void testPickup() {
		item.setCanBePickedUp(true);
		assertTrue(item.canBePickedUp());
	}
	
	@Test
	public void testPourable() {
		item.setPourable(true);
		assertTrue(item.isPourable());
	}
	
	@Test
	public void testDescription() {
		String description = "bruh";
		item.setDescription(description);
		assertEquals(description, item.getDescription());
	}
}
