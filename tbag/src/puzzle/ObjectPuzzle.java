package puzzle;

import items.Item;
import map.RoomObject;

public class ObjectPuzzle extends Puzzle {
	private RoomObject object;
	private Item requiredItem;
	
	private int objectID;
	private int itemID;
	
	public ObjectPuzzle(String description, String solution, String hint, RoomObject object, Item requiredItem, String unlockObstacle, int roomID) {
		super(description, solution, hint, false, unlockObstacle, roomID);
		this.object = object;
		this.requiredItem = requiredItem;
		this.objectID = object.getObjectID();
		this.itemID = requiredItem.getItemID();
	}
	
	public boolean isSolved() {
		return object.getInventory().contains(requiredItem);
	}

	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
}