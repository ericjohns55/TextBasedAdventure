package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class PourCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		
		if (location != null) {	// check location
			if (room.hasObject(location)) {
				if (inventory.contains(noun)) {
					RoomObject object = room.getObject(location);	// grab location and item from command
					Item item = inventory.getItem(noun);
					
					if (item.isPourable()) {	// confirm item is pourable and item is coverable
						if (object.isCoverable()) {
							if (!object.isCovered()) {		// if not covered, cover and update DB
								game.pour(object, item, getPlayer(), puzzle, noun, location);														
							} else {	// objects cannot be covered twice
								game.setOutput("This object is already covered.");
							}
						} else {
							game.setOutput("Cannot pour " + noun + " on " + location + ".");
						}
					} else {
						game.setOutput("You cannot pour a " + noun + ".");
					}
				} else {
					game.setOutput("You do not possess a " + noun + ".");
				}
			} else {
				game.setOutput("A " + location + " does not exist in your room.");
			}
		} else {
			game.setOutput("I am not sure where to pour that.");
		}
	}

}
