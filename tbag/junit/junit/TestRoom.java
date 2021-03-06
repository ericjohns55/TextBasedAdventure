package junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import items.Item;
import map.Room;


public class TestRoom {

	// Each room needs to have to have a puzzle and items
	// My approach is similar to the inventory class
	
	private Room room;
	
	@Before
	public void setUp() {
		room = new Room("roomTest", 3);
	}
	
	// Make sure each room has the the set items in it, feel like I shouldnt include an addItem because i should be setting.
	// But the items putting down should be added to the designated room.
	@Test
	public void testItemsInRoom() {
		Item testItem = new Item();
		room.addItem("sandwich1", testItem);
		assertTrue(room.hasItem("sandwich1"));
	}
	
	@Test
	public void testHasExit() {
		Room room2 = new Room("room2", 5);
		room.addExit("north", room2);
		assertTrue(room.hasExit("north"));
	}
	
	@Test
	public void testExit() {
		Room room2 = new Room("room2", 5);
		room.addExit("north", room2);
		assertEquals(room2.getRoomID(), room.getExit("north"));
	}
	
	@Test
	public void testRemoveItem() {
		Item testItem = new Item();
		room.addItem("sandwich1", testItem);
		room.removeItem("sandwich1");
		assertFalse(room.hasItem("sandwich1"));
	}
	
	// Need to test to make sure it has a puzzle too
	
	@Test
	public void testGetCanSee() {

		assertTrue(room.getCanSee() == true);
	}
	
	@Test
	public void testSetCanSee() {

		room.setCanSee(false);
		assertTrue(room.getCanSee() == false);
	}
	
	
	
	
	
}

