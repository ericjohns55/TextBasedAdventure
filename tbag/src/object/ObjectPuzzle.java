package object;

import items.Item;
import map.RoomObject;

public class ObjectPuzzle extends Puzzle {
	private RoomObject object;
	private Item requiredItem;
	
	public ObjectPuzzle(String description, String solution, String hint, RoomObject object, Item requiredItem, String unlockObstacle) {
		super(description, solution, hint, false, unlockObstacle);
		this.object = object;
		this.requiredItem = requiredItem;
	}
	
	public boolean isSolved() {
		return object.getInventory().contains(requiredItem);
	}
}
