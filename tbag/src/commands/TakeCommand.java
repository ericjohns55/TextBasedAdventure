package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class TakeCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		
		if (location == null || location.equals("room") || location.equals("floor")) {
			if (room.hasItem(noun)) {
				Item toGrab = room.getItem(noun);
				
				if (toGrab != null) {
					game.take(room, toGrab, getPlayer(), noun);
					game.setOutput("You picked up " + noun + ".");
				} else {
					game.setOutput("This item does not exist in your current room.");
				}
			} else if (noun == null) {
				game.setOutput("Please specify an item.");
			} else {
				game.setOutput("This item does not exist in your current room.");
			}
		} else {
			if (room.hasObject(location)) {
				RoomObject roomObject = room.getObject(location);
				
				if (roomObject.canHoldItems()) {
					Inventory objectInventory = roomObject.getInventory();
					
					if (!roomObject.isLocked()) {
						if (objectInventory.contains(noun)) {
							Item toGrab = objectInventory.removeItem(noun);
														
							game.take(roomObject, toGrab, getPlayer(), puzzle, noun);
						} else {
							game.setOutput("This object does not have that item.");
						}
					} else {
						game.setOutput("This " + roomObject.getName() + " is locked.");
					}									
				} else {
					game.setOutput("This object does not possess any items.");
				}
			} else {
				game.setOutput("Could not find that object.");
			}
		}
	}
}
