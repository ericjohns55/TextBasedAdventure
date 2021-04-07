package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import items.Inventory;
import items.Item;
import map.RoomObject;

public class TestRoomObject {

	private RoomObject roomObject;
	
	
	@Before
	public void setUp() {
		//RoomObject(String name, String description, String direction, boolean isObstacle, boolean blockingExit, boolean moveable)
		roomObject = new RoomObject("name", "description", "direction", true, true, true);
	}

	
	// Testing the getters
	@Test
	public void testGetName() {
		assertEquals("name", roomObject.getName());
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("description", roomObject.getDescription());
	}
	
	@Test
	public void testGetDirection() {
		assertEquals("direction", roomObject.getDirection());
	}
	
	@Test
	public void testIsObstacle() {
		assertEquals(true, roomObject.isObstacle());
	}
	
	@Test
	public void testIsBlockingExit() {
		assertEquals(true, roomObject.isBlockingExit());
	}
	
	@Test
	public void testIsMoveable() {
		assertEquals(true, roomObject.isMoveable());
	}
	
	@Test
	public void testGetInventory() {
		Item spring = new Item("sprong", 0.1);
		roomObject.getInventory().addItem("boing", spring);
		assertEquals(spring, roomObject.getInventory().getItem("boing"));
	}
	
	// These puppies are automatically set to false when a new roomObject is made 
	@Test
	public void testIsUnlockable() {
		assertEquals(false, roomObject.isUnlockable());
	}
	
	@Test
	public void testIsLocked() {
		assertEquals(false, roomObject.isLocked());
	}
	
	@Test
	public void testIsInteractable() {
		assertEquals(false, roomObject.isInteractable());
	}
	
	@Test
	public void testCanHoldItems() {
		assertEquals(false, roomObject.canHoldItems());
	}
	
	

	// Testing the setters
	// setName, setDescription, setInteractable, setLocked, setDirectioncan, canHoldItems, 
	@Test
	public void testSetName() {
		roomObject.setName("name");
		assertEquals("name",roomObject.getName());
	}
	
	@Test
	public void testSetDescription() {
		roomObject.setDescription("des");
		assertEquals("des", roomObject.getDescription());
	}
	
	@Test
	public void testSetInteractable() {
		roomObject.setInteractable(false);
		assertEquals(false, roomObject.isInteractable());
	}
	
	@Test
	public void testSetDirection() {
		roomObject.setDirection("m");
		assertEquals("m", roomObject.getDirection());
	}
	
	@Test
	public void testSetCanHoldItems() {
		roomObject.setCanHoldItems(true);
		assertEquals(true, roomObject.canHoldItems());
	}
	
	@Test
	public void testSetLocked() {
		roomObject.setUnlockable(true);
		roomObject.setLocked(true);
		
		assertEquals(true, roomObject.isLocked());
	}
	
	@Test
	public void testSetObstacle() {
		roomObject.setObstacle(false);
		assertEquals(false, roomObject.isObstacle());
	}
	
	@Test
	public void testSetUnlockable() {
		roomObject.setUnlockable(true);
		assertEquals(true, roomObject.isUnlockable());
	}
	
	@Test
	public void testSetMoveable() {
		roomObject.setMoveable(false);
		assertEquals(false, roomObject.isMoveable());
	}
	
	@Test
	public void testSetBlockingExit() {
		roomObject.setBlockingExit(false);
		assertEquals(false, roomObject.isBlockingExit());
	}
	

	
	
	
}
