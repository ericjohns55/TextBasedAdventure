package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import items.Item;
import map.UnlockableObject;

public class TestUnlockableObject {
	
	private UnlockableObject unlockableObject;
	private Item item;
	
	
	@Before
	public void setUp() {
		//public UnlockableObject(String name, String description, String direction, boolean blockingExit, String unlockItem)
		item = new Item("moose");
		unlockableObject = new UnlockableObject("name", "description", "direction", true, item, 4);
	}

	
	@Test
	public void testGetUnlockItem() {
		assertEquals(item, unlockableObject.getUnlockItem());
	}
	
	@Test
	public void testSetUnlockItem() {
		unlockableObject.setUnlockItem(item);
		assertEquals(item, unlockableObject.getUnlockItem());
	}
	
	
	@Test
	public void testConsumeItem() {
		assertEquals(true, unlockableObject.consumeItem());
	}
		
	@Test
	public void testSetConsumeItem() {
		unlockableObject.setConsumeItem(false);
		assertEquals(false, unlockableObject.consumeItem());
	}
	
	//this.setUnlockable(true);
	//this.setLocked(true);
	@Test
	public void testSetUnlockable() {
		unlockableObject.setUnlockable(false);
		assertEquals(false, unlockableObject.isUnlockable());
	}
	
	@Test
	public void testSetLocked() {
		unlockableObject.setUnlockable(true);
		unlockableObject.setLocked(true);
		
		assertEquals(true, unlockableObject.isLocked());
	}
	
	
	
	
	// One test from testRoomObject
	@Test
	public void testSetDirection() {
		unlockableObject.setDirection("p");
		assertEquals("p", unlockableObject.getDirection());
	}
	
	
	
	
	
	
	
	
}
