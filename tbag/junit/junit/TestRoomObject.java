package junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
}
