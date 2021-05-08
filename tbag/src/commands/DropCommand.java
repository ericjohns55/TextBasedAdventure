package commands;

import java.util.HashMap;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class DropCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Inventory inventory = getInventory();
		Room room = getRoom();
		Puzzle puzzle = getPuzzle();
		
		Game game = getGame();
		
		if (noun == null) {	// make sure an item was specified
			game.setOutput("Please specify an item.");
		} else if (inventory.contains(noun) || noun.equals("all")) {	// confirm that the noun exists in inventory (or it is all)
			if (location == null || location.equals("room") || location.equals("floor")) {	// check if there is no location or confirm it means in room
				if (noun.equals("all")) {	// check edge case for "drop all"
					if (!inventory.isEmpty()) {	// confirm inventory is not empty
						HashMap<String, Item> items = inventory.getAllItems();
						
						for (String identifier : items.keySet()) {	// loop through items and drop
							game.addOutput("You dropped " + identifier + " on the floor.\n");
							game.dropItem(room, identifier, getPlayer(), puzzle);
						}
					} else {
						game.setOutput("You possess nothing to drop.");
					}					
				} else {	// item exists and not all, redirect to game for DB update
					game.dropItem(room, noun, getPlayer(), puzzle);
					game.setOutput("You dropped " + noun + " on the floor.");	
				}
			} else {
				if (room.hasObject(location)) {	// location specified, check that it exists
					RoomObject roomObject = room.getObject(location);

					// make sure object can hold these items
					if (roomObject.canHoldItems()) {
						if (noun.equals("all")) {	// drop all, same logic as before
							if (!inventory.isEmpty()) {
								HashMap<String, Item> items = inventory.getAllItems();
								
								// drop all items on the room object instead of the ground
								for (String identifier : items.keySet()) {
									game.dropItem(roomObject, identifier, getPlayer(), puzzle, location);
								}
							} else {
								game.setOutput("You possess nothing to drop.");
							}
						} else {
							// not drop all, drop single item on object
							game.dropItem(roomObject, noun, getPlayer(), puzzle, location);
						}
					} else {
						game.setOutput("This object cannot hold that...");
					}
				} else {
					game.setOutput("Could not find that object.");
				}
			}
		} else {
			game.setOutput("You do not possess this item.");
		}
	}
}
