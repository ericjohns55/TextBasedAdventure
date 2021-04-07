package junit;

import org.junit.Before;
import org.junit.Test;

import items.Item;

import static org.junit.Assert.*;

import map.RoomObject;

public class TestRoomObject {
	private RoomObject object;
	
	@Before
	public void setUp() {
		object = new RoomObject("name", "desc", "dir", false, false, false);
	}
	
	@Test
	public void testGettersSetters() {
		String name = "NAME";
		String description = "DESCRIPTION";
		String direction = "DIRECTION";
		String covered = "YOLO";
		boolean unlockable = true;
		boolean isInteractable = true;
		boolean canHoldItems = true;
		boolean blockingExit = true;
		boolean moveable = true;
		boolean isObstacle = true;
		boolean coverable = true;
		
		object.setName(name);
		object.setDescription(description);
		object.setDirection(direction);
		object.cover(covered);
		object.setUnlockable(unlockable);
		object.setInteractable(isInteractable);
		object.setCanHoldItems(canHoldItems);
		object.setBlockingExit(blockingExit);
		object.setMoveable(moveable);
		object.setObstacle(isObstacle);
		object.setCoverable(coverable);
		
		assertEquals(name, object.getName());
		assertEquals(description, object.getDescription());
		assertEquals(direction, object.getDirection());
		assertEquals(covered, object.getCovering());
		assertEquals(unlockable, object.isUnlockable());
		assertEquals(canHoldItems, object.canHoldItems());
		assertEquals(blockingExit, object.isBlockingExit());
		assertEquals(moveable, object.isMoveable());
		assertEquals(isObstacle, object.isObstacle());
		assertEquals(coverable, object.isCoverable());
		assertEquals(0, object.getInventory().getInventorySize());
	}
	
	@Test
	public void testLockedFalse() {
		object.setLocked(true);
		object.setUnlockable(false);
		
		assertFalse(object.isLocked());
	}
	
	@Test
	public void testLockedTrue() {
		object.setLocked(true);
		object.setUnlockable(true);
		
		assertTrue(object.isLocked());
	}
  
  
  
  
    // michaelfeldman's tests
    // Testing the getters
	@Test
	public void testGetName() {
		assertEquals("name", object.getName());
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("desc", object.getDescription());
	}
	
	@Test
	public void testGetDirection() {
		assertEquals("dir", object.getDirection());
	}
	
	@Test
	public void testIsObstacle() {
		assertEquals(false, object.isObstacle());
	}
	
	@Test
	public void testIsBlockingExit() {
		assertEquals(false, object.isBlockingExit());
	}
	
	@Test
	public void testIsMoveable() {
		assertEquals(false, object.isMoveable());
	}
	
	@Test
	public void testGetInventory() {
		Item spring = new Item("sprong", 0.1);
		object.getInventory().addItem("boing", spring);
		assertTrue(object.getInventory().contains("boing"));
	}
	
	// These puppies are automatically set to false when a new roomObject is made 
	@Test
	public void testIsUnlockable() {
		assertEquals(false, object.isUnlockable());
	}
	
	@Test
	public void testIsLocked() {
		assertEquals(false, object.isLocked());
	}
	
	@Test
	public void testIsInteractable() {
		assertEquals(false, object.isInteractable());
	}
	
	@Test
	public void testCanHoldItems() {
		assertEquals(false, object.canHoldItems());
	}
	
	

	// Testing the setters
	// setName, setDescription, setInteractable, setLocked, setDirectioncan, canHoldItems, 
	@Test
	public void testSetName() {
		object.setName("name");
		assertEquals("name",object.getName());
	}
	
	@Test
	public void testSetDescription() {
		object.setDescription("des");
		assertEquals("des", object.getDescription());
	}
	
	@Test
	public void testSetInteractable() {
		object.setInteractable(false);
		assertEquals(false, object.isInteractable());
	}
	
	@Test
	public void testSetDirection() {
		object.setDirection("m");
		assertEquals("m", object.getDirection());
	}
	
	@Test
	public void testSetCanHoldItems() {
		object.setCanHoldItems(true);
		assertEquals(true, object.canHoldItems());
	}
	
	@Test
	public void testSetLocked() {
		object.setUnlockable(true);
		object.setLocked(true);
		
		assertEquals(true, object.isLocked());
	}
	
	@Test
	public void testSetObstacle() {
		object.setObstacle(false);
		assertEquals(false, object.isObstacle());
	}
	
	@Test
	public void testSetUnlockable() {
		object.setUnlockable(true);
		assertEquals(true, object.isUnlockable());
	}
	
	@Test
	public void testSetMoveable() {
		object.setMoveable(false);
		assertEquals(false, object.isMoveable());
	}
	
	@Test
	public void testSetBlockingExit() {
		object.setBlockingExit(false);
		assertEquals(false, object.isBlockingExit());
	}
}
