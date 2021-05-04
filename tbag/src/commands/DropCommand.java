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
		
		if (noun == null) {
			game.setOutput("Please specify an item.");
		} else if (inventory.contains(noun) || noun.equals("all")) {
			if (location == null || location.equals("room") || location.equals("floor")) {
				if (noun.equals("all")) {
					if (!inventory.isEmpty()) {
						HashMap<String, Item> items = inventory.getAllItems();
						
						for (String identifier : items.keySet()) {
							game.addOutput("You dropped " + identifier + " on the floor.\n");
							game.dropItem(room, identifier, getPlayer(), puzzle);
						}
					} else {
						game.setOutput("You possess nothing to drop.");
					}					
				} else {
					game.dropItem(room, noun, getPlayer(), puzzle);
					game.setOutput("You dropped " + noun + " on the floor.");	
				}
			} else {
				if (room.hasObject(location)) {
					RoomObject roomObject = room.getObject(location);

					if (roomObject.canHoldItems()) {
						if (noun.equals("all")) {
							if (!inventory.isEmpty()) {
								HashMap<String, Item> items = inventory.getAllItems();
								
								for (String identifier : items.keySet()) {
									game.addOutput("You placed the " + identifier + " on the " + location + ".\n"); 
									game.dropItem(roomObject, identifier, getPlayer(), puzzle, location);
								}
							} else {
								game.setOutput("You possess nothing to drop.");
							}
						} else {
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
