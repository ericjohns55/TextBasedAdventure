package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class DropCommand extends UserCommand {
	public DropCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		String location = getLocation();
		
		Inventory inventory = getInventory();
		Room room = getRoom();
		Puzzle puzzle = getPuzzle();
		
		if (inventory.contains(noun)) {
			if (location == null || location.equals("room") || location.equals("floor")) {
				Item removed = inventory.removeItem(noun);
				removed.setInInventory(false);
				room.addItem(noun, removed);
				output = "You dropped " + noun + " on the floor.";
			} else {
				if (room.hasObject(location)) {
					RoomObject roomObject = room.getObject(location);
					
					if (roomObject.canHoldItems()) {
						Inventory objectInventory = roomObject.getInventory();
						
						Item toRemove = inventory.removeItem(noun);
						objectInventory.addItem(noun, toRemove);
						output = "You placed the " + noun + " on the " + location + "."; 
						
						if (puzzle.getDescription().equals("weightPuzzle")) {
							double weightSolution = Double.parseDouble(puzzle.getSolution());
							
							if (objectInventory.getCurrentWeight() >= weightSolution) {
								RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());	
								if (obstacle.isLocked()) {
									obstacle.setLocked(false);
									obstacle.setPreviouslyUnlocked(true);
									output = "A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.";
								}
							}
						}
					} else {
						output = "This object cannot hold that...";
					}
				} else {
					output = "Could not find that object.";
				}
			}
		} else if (noun == null) {
			output = "Please specify an item.";
		} else {
			output = "You do not possess this item.";
		}
		
		return output;
	}

}
